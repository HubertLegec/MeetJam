package com.pik.event;

import org.springframework.http.HttpStatus;

public class EventException extends RuntimeException {
    private HttpStatus status;

    public EventException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
