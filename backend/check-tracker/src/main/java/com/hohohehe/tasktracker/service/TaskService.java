package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.exception.SystemException;
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

    public CommonResponse<List<TaskInfo>> getTaskList(String viewMode) throws SystemException {
        try {
            List<Long> groupSeqs = SecurityContext.getCurrentUser().getGroup()
                    .stream()
                    .map(Groups::getGroupSeq)
                    .toList();

            return CommonResponse.success("조회에 성공했습니다.", taskMapper.getTaskList(groupSeqs, viewMode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new SystemException(e, "DB 조회시 문제가 발생했습니다.");
        }
    }
}
