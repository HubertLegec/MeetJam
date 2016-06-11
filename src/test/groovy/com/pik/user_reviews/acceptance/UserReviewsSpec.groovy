package com.pik.user_reviews.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.user_reviews.Review
import com.pik.user_reviews.ReviewPostingError
import com.pik.user_reviews.UserReviewsRepository
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class UserReviewsSpec extends MvcIntegrationSpec{
    @Autowired
    private UserReviewsRepository reviewRepository
    @Shared
    private String token

    private static final SAMPLE_REVIEWS = [
            new Review("Damian","Ala",5,"Brilliant guitarist!"),
            new Review("Damian","Marek",3,"Average guitar player!"),
    ]

    def setup(){
        def response = sendLoginRequest(
                testAccounts[1].getUsername(),
                testAccounts[1].getPassword())
        token = extractTokenFromResponse(response.andReturn().response.contentAsString)
    }

    def cleanup(){
        reviewRepository.deleteAll()
    }

    def 'should find all reviews of user with correct values'(){
        given: 'user Damian has two reviews'
            insertSampleReviews()
        when: 'there was a request for user Damian reviews'
            def response = sendReviewsRequest("Damian")
        then: 'result list contains two sample events is returned'
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            isCorrectReviewList(response, SAMPLE_REVIEWS)
    }

    def 'after posting review, Http status OK should be returned and review should be in database'(){
        given:
            Review reviewToInsert = new Review("Adam", "Zosia", 4, "good bassist")
        when: 'Zosia posts review of Adam'
            def response = sendPostReviewRequest(reviewToInsert.getReviewedLogin(),
                                                 reviewToInsert.getComment(),
                                                 new Integer(reviewToInsert.rating).toString())
        then: 'status OK is returned'
            response.andExpect(status().isOk())

        when:
            List<Review> reviewsInDatabase = reviewRepository.findByReviewedLogin("Adam")
        then: 'review was saved in database'
            reviewsInDatabase.size() != 0
            reviewsInDatabase[0].rating == reviewToInsert.rating
            reviewsInDatabase[0].comment.equals(reviewToInsert.comment)
            reviewsInDatabase[0].reviewerLogin.equals(reviewToInsert.reviewerLogin)
    }

    def 'when someone is trying to add review of user that not exist, proper error message should be returned'(){
        given: "review object with not existing author"
            Review reviewToInsert = new Review("NOT EXISTING", "Zosia", 4, "good bassist")
        when: 'Zosia posts review of not existing user'
            def response = sendPostReviewRequest(reviewToInsert.getReviewedLogin(),
                reviewToInsert.getComment(),
                new Integer(reviewToInsert.rating).toString())
                String message = response.andReturn().response.contentAsString;
        then: 'status Not acceptable and proper message is returned'
            response.andExpect(status().isNotAcceptable())
            message.equals(ReviewPostingError.USER_TO_BE_REVIEWED_NOT_FOUND.message)
    }

    def 'when someone is trying to add second review of the same user, proper message should be sent'(){
        given: 'Zosia has already rated Adam'
            reviewRepository.save(new Review("Adam", "Zosia", 4, "good bassist"))
        when: 'Zosia tries to rate Adam Again'
            def response = sendPostReviewRequest("Adam", "bad bassist", "1")
            String message = response.andReturn().response.contentAsString;
        then: 'status Not acceptable and proper message is returned'
            response.andExpect(status().isNotAcceptable())
            message.equals(ReviewPostingError.USER_REVIEW_ALREADY_EXISTS.message)
    }

    def 'when someone is trying to review himself, proper message should be sent'(){
        when: 'Zosia tries to rate herself'
            def response = sendPostReviewRequest("Zosia", "Excellent drummer, always invite her to jams!", "5")
        String message = response.andReturn().response.contentAsString;
        then: 'status Not acceptable and proper message is returned'
            response.andExpect(status().isNotAcceptable())
            message.equals(ReviewPostingError.USER_TRIED_TO_REVIEW_HIMSELF.message)
    }

    def 'when someone is trying to give invalid value of rating, proper message should be sent'(){
        when: 'Zosia tries to give Adam 10/5 rating'
            def response = sendPostReviewRequest("Adam", "Excellent bassist, always invite him to jams!", "10")
            String message = response.andReturn().response.contentAsString;
        then: 'status Not acceptable and proper message is returned'
            response.andExpect(status().isNotAcceptable())
            message.equals(ReviewPostingError.WRONG_RATING.message)
    }


    private def sendReviewsRequest(String userLogin){
        return mockMvc.perform(get("/api/reviews/ofUser")
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('userLogin', userLogin)
                .param('pageNumber', '0'))
    }

    private def sendPostReviewRequest(String userToRate, String comment, String rating){
        return  mockMvc.perform(post("/api/reviews/post")
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('reviewedUserName', userToRate)
                .param('comment', comment)
                .param('rating', rating))
    }

    def insertSampleReviews() {
        SAMPLE_REVIEWS.each {reviewRepository.save(it)}
    }


    private static boolean isCorrectReviewList(ResultActions response, List<Review> list){
        def jsonResult = new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
        if(reviewsEqual(jsonResult[0], list[0])&&reviewsEqual(jsonResult[1], list[1])
        || reviewsEqual(jsonResult[0], list[1])&&reviewsEqual(jsonResult[1], list[0])){
            return true
        }

        return false
    }

    def parseJson(ResultActions response)
    {
         return new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
    }

    private static boolean reviewsEqual(def fromJson, Review review){
        if(!review.reviewerLogin.equals(fromJson.reviewerLogin)){
            return false
        }
        if(!review.comment.equals(fromJson.comment)){
            return false
        }
        if(review.rating != fromJson.rating){
            return false
        }
        return true
    }
}
