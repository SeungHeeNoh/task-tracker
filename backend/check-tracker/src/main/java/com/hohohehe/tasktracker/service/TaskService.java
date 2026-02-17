package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.mapper.TaskLogMapper;
import com.hohohehe.tasktracker.mapper.TaskMapper;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Task;
import com.hohohehe.tasktracker.model.entity.TaskLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskLogMapper taskLogMapper;

    public CommonResponse<List<TaskInfo>> getTaskList(String viewMode) throws SystemException {
        try {
            List<Long> groupSeqs = SecurityContext.getCurrentUser().getGroup()
                    .stream()
                    .map(Groups::getGroupSeq)
                    .toList();

            return CommonResponse.success("조회에 성공했습니다.", taskMapper.getTaskList(groupSeqs, viewMode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new SystemException(e, "할 일 목록을 불러오는 중 오류가 발생했습니다.");
        }
    }

    public CommonResponse<TaskInfo> addTask(Task task) {
        try {
            taskMapper.addTask(task);
            taskLogMapper.addTaskLog(TaskLog.of(task.getTaskId(), TaskStatus.CREATED));
            return CommonResponse.success("할 일을 추가하는 데 성공했습니다.");
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new SystemException(e, "할 일을 추가하는 중 오류가 발생했습니다.");
        }
    }
}
