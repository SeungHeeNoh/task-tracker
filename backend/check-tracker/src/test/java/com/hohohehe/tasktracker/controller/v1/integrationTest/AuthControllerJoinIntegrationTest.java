package com.hohohehe.tasktracker.controller.v1.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.model.dto.request.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerJoinIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM user_group_map");
        jdbcTemplate.update("DELETE FROM task_log");
        jdbcTemplate.update("DELETE FROM task");
        jdbcTemplate.update("DELETE FROM `groups`");
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    @DisplayName("회원가입 통합 테스트 (Auth) - 성공")
    void join_IntegrationSuccess() throws Exception {
        // given
        JoinRequest request = new JoinRequest("newUser", "신규유저", "password123", "avatar.png");

        // when & then
        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.message").value("회원가입에 성공했습니다.\n 로그인해주세요."));
    }

    @Test
    @DisplayName("회원가입 통합 테스트 (Auth) - 중복 아이디 실패")
    void join_IntegrationFail_DuplicateUserId() throws Exception {
        // given
        JoinRequest firstJoin = new JoinRequest("duplicateUser", "유저1", "password123", "avatar.png");
        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstJoin)))
                .andExpect(status().isOk());

        JoinRequest secondJoin = new JoinRequest("duplicateUser", "유저2", "password123", "avatar.png");

        // when & then
        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondJoin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다."));
    }
}
