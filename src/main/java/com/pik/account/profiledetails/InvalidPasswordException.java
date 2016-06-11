package com.pik.account.profiledetails;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends RuntimeException {
    private HttpStatus status;

    public InvalidPasswordException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
