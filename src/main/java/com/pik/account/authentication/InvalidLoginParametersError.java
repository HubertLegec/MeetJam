package com.pik.account.authentication;

public enum InvalidLoginParametersError {
    NULL_LOGIN_VALUE(0, "Login field must not be empty"),
    NULL_PASSWORD_VALUE(1, "Password field must not be empty"),
    NON_EXISTING_LOGIN(2, "Login doesn't exist"),
    INVALID_PASSWORD(3, "Password doesn't match login");


    private final int code;
    private final String message;

    InvalidLoginParametersError(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
