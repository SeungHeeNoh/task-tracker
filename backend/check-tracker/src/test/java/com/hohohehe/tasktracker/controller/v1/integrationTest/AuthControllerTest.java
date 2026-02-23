package com.hohohehe.tasktracker.controller.v1.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.model.dto.request.LoginRequest;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

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
        // 테스트 실행 전 실제 로그인 검증을 위한 사용자를 DB에 추가합니다.
        String encodedPassword = passwordEncoder.encode("testPassword");

        jdbcTemplate.update(
                "INSERT INTO users (user_id, user_name, password, avatar_img, created_at, created_by, modified_at, modified_by) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "testUser", "테스트유저", encodedPassword, "default.png", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L);
    }

    @Test
    @DisplayName("DB 연동 로그인 컨트롤러 통합 테스트 - 성공하면 토큰 발급")
    void login_IntegrationSuccess() throws Exception {
        // given
        LoginRequest request = new LoginRequest("testUser", "testPassword");

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                // 응답에서 메시지나 데이터 필드 하위에 발급된 JWT 토큰 값이 존재하는지 체크
                .andExpect(jsonPath("$.data.accessToken").exists());
    }
}
