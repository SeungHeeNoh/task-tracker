package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.model.entity.TaskLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskLogMapper {
    void addTaskLog(TaskLog taskLog);
}
