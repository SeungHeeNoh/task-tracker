package com.hohohehe.tasktracker.common.enumCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Auth
    AUTH_TOKEN_EXPIRED("액세스 토큰이 만료되었습니다."),
    AUTH_INVALID_TOKEN("유효하지 않은 토큰입니다."),
    AUTH_SESSION_EXPIRED("세션이 만료되었습니다."),
    AUTH_LOGIN_FAILED("아이디 또는 비밀번호가 일치하지 않습니다."),
    AUTH_REFRESH_TOKEN_EXPIRED("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
    AUTH_INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다."),

    // User
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    USER_PASSWORD_MISMATCH("현재 비밀번호가 일치하지 않습니다."),
    USER_ACCESS_DENIED("잘못된 접근입니다."),

    // Task
    TASK_NOT_FOUND("존재하지 않는 태스크입니다."),
    TASK_ACCESS_DENIED("접근 권한이 없습니다."),

    // Common
    INVALID_REQUEST("잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
