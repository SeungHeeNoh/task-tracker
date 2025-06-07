package com.hohohehe.checktracker.contoller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.checktracker.dto.v1.UserDto;
import com.hohohehe.checktracker.dto.v1.request.UserRequest;
import com.hohohehe.checktracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    UserControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ) {
       this.mvc = mvc;
       this.mapper = mapper;
    }

    @Test
    void 회원가입_API_성공() throws Exception {
        // given
        UserRequest userRequest = createUserRequest("test");

        // when & then
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("SC"));
    }

    @Test
    void 회원가입_API_실패() throws Exception {
        // given
        UserRequest userRequest = createUserRequest("nsh");

        // when & then
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("FA"))
                .andExpect(jsonPath("message").value("이미 존재하는 아이디입니다."));
    }

    // fixture
    private UserRequest createUserRequest(String userId) {
        return new UserRequest(userId, "1234");
    }

}