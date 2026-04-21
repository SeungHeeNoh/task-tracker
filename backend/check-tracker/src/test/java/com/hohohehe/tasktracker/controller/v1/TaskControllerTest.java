package com.hohohehe.tasktracker.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.config.SecurityConfig;
import com.hohohehe.tasktracker.config.jwt.JWTAuthenticationFilter;
import com.hohohehe.tasktracker.model.dto.TaskDetail;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.dto.request.ManageTaskRequest;
import com.hohohehe.tasktracker.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import com.hohohehe.tasktracker.common.WithMockCustomUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTAuthenticationFilter.class)
})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    @WithMockCustomUser // Spring Security 테스트 유저
    @DisplayName("할 일 목록 조회 컨트롤러 - 성공")
    void getTasks_Success() throws Exception {
        // given
        String viewMode = "WEEK";
        TaskInfo mockTask = new TaskInfo();
        mockTask.setTaskId(1L);
        mockTask.setTitle("Test Task");
        mockTask.setDuedate("2026-02-17"); // string
        mockTask.setTaskStatus(com.hohohehe.tasktracker.common.enumCode.TaskStatus.CREATED);
        mockTask.setCreator("user1");
        mockTask.setModifier("user1");
        List<TaskInfo> mockTasks = List.of(mockTask);
        given(taskService.getTaskList(viewMode)).willReturn(CommonResponse.success("Success", mockTasks));

        // when & then
        mockMvc.perform(get("/api/v1/tasks")
                .param("viewMode", viewMode)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.data[0].taskId").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("할 일 추가 컨트롤러 - 성공")
    void addTask_Success() throws Exception {
        // given
        ManageTaskRequest request = new ManageTaskRequest("New Task", 1L, "2026-02-17");
        given(taskService.addTask(any())).willReturn(CommonResponse.success("Success"));

        // when & then
        mockMvc.perform(post("/api/v1/tasks")
                .with(csrf()) // POST 요청 시 CSRF 토큰 필요
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("할 일 수정 컨트롤러 - 성공")
    void modifyTask_Success() throws Exception {
        // given
        Long taskId = 1L;
        ManageTaskRequest request = new ManageTaskRequest("Updated Task", 1L, "2026-02-17");
        given(taskService.modifyTask(any())).willReturn(CommonResponse.success("Success"));

        // when & then
        mockMvc.perform(post("/api/v1/tasks/{taskId}", taskId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("할 일 삭제 컨트롤러 - 성공")
    void deleteTask_Success() throws Exception {
        // given
        Long taskId = 1L;
        given(taskService.deleteTask(any())).willReturn(CommonResponse.success("Success"));

        // when & then
        mockMvc.perform(delete("/api/v1/tasks/{taskId}", taskId)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("할 일 상태 변경 컨트롤러 - 성공")
    void changeStatus_Success() throws Exception {
        // given
        Long taskId = 1L;
        given(taskService.changeStatus(taskId)).willReturn(CommonResponse.success("Success"));

        // when & then
        mockMvc.perform(post("/api/v1/tasks/{taskId}/status", taskId)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("할 일 상세 로그 조회 컨트롤러 - 성공")
    void getTaskDetail_Success() throws Exception {
        // given
        Long taskId = 1L;
        TaskDetail mockDetail = new TaskDetail();
        mockDetail.setTaskId(1L);
        mockDetail.setTitle("Test Task");
        mockDetail.setDuedate("2026-02-17");
        mockDetail.setCreator("Test Creator");
        mockDetail.setGroupName("Test Group");
        mockDetail.setTaskLogDetailList(List.of());
        given(taskService.getTaskDetail(taskId)).willReturn(CommonResponse.success("Success", mockDetail));

        // when & then
        mockMvc.perform(post("/api/v1/tasks/{taskId}/logs", taskId)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"))
                .andExpect(jsonPath("$.data.taskId").value(1L))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }
}
