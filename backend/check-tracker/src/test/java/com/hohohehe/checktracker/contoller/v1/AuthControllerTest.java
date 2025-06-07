package com.hohohehe.checktracker.contoller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.checktracker.dto.v1.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    AuthControllerTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void 로그인_정상_토큰_반환() throws Exception {
        // given
        String userId = "nsh";
        LoginRequest loginRequest = new LoginRequest(userId, "123456");

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    void 로그인_실패_없는_유저() throws Exception {
        // given
        String userId = "test";
        LoginRequest loginRequest = new LoginRequest(userId, "123456");

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void 로그인_실패_비밀번호_틀림() throws Exception {
        // given
        String userId = "nsh";
        LoginRequest loginRequest = new LoginRequest(userId, "123");

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }
}