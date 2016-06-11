package com.pik.user_reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/api/reviews")
public class UserReviewsController {
    private UserReviewsService userReviewsService;

    @Autowired
    public UserReviewsController(UserReviewsService userReviewsService) {
        this.userReviewsService = userReviewsService;
    }

    @RequestMapping(value = "ofUser", produces = APPLICATION_JSON_VALUE, method = GET)
    public ResponseEntity<List<ReviewDTO>> reviewsOfUser(String userLogin, int pageNumber) {
        List<ReviewDTO> result = userReviewsService.getReviewsOfUser(userLogin, pageNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @RequestMapping(value = "post", produces = APPLICATION_JSON_VALUE, method = POST)
    public ResponseEntity<String> postReview(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                             String reviewedUserName,
                                             String comment,
                                             int rating) {
        userReviewsService.postReview(token, reviewedUserName, comment, rating);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Review posted.");
    }

    @ExceptionHandler(ReviewPostingException.class)
    public ResponseEntity<String> reviewPostingException(ReviewPostingException e) {
        return ResponseEntity.status(NOT_ACCEPTABLE).body(e.getMessage());
    }
}
