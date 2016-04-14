package com.pik.model.exception;

import java.util.List;

/**
 * Created by hubert.legec on 2016-04-12.
 */
public class InvalidObjectException extends BaseException {
    List<String> errors;

    public InvalidObjectException(List<String> errors){
        this.errors = errors;
    }

    @Override
    public List<String> getMessages() {
        return errors;
    }
}
