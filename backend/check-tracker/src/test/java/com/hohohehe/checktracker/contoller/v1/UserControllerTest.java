package com.hohohehe.checktracker.contoller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.checktracker.config.MockServiceConfig;
import com.hohohehe.checktracker.config.SecurityConfig;
import com.hohohehe.checktracker.dto.v1.UserDto;
import com.hohohehe.checktracker.dto.v1.request.UserRequest;
import com.hohohehe.checktracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({MockServiceConfig.class, SecurityConfig.class})
@WebMvcTest(UserController.class)
class UserControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final UserService userService;

    UserControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper,
            @Autowired UserService userService
    ) {
       this.mvc = mvc;
       this.mapper = mapper;
       this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
    }

    @Test
    void 회원가입_API_성공() throws Exception {
        // given
        UserRequest userRequest = createUserRequest("test");
        willDoNothing().given(userService).saveUser(any(UserDto.class));

        // when & then
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("SC"));
        then(userService).should().saveUser(any(UserDto.class));
    }

    @Test
    void 회원가입_API_실패() throws Exception {
        // given
        UserRequest userRequest = createUserRequest("nsh");
        willThrow(new IllegalArgumentException("이미 존재하는 아이디입니다.")).given(userService).saveUser(any(UserDto.class));

        // when & then
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("FA"))
                .andExpect(jsonPath("message").value("이미 존재하는 아이디입니다."));
        then(userService).should().saveUser(any(UserDto.class));
    }

    // fixture
    private UserRequest createUserRequest(String userId) {
        return new UserRequest(userId, "1234");
    }

}