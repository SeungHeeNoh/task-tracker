package com.hohohehe.tasktracker.common.exception;

import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    private final ErrorCode errorCode;

    public SystemException(Exception e) {
        super(e);
        this.errorCode = null;
    }

    public SystemException(String message) {
        super(message);
        this.errorCode = null;
    }

    public SystemException(Exception e, String message) {
        super(message, e);
        this.errorCode = null;
    }

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
