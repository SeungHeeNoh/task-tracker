package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.dto.request.ManageTaskRequest;
import com.hohohehe.tasktracker.model.dto.request.TaskListRequest;
import com.hohohehe.tasktracker.model.entity.Task;
import com.hohohehe.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

            if(!StringUtils.hasLength(taskListRequest.viewMode())) {
                throw new IllegalArgumentException("viewMode는 필수값입니다.");
            }

            return taskService.getTaskList(taskListRequest.viewMode());
        } catch (Exception e) {
            return CommonResponse.fail(e.getMessage());
        }
    }

    @PostMapping
    public CommonResponse<Void> addTask(@RequestBody ManageTaskRequest addTaskRequest) {
        try {
            addTaskRequest.checkValidation();

            return taskService.addTask(Task.from(addTaskRequest));
        } catch (Exception e) {
            return CommonResponse.fail(e.getMessage());
        }
    }

    @PostMapping("/{taskId}")
    public CommonResponse<Void> modifyTask(@PathVariable Long taskId, @RequestBody ManageTaskRequest modifyTaskRequest) {
        try {
            modifyTaskRequest.checkValidation();

            return taskService.modifyTask(Task.of(taskId, modifyTaskRequest));
        } catch (Exception e) {
            return CommonResponse.fail(e.getMessage());
        }
    }

    @DeleteMapping("/{taskId}")
    public CommonResponse<Void> deleteTask(@PathVariable Long taskId) {
        try {
            return taskService.deleteTask(Task.of(taskId));
        } catch (Exception e) {
            return CommonResponse.fail(e.getMessage());
        }
    }
}
