package com.hohohehe.tasktracker.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.config.SecurityConfig;
import com.hohohehe.tasktracker.config.jwt.JWTAuthenticationFilter;
import com.hohohehe.tasktracker.model.dto.request.JoinRequest;
import com.hohohehe.tasktracker.model.dto.request.LoginRequest;
import com.hohohehe.tasktracker.model.dto.request.PasswordChangeRequest;
import com.hohohehe.tasktracker.model.dto.request.ReissueRequest;
import com.hohohehe.tasktracker.service.AuthService;
import com.hohohehe.tasktracker.service.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UsersService usersService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("회원가입 - 성공")
    void join_Success() throws Exception {
        JoinRequest request = new JoinRequest("newUser", "신규유저", "NewUser123!", "avatar.png");

        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest("testUser", "password");
        Map<String, Object> mockResponse = new HashMap<>();
        
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(mock(Authentication.class));
        given(authService.getLoginResponse()).willReturn(mockResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @DisplayName("토큰 재발급 - 성공")
    void reissue_Success() throws Exception {
        ReissueRequest request = new ReissueRequest("valid-refresh-token");
        Map<String, Object> mockResponse = new HashMap<>();
        given(authService.getNewAccessTokenResponse(any())).willReturn(mockResponse);

        mockMvc.perform(post("/api/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void changePassword_Success() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest("old", "new");

        mockMvc.perform(post("/api/v1/auth/{userId}/password", "testUser")
                        .param("oldPassword", "old")
                        .param("newPassword", "new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    void logout_Success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }
}
