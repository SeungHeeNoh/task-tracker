package com.hohohehe.tasktracker.model.dto.request;

import org.springframework.util.StringUtils;

public record ReissueRequest(
        String refreshToken
) {

    public void checkValidation() {
        // 필수 값 검증
        if(!StringUtils.hasLength(refreshToken)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

    }
}
