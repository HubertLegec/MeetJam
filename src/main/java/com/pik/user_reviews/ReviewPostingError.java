package com.pik.user_reviews;


public enum ReviewPostingError {
    REVIEWING_USER_NOT_FOUND("Reviewing user not found."),
    USER_TO_BE_REVIEWED_NOT_FOUND("User to be reviewed was not found."),
    USER_TRIED_TO_REVIEW_HIMSELF("User tried to review his own profile."),
    USER_REVIEW_ALREADY_EXISTS("User review already exists on this profile."),
    WRONG_RATING("Rating should be integer bigger than zero and smaller than six.");

    private String message;

    ReviewPostingError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
