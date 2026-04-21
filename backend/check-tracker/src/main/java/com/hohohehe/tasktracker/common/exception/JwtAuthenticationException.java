package com.hohohehe.tasktracker.common.exception;


import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public JwtAuthenticationException(String msg) {
        super(msg);
        this.errorCode = ErrorCode.AUTH_INVALID_TOKEN;
    }

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
