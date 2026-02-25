package com.hohohehe.tasktracker.controller.v1.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.common.WithMockCustomUser;
import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import com.hohohehe.tasktracker.model.dto.request.ManageTaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long testGroupSeq = 1L;
    private Long testUserSeq = 1L;

    @BeforeEach
    void setUp() {
        // Clear tables to ensure a clean state
        jdbcTemplate.update("DELETE FROM task_log");
        jdbcTemplate.update("DELETE FROM task");
        jdbcTemplate.update("DELETE FROM user_group_map");
        jdbcTemplate.update("DELETE FROM `groups` WHERE group_seq = ?", testGroupSeq);
        jdbcTemplate.update("DELETE FROM users WHERE user_seq = ?", testUserSeq);

        // Setup base data: User, Group, and User-Group Map
        jdbcTemplate.update(
                "INSERT INTO users (user_seq, user_id, user_name, password, avatar_img, created_at, created_by, modified_at, modified_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                testUserSeq, "testUser", "테스트유저", "password", "default.png", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L);

        jdbcTemplate.update(
                "INSERT INTO `groups` (group_seq, group_name, created_at, created_by, modified_at, modified_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                testGroupSeq, "Test Group", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L);

        jdbcTemplate.update(
                "INSERT INTO user_group_map (group_seq, user_seq, created_at, created_by) " +
                        "VALUES (?, ?, ?, ?)",
                testGroupSeq, testUserSeq, LocalDateTime.now(), 1L);
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 목록 조회 통합 테스트 - 성공")
    void getTasks_IntegrationSuccess() throws Exception {
        // given: 할 일 하나 추가
        jdbcTemplate.update(
                "INSERT INTO task (title, duedate, deleted_yn, created_at, created_by, modified_at, modified_by, group_seq) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "Existing Task", "2026-12-31", "N", LocalDateTime.now(), testUserSeq, LocalDateTime.now(), testUserSeq, testGroupSeq);

        // when & then
        mockMvc.perform(get("/api/v1/tasks")
                        .param("viewMode", "WEEK")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.data[0].title").value("Existing Task"));
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 추가 통합 테스트 - 성공")
    void addTask_IntegrationSuccess() throws Exception {
        // given
        ManageTaskRequest request = new ManageTaskRequest("New Integration Task", testGroupSeq, "2026-12-31");

        // when & then
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 수정 통합 테스트 - 성공")
    void modifyTask_IntegrationSuccess() throws Exception {
        // given: 할 일 하나 추가
        jdbcTemplate.update(
                "INSERT INTO task (title, duedate, deleted_yn, created_at, created_by, modified_at, modified_by, group_seq) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "Task to Modify", "2026-12-31", "N", LocalDateTime.now(), testUserSeq, LocalDateTime.now(), testUserSeq, testGroupSeq);
        Long taskId = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);

        ManageTaskRequest request = new ManageTaskRequest("Updated Title", testGroupSeq, "2026-11-30");

        // when & then
        mockMvc.perform(post("/api/v1/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 삭제 통합 테스트 - 성공")
    void deleteTask_IntegrationSuccess() throws Exception {
        // given: 할 일 하나 추가
        jdbcTemplate.update(
                "INSERT INTO task (title, duedate, deleted_yn, created_at, created_by, modified_at, modified_by, group_seq) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "Task to Delete", "2026-12-31", "N", LocalDateTime.now(), testUserSeq, LocalDateTime.now(), testUserSeq, testGroupSeq);
        Long taskId = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);

        // when & then
        mockMvc.perform(delete("/api/v1/tasks/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 상태 변경 통합 테스트 - 성공")
    void changeStatus_IntegrationSuccess() throws Exception {
        // given: 할 일 및 초기 로그(CREATED) 추가
        jdbcTemplate.update(
                "INSERT INTO task (title, duedate, deleted_yn, created_at, created_by, modified_at, modified_by, group_seq) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "Status Task", "2026-12-31", "N", LocalDateTime.now(), testUserSeq, LocalDateTime.now(), testUserSeq, testGroupSeq);
        Long taskId = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);

        jdbcTemplate.update(
                "INSERT INTO task_log (task_id, task_status, created_at, created_by) " +
                        "VALUES (?, ?, ?, ?)",
                taskId, TaskStatus.CREATED.name(), LocalDateTime.now(), testUserSeq);

        // when & then: CREATED -> IN_PROGRESS 로 변경 기대
        mockMvc.perform(post("/api/v1/tasks/{taskId}/status", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 상세 로그 조회 통합 테스트 - 성공")
    void getTaskDetail_IntegrationSuccess() throws Exception {
        // given: 할 일 및 로그 추가
        jdbcTemplate.update(
                "INSERT INTO task (title, duedate, deleted_yn, created_at, created_by, modified_at, modified_by, group_seq) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "Detail Task", "2026-12-31", "N", LocalDateTime.now(), testUserSeq, LocalDateTime.now(), testUserSeq, testGroupSeq);
        Long taskId = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);

        jdbcTemplate.update(
                "INSERT INTO task_log (task_id, task_status, created_at, created_by) " +
                        "VALUES (?, ?, ?, ?)",
                taskId, TaskStatus.CREATED.name(), LocalDateTime.now(), testUserSeq);

        // when & then
        mockMvc.perform(post("/api/v1/tasks/{taskId}/logs", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.data.taskId").value(taskId))
                .andExpect(jsonPath("$.data.title").value("Detail Task"));
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 추가 통합 테스트 - 유효성 검사 실패 (제목 없음)")
    void addTask_ValidationFail_NoTitle() throws Exception {
        // given
        ManageTaskRequest request = new ManageTaskRequest("", testGroupSeq, "2026-12-31");

        // when & then
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("할 일의 이름을 입력해 주세요."));
    }

    @Test
    @WithMockCustomUser(userSeq = 1L)
    @DisplayName("할 일 추가 통합 테스트 - 권한 없음 실패")
    void addTask_ValidationFail_NoPermission() throws Exception {
        // given: 권한이 없는 groupSeq (999L)
        ManageTaskRequest request = new ManageTaskRequest("No Permission Task", 999L, "2026-12-31");

        // when & then
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("해당 그룹에 접근할 수 있는 권한이 없습니다."));
    }
}
