package com.pik.user_reviews;


public class ReviewDTO {
    private String reviewerLogin;
    private String comment;
    private int rating;

    public ReviewDTO(String reviewerLogin, String comment, int rating) {
        this.reviewerLogin = reviewerLogin;
        this.comment = comment;
        this.rating = rating;
    }

    public ReviewDTO() {
    }

    public String getReviewerLogin() {
        return reviewerLogin;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
