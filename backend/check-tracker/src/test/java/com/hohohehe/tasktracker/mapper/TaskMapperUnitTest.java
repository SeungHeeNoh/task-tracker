package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.config.MybatisConfig;
import com.hohohehe.tasktracker.model.dto.TaskDetail;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.entity.Task;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MybatisConfig.class)
@Sql(scripts = "classpath:db/ddl.sql")
class TaskMapperUnitTest {

    @Autowired
    private TaskMapper taskMapper;

    @Test
    @DisplayName("addTask, getTaskList, modifyTask, deleteTask, getTaskStatus, getTaskDetail - 통합 성공")
    void taskMapper_FullFlow() {
        // 1. addTask
        Task task = Task.builder()
                .title("Test Task")
                .duedate(LocalDate.now())
                .groupSeq(1L)
                .createdBy(1L)
                .modifiedBy(1L)
                .build();
        taskMapper.addTask(task);
        assertNotNull(task.getTaskId());

        // 2. getTaskList
        List<TaskInfo> taskList = taskMapper.getTaskList(List.of(1L), "all");
        assertFalse(taskList.isEmpty());
        assertEquals("Test Task", taskList.get(0).getTitle());

        // 3. modifyTask
        task.setTitle("Updated Task");
        taskMapper.modifyTask(task);
        TaskDetail detail = taskMapper.getTaskDetail(task.getTaskId(), List.of(1L));
        assertEquals("Updated Task", detail.getTitle());

        // 4. getTaskStatus
        TaskInfo status = taskMapper.getTaskStatus(task.getTaskId(), List.of(1L));
        assertNotNull(status);

        // 5. deleteTask
        int deleted = taskMapper.deleteTask(task, List.of(1L));
        assertEquals(1, deleted);
        
        TaskInfo deletedStatus = taskMapper.getTaskStatus(task.getTaskId(), List.of(1L));
        assertNull(deletedStatus);
    }
}
