package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.model.dto.TaskDetail;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {
    List<TaskInfo> getTaskList(@Param("groupSeqs") List<Long> groupSeqs, @Param("viewMode") String viewMode);

    void addTask(Task task);

    void modifyTask(Task task);

    Integer deleteTask(@Param("task")Task task, @Param("groupSeqs")List<Long> groupSeqs);

    TaskInfo getTaskStatus(@Param("taskId")Long taskId, @Param("groupSeqs")List<Long> groupSeqs);

    TaskDetail getTaskDetail(@Param("taskId")Long taskId, @Param("groupSeqs")List<Long> groupSeqs);
}
