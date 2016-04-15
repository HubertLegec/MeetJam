package com.pik.account.registration;

public enum InvalidRegisterParameterError {
    DUPLICATE_LOGIN(0,"User with given login already exists!"),
    INVALID_EMAIL(1,"Email is invalid!"),
    INVALID_PASSWORD(2,"Password do not match requirements!");

    private final int code;
    private final String message;

    InvalidRegisterParameterError(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
