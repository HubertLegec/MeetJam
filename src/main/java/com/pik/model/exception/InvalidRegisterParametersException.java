package com.pik.model.exception;

import com.pik.model.errors.InvalidRegisterParameterError;

import java.util.List;

import static java.util.stream.Collectors.*;

public class InvalidRegisterParametersException extends BaseException{
    List<InvalidRegisterParameterError> errors;
    List<String> passwordIssues;

    public InvalidRegisterParametersException(List<InvalidRegisterParameterError> errors, List<String> passwordIssues) {
        this.errors = errors;
        this.passwordIssues = passwordIssues;
    }

    @Override
    public List<String> getMessages() {
        List result = errors.stream()
                .map(InvalidRegisterParameterError::getMessage)
                .collect(toList());
        result.addAll(passwordIssues);
        return result;
    }
}
