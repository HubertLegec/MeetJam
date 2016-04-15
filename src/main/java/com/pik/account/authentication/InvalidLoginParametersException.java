package com.pik.account.authentication;

import com.pik.common.BaseException;

import java.util.ArrayList;
import java.util.List;


class InvalidLoginParametersException extends BaseException {
    List<String> errors = new ArrayList<>();

    InvalidLoginParametersException(List<String> errors){
        this.errors = errors;
    }

    InvalidLoginParametersException(String message){
        errors.add(message);
    }

    @Override
    public List<String> getMessages() {
        return errors;
    }
}
