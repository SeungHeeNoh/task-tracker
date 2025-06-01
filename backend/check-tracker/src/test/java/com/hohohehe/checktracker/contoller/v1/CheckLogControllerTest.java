package com.hohohehe.checktracker.contoller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.checktracker.config.MockServiceConfig;
import com.hohohehe.checktracker.dto.v1.request.CheckLogRequest;
import com.hohohehe.checktracker.service.CheckLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MockServiceConfig.class)
@WebMvcTest(CheckLogController.class)
class CheckLogControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final CheckLogService checkLogService;

    CheckLogControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper,
            @Autowired CheckLogService checkLogService
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
        this.checkLogService = checkLogService;
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(checkLogService);
    }

    @Test
    void 체크리스트_체크_API_정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createCheckLogRequest();
        willDoNothing().given(checkLogService).saveCheckLog(any(Long.class), any(LocalDate.class));

        // when & then
        mvc.perform(post("/api/v1/checklogs/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("SC"));
        then(checkLogService).should().saveCheckLog(any(Long.class), any(LocalDate.class));
    }

    @Test
    void 체크리스트_체크_API_비정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createCheckLogRequest();
        willThrow(new IllegalArgumentException("이미 체크된 항목입니다."))
                .given(checkLogService).saveCheckLog(any(Long.class), any(LocalDate.class));

        // when & then
        mvc.perform(post("/api/v1/checklogs/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("FA"))
                .andExpect(jsonPath("message").value("이미 체크된 항목입니다."));
        then(checkLogService).should().saveCheckLog(any(Long.class), any(LocalDate.class));
    }

    @Test
    void 체크리스트_삭제_API_정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createCheckLogRequest();
        willDoNothing().given(checkLogService).deleteCheckLog(any(Long.class), any(LocalDate.class));

        // when & then
        mvc.perform(post("/api/v1/checklogs/release")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("SC"));
        then(checkLogService).should().deleteCheckLog(any(Long.class), any(LocalDate.class));
    }

    @Test
    void 체크리스트_삭제_API_비정상작동() throws Exception {
        // given
        CheckLogRequest checkLogRequest = createCheckLogRequest();
        willThrow(new IllegalArgumentException("체크된 적 없는 항목입니다."))
                .given(checkLogService).deleteCheckLog(any(Long.class), any(LocalDate.class));

        // when & then
        mvc.perform(post("/api/v1/checklogs/release")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkLogRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result").value("FA"))
                .andExpect(jsonPath("message").value("체크된 적 없는 항목입니다."));
        then(checkLogService).should().deleteCheckLog(any(Long.class), any(LocalDate.class));
    }

    // fixture
    private CheckLogRequest createCheckLogRequest() {
        return new CheckLogRequest(1L, LocalDate.now());
    }
}