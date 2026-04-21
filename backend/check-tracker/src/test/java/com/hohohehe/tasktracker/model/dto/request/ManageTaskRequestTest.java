package com.hohohehe.tasktracker.model.dto.request;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageTaskRequestTest {

    private MockedStatic<SecurityContext> mockedSecurityContext;

    @BeforeEach
    void setUp() {
        Users testUser = new Users();
        testUser.setUserSeq(1L);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockedSecurityContext = mockStatic(SecurityContext.class);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContext.close();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("checkValidation - 성공")
    void checkValidation_Success() {
        ManageTaskRequest request = new ManageTaskRequest("Valid Title", 1L, "2026-03-15");
        when(SecurityContext.getCurrentUserGroupSeq()).thenReturn(List.of(1L));

        assertDoesNotThrow(request::checkValidation);
    }

    @Test
    @DisplayName("checkValidation - 제목 누락")
    void checkValidation_NoTitle() {
        ManageTaskRequest request = new ManageTaskRequest("", 1L, "2026-03-15");
        assertThrows(IllegalArgumentException.class, request::checkValidation);
    }

    @Test
    @DisplayName("checkValidation - 그룹 누락")
    void checkValidation_NoGroup() {
        ManageTaskRequest request = new ManageTaskRequest("Title", null, "2026-03-15");
        assertThrows(IllegalArgumentException.class, request::checkValidation);
    }

    @Test
    @DisplayName("checkValidation - 날짜 형식 오류")
    void checkValidation_InvalidDate() {
        ManageTaskRequest request = new ManageTaskRequest("Title", 1L, "invalid-date");
        assertThrows(IllegalArgumentException.class, request::checkValidation);
    }

    @Test
    @DisplayName("checkValidation - 권한 없는 그룹")
    void checkValidation_NoPermission() {
        ManageTaskRequest request = new ManageTaskRequest("Title", 2L, "2026-03-15");
        when(SecurityContext.getCurrentUserGroupSeq()).thenReturn(List.of(1L));

        assertThrows(IllegalArgumentException.class, request::checkValidation);
    }
}
