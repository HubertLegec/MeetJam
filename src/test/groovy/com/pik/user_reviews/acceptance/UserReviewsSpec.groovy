package com.pik.user_reviews.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.user_reviews.Review
import com.pik.user_reviews.UserReviewsRepository
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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

    private def sendReviewsRequest(String userLogin){
        mockMvc.perform(get("/api/reviews/ofUser")
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('userLogin', userLogin)
                .param('pageNumber', '0'))
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
        for(int i = 0; i < list.size(); i++){
            if(!reviewsEqual(jsonResult[i], list[i])){
                return false
            }
        }
        return true
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
