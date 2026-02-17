package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.dto.request.TaskListRequest;
import com.hohohehe.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public CommonResponse<List<TaskInfo>> getTasks(TaskListRequest taskListRequest) {
        try {
            return taskService.getTaskList(taskListRequest.viewMode());
        } catch (Exception e) {
            return CommonResponse.fail("데이터를 불러오는 중 오류가 발생했습니다.");
        }
    }

}
