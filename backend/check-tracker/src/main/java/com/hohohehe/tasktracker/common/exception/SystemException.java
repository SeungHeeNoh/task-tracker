package com.hohohehe.tasktracker.common.exception;

public class SystemException extends RuntimeException {

    public SystemException(Exception e) {
        super(e);
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Exception e, String message) {
        super(message, e);
    }
}
