package com.pik.account.registration;

public enum InvalidRegisterParameterError {
    DUPLICATE_LOGIN("User with given login already exists!"),
    INVALID_EMAIL("Email is invalid!"),
    INVALID_PASSWORD("Password do not match requirements!");

    private final String message;

    InvalidRegisterParameterError(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
