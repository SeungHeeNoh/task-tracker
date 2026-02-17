package com.hohohehe.tasktracker.common.exception;

public class SystemException extends RuntimeException {

    private Exception e;
    private String message;

    public SystemException(Exception e) {
        this.e = e;
    }

    public SystemException(Exception e, String message) {
        this.e = e;
        this.message = message;
    }
}
