package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.config.MybatisConfig;
import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import com.hohohehe.tasktracker.model.entity.Task;
import com.hohohehe.tasktracker.model.entity.TaskLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MybatisConfig.class)
@Sql(scripts = "classpath:db/ddl.sql")
class TaskLogMapperUnitTest {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskLogMapper taskLogMapper;

    @Test
    @DisplayName("addTaskLog - 성공")
    void addTaskLog_Success() {
        // 1. Task 생성
        Task task = Task.builder()
                .title("Log Test Task")
                .duedate(LocalDate.now())
                .groupSeq(1L)
                .createdBy(1L)
                .modifiedBy(1L)
                .build();
        taskMapper.addTask(task);

        // 2. TaskLog 추가
        TaskLog log = TaskLog.builder()
                .taskId(task.getTaskId())
                .taskStatus(TaskStatus.CREATED)
                .createdBy(1L)
                .build();
        taskLogMapper.addTaskLog(log);

        assertNotNull(log.getTaskLogId());
    }
}
