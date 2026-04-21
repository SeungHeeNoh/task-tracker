package com.hohohehe.tasktracker.model.dto.request;

import org.springframework.util.StringUtils;

public record PasswordChangeRequest(
        String prevPassword,
        String password
) {
    public void checkValidation() {
        // 1. 이전 비밀번호 검증
        if (!StringUtils.hasLength(prevPassword)) {
            throw new IllegalArgumentException("기존 비밀번호 입력은 필수입니다.");
        }

        // 2. 비밀번호 검증
        if (password == null || !password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{6,}")) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이며, 대문자, 소문자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.");
        }
    }
}
