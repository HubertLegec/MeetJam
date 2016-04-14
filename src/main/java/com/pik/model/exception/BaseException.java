package com.pik.model.exception;

import java.util.List;


public abstract class BaseException extends RuntimeException {

    public abstract List<String> getMessages();
}
