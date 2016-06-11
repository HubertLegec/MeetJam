package com.pik.user_reviews;


public class ReviewDTO {
    private String reviewerLogin;
    private String comment;
    private int rating;

    ReviewDTO(String reviewerLogin, String comment, int rating) {
        this.reviewerLogin = reviewerLogin;
        this.comment = comment;
        this.rating = rating;
    }

    ReviewDTO(Review review) {
        this.reviewerLogin = review.getReviewerLogin();
        this.comment = review.getComment();
        this.rating = review.getRating();
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
