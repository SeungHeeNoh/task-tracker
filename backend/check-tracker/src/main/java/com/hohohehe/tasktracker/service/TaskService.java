package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.mapper.TaskMapper;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.entity.Groups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public CommonResponse<List<TaskInfo>> getTaskList(String viewMode) {
        List<Long> groupSeqs = SecurityContext.getCurrentUser().getGroup()
                .stream()
                .map(Groups::getGroupSeq)
                .toList();

        return taskMapper.getTaskList(groupSeqs, viewMode);
    }
}
