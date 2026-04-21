package com.hohohehe.tasktracker.model.entity;

import com.hohohehe.tasktracker.model.dto.request.ManageTaskRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @BeforeEach
    void setUp() {
        Users testUser = new Users();
        testUser.setUserSeq(1L);
        testUser.setUserId("testUser");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("from - 신규 할 일")
    void from_NewTask() {
        ManageTaskRequest request = new ManageTaskRequest("Test Task", 1L, "2026-03-15");

        Task task = Task.from(request);

        assertEquals("Test Task", task.getTitle());
        assertEquals(LocalDate.parse("2026-03-15"), task.getDuedate());
        assertEquals(1L, task.getGroupSeq());
        assertNull(task.getTaskId());
    }

    @Test
    @DisplayName("of - 기존 할 일 수정")
    void of_ModifyTask() {
        ManageTaskRequest request = new ManageTaskRequest("Updated Task", 1L, "2026-03-15");

        Task task = Task.of(100L, request);

        assertEquals(100L, task.getTaskId());
        assertEquals("Updated Task", task.getTitle());
    }
}
