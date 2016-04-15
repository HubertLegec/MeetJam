package com.pik.account.registration;

import com.pik.common.BaseException;

import java.util.List;

import static java.util.stream.Collectors.toList;

class InvalidRegisterParametersException extends BaseException {
    List<InvalidRegisterParameterError> errors;
    List<String> passwordIssues;

    InvalidRegisterParametersException(List<InvalidRegisterParameterError> errors, List<String> passwordIssues) {
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
