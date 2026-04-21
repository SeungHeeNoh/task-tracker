package com.hohohehe.tasktracker.model.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinRequestTest {

    @Test
    @DisplayName("checkValidation - 성공")
    void checkValidation_Success() {
        JoinRequest request = new JoinRequest("testUser", "테스트", "Password123!", "avatar.png");
        assertDoesNotThrow(request::checkValidation);
    }

    @Test
    @DisplayName("checkValidation - 아이디 누락")
    void checkValidation_NoId() {
        JoinRequest request = new JoinRequest("", "테스트", "Password123!", "avatar.png");
        assertThrows(IllegalArgumentException.class, request::checkValidation);
    }

    @Test
    @DisplayName("checkValidation - 비밀번호 형식 오류")
    void checkValidation_InvalidPassword() {
        JoinRequest request = new JoinRequest("testUser", "테스트", "12345", "avatar.png");
        assertThrows(IllegalArgumentException.class, request::checkValidation);
    }
}
