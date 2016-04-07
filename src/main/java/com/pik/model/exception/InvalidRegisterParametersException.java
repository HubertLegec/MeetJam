package com.pik.model.exception;

import com.pik.model.errors.InvalidRegisterParameterError;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidRegisterParametersException extends RuntimeException{

    List<InvalidRegisterParameterError> errors;
    List<String> passwordIssues;

    public InvalidRegisterParametersException(List<InvalidRegisterParameterError> errors, List<String> passwordIssues)
    {
        this.errors = errors;
        this.passwordIssues = passwordIssues;
    }

    public List<String> getMessages()
    {
        List result = errors.stream()
                .map(error -> error.getMessage())
                .collect(Collectors.toList());
        result.addAll(passwordIssues);

        return result;
    }
}
