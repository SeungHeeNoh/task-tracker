package com.hohohehe.checktracker.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.checktracker.config.MockServiceConfig;
import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.dto.request.CheckListRequest;
import com.hohohehe.checktracker.service.CheckListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MockServiceConfig.class)
@WebMvcTest(CheckListController.class)
class CheckListControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final CheckListService checkListService;

    CheckListControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper,
            @Autowired CheckListService checkListService
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
        this.checkListService = checkListService;
    }

    @Test
    void 체크리스트_조회_API_정상작동() throws Exception {
        // given
        long checkListId = 1L;
        given(checkListService.searchCheckList()).willReturn(createCheckList(checkListId));

        // when & then
        mvc.perform(get("/checkLists"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].checkListId").value(checkListId))
                .andExpect(jsonPath("$[0].title").value("test"));
        then(checkListService).should().searchCheckList();
    }

    @Test
    void 체크리스트_생성_API_정상작동() throws Exception {
        // given
        CheckListRequest request = new CheckListRequest("공부하기");
        given(checkListService.saveCheckList(any(CheckList.class))).willReturn(any(CheckList.class));

        // when & then
        mvc.perform(post("/checkLists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value("SC"));
        then(checkListService).should().saveCheckList(any(CheckList.class));
    }

    // fixture
    private List<CheckList> createCheckList(Long checkListId) {
        CheckList checkList = CheckList.of("test");
        ReflectionTestUtils.setField(checkList, "checkListId", checkListId);
        ReflectionTestUtils.setField(checkList, "createdBy", "nsh");
        ReflectionTestUtils.setField(checkList, "createdAt", LocalDateTime.now());

        return List.of(checkList);
    }
}