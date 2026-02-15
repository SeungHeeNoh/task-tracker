package com.hohohehe.checktracker.contoller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.checktracker.config.jwt.TokenProvider;
import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.domain.CheckLog;
import com.hohohehe.checktracker.domain.User;
import com.hohohehe.checktracker.dto.v1.request.CheckLogRequest;
import com.hohohehe.checktracker.mapper.CheckLogMapper;
import com.hohohehe.checktracker.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class CheckLogControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final UserMapper userMapper;
    private final CheckLogMapper checkLogMapper;
    private final TokenProvider tokenProvider;
    private String accessToken;

    private static final Long ALREADY_CHECKED_ID = 3L;

    CheckLogControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper,
            @Autowired TokenProvider tokenProvider,
            @Autowired UserMapper userMapper,
            @Autowired CheckLogMapper checkLogMapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
        this.checkLogMapper = checkLogMapper;
    }

    @BeforeEach
    void setUp() {
        Optional<User> user = userMapper.findByUserId("nsh");
        LocalDate checkDate = LocalDate.now(ZoneId.of("Asia/Seoul"));

        accessToken = tokenProvider.generateAccessToken(user
                .orElseThrow(() -> new RuntimeException("User not found"))
        );

        checkLogMapper.save(CheckLog.of(createCheckList(ALREADY_CHECKED_ID), checkDate));
    }

    @Test
    void 체크리스트_체크_API_정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createCheckLogRequest();

        // when & then
        mvc.perform(post("/api/v1/checklogs/confirm")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("SC"));
    }

    @Test
    void 체크리스트_체크_API_비정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createAlreadyCheckLogRequest();

        // when & then
        mvc.perform(post("/api/v1/checklogs/confirm")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("FA"))
                .andExpect(jsonPath("message").value("이미 체크된 항목입니다."));
    }

    @Test
    void 체크리스트_삭제_API_정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createAlreadyCheckLogRequest();

        // when & then
        mvc.perform(post("/api/v1/checklogs/release")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("SC"));
    }

    @Test
    void 체크리스트_삭제_API_비정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createCheckLogRequest();

        // when & then
        mvc.perform(post("/api/v1/checklogs/release")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("FA"))
                .andExpect(jsonPath("message").value("체크된 적 없는 항목입니다."));
    }

    // fixture
    private CheckList createCheckList(Long checkListId) {
        CheckList checkList = new CheckList();
        ReflectionTestUtils.setField(checkList, "checkListId", checkListId);

        return checkList;
    }

    private CheckLogRequest createCheckLogRequest() {
        return new CheckLogRequest(2L, LocalDate.now());
    }

    private CheckLogRequest createAlreadyCheckLogRequest() {
        return new CheckLogRequest(ALREADY_CHECKED_ID, LocalDate.now());
    }
}