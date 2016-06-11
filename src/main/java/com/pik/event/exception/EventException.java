package com.pik.event.exception;

import org.springframework.http.HttpStatus;

public class EventException extends RuntimeException {
    private HttpStatus status;

    public EventException(EventsError message, HttpStatus status) {
        super(message.getMessage());
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
