package com.pik.user_reviews;

import groovy.transform.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@ToString(includeNames = true)
public class Review {
    @Id
    private String id;
    @Indexed
    private String reviewedLogin;
    private String reviewerLogin;
    private int rating;
    private String comment;

    public Review(String reviewedLogin, String reviewerLogin, int rating, String comment) {
        this.reviewedLogin = reviewedLogin;
        this.reviewerLogin = reviewerLogin;
        this.rating = rating;
        this.comment = comment;
    }

    public String getReviewerLogin() {
        return reviewerLogin;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
