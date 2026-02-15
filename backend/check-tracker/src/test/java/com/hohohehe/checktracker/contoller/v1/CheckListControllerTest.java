package com.hohohehe.checktracker.contoller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.checktracker.config.jwt.TokenProvider;
import com.hohohehe.checktracker.domain.User;
import com.hohohehe.checktracker.dto.v1.request.CheckListRequest;
import com.hohohehe.checktracker.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class CheckListControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final UserMapper userMapper;
    private final TokenProvider tokenProvider;
    private String accessToken;

    CheckListControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper,
            @Autowired TokenProvider tokenProvider,
            @Autowired UserMapper userMapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @BeforeEach
    void setUp() throws Exception {
        Optional<User> user = userMapper.findByUserId("nsh");

        accessToken = tokenProvider.generateAccessToken(user
                .orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

    @Test
    void 체크리스트_조회_API_정상작동() throws Exception {
        // given
        long checkListId = 2L;

        // when & then
        mvc.perform(get("/api/v1/checklists")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].checkListId").value(checkListId));
    }

    @Test
    void 체크리스트_생성_API_정상작동() throws Exception {
        // given
        CheckListRequest request = new CheckListRequest("공부하기");

        // when & then
        mvc.perform(post("/api/v1/checklists")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value("SC"));
    }
}