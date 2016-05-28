package com.pik.event.createremove;

import org.springframework.http.HttpStatus;

public class RemoveEventException extends RuntimeException {
    private HttpStatus status;

    RemoveEventException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    HttpStatus getStatus() {
        return status;
    }
}
