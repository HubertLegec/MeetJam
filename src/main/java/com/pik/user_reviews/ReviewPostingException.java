package com.pik.user_reviews;


public class ReviewPostingException extends RuntimeException {

    public ReviewPostingException(ReviewPostingError error) {
        super(error.getMessage());
    }
}
