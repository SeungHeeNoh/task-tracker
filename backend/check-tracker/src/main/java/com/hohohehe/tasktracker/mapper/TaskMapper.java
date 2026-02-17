package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.model.dto.TaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {
    List<TaskInfo> getTaskList(@Param("groupSeqs") List<Long> groupSeqs, @Param("viewMode") String viewMode);
}
