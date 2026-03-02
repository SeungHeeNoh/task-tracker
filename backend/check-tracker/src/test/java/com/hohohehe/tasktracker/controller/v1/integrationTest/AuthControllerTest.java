package com.hohohehe.tasktracker.controller.v1.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.model.dto.request.JoinRequest;
import com.hohohehe.tasktracker.model.dto.request.LoginRequest;
import com.hohohehe.tasktracker.model.dto.request.ReissueRequest;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        // 테이블 초기화
        jdbcTemplate.update("DELETE FROM user_group_map");
        jdbcTemplate.update("DELETE FROM task_log");
        jdbcTemplate.update("DELETE FROM task");
        jdbcTemplate.update("DELETE FROM `groups`");
        jdbcTemplate.update("DELETE FROM users");

        // 테스트 그룹 추가
        jdbcTemplate.update(
                "INSERT INTO `groups` (group_seq, group_name, created_at, created_by, modified_at, modified_by) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                1L, "테스트그룹", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L);

        // 테스트 기본 사용자 추가 (user_seq를 1L로 고정하여 그룹 매핑 시 사용)
        String encodedPassword = passwordEncoder.encode("Test1234!"); // 대문자, 소문자, 특수문자 포함 6자 이상
        jdbcTemplate.update(
                "INSERT INTO users (user_seq, user_id, user_name, password, avatar_img, created_at, created_by, modified_at, modified_by) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                1L, "testUser", "테스트유저", encodedPassword, "default.png", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L);

        // 사용자-그룹 매핑 추가
        jdbcTemplate.update(
                "INSERT INTO user_group_map (group_seq, user_seq, created_at, created_by) "
                        + "VALUES (?, ?, ?, ?)",
                1L, 1L, LocalDateTime.now(), 1L);
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void join_Success() throws Exception {
        JoinRequest request = new JoinRequest("newUser", "신규유저", "NewUser123!", "avatar.png");

        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.message").value("회원가입에 성공했습니다.\n 로그인해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 - ID 규칙 위반 (특수문자 포함)")
    void join_Fail_InvalidId() throws Exception {
        JoinRequest request = new JoinRequest("user!", "신규유저", "NewUser123!", "avatar.png");

        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("ID는 4~50자의 영문 대소문자 및 숫자만 가능합니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 공백")
    void join_Fail_InvalidNickname() throws Exception {
        JoinRequest request = new JoinRequest("newUser", "  ", "NewUser123!", "avatar.png");

        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("닉네임은 1~50자 사이로 입력해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 규칙 위반 (특수문자 없음)")
    void join_Fail_InvalidPassword() throws Exception {
        JoinRequest request = new JoinRequest("newUser", "신규유저", "NewUser123", "avatar.png");

        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("비밀번호는 6자 이상이며, 대문자, 소문자, 특수문자를 각각 최소 1개 이상 포함해야 합니다."));
    }

    @Test
    @DisplayName("회원가입 실패 - 아바타 이미지 용량 초과")
    void join_Fail_InvalidAvatarSize() throws Exception {
        // 2MB를 초과하는 긴 문자열 생성 (약 300만 자)
        String longAvatar = "a".repeat(3000000);
        JoinRequest request = new JoinRequest("newUser", "신규유저", "NewUser123!", longAvatar);

        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("아바타 이미지는 2MB 이하여야 합니다."));
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest("testUser", "Test1234!");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.data.token.access_token").exists())
                .andExpect(jsonPath("$.data.token.refresh_token").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_InvalidPassword() throws Exception {
        LoginRequest request = new LoginRequest("testUser", "wrongPassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    void logout_Success() throws Exception {
        // 1. 먼저 로그인하여 토큰 획득
        LoginRequest loginRequest = new LoginRequest("testUser", "Test1234!");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
        Map<String, Object> token = (Map<String, Object>) data.get("token");
        String accessToken = (String) token.get("access_token");

        // 2. 획득한 토큰을 헤더에 담아 로그아웃 요청
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("X-AccessToken", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.message").value("로그아웃 되었습니다."));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 빈 리프레시 토큰")
    void reissue_Fail_EmptyToken() throws Exception {
        ReissueRequest request = new ReissueRequest("");

        mockMvc.perform(post("/api/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FA"))
                .andExpect(jsonPath("$.message").value("잘못된 접근입니다."));
    }
}
