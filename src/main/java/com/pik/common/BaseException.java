package com.pik.common;

import java.util.List;


public abstract class BaseException extends RuntimeException {

    public abstract List<String> getMessages();
}
