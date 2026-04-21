package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.enumCode.ResponseStatus;
import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.mapper.TaskLogMapper;
import com.hohohehe.tasktracker.mapper.TaskMapper;
import com.hohohehe.tasktracker.model.dto.TaskDetail;
import com.hohohehe.tasktracker.model.dto.TaskInfo;
import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Task;
import com.hohohehe.tasktracker.model.entity.TaskLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskLogMapper taskLogMapper;

    @InjectMocks
    private TaskService taskService;

    private com.hohohehe.tasktracker.model.entity.Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new com.hohohehe.tasktracker.model.entity.Users();
        testUser.setUserSeq(1L);
        testUser.setUserId("testUser");
        Groups group = new Groups();
        group.setGroupSeq(1L);
        group.setGroupName("Test Group");
        testUser.setGroup(Collections.singletonList(group));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("getTaskList - 성공")
    void getTaskList_Success() throws SystemException {
        // given
        List<TaskInfo> taskList = Collections.singletonList(new TaskInfo());
        when(taskMapper.getTaskList(anyList(), anyString())).thenReturn(taskList);

        // when
        CommonResponse<List<TaskInfo>> response = taskService.getTaskList("all");

        // then
        assertEquals(ResponseStatus.SC, response.getStatus());
        assertEquals(taskList, response.getData());
    }

    @Test
    @DisplayName("getTaskList - 예외")
    void getTaskList_Exception() {
        when(taskMapper.getTaskList(anyList(), anyString())).thenThrow(new RuntimeException("DB Error"));
        assertThrows(SystemException.class, () -> taskService.getTaskList("all"));
    }

    @Test
    @DisplayName("addTask - 성공")
    void addTask_Success() {
        // given
        Task task = new Task();
        task.setTaskId(1L);

        // when
        CommonResponse<Void> response = taskService.addTask(task);

        // then
        assertEquals(ResponseStatus.SC, response.getStatus());
        verify(taskMapper, times(1)).addTask(task);
        verify(taskLogMapper, times(1)).addTaskLog(any(TaskLog.class));
    }

    @Test
    @DisplayName("modifyTask - 성공")
    void modifyTask_Success() {
        // given
        Task task = new Task();

        // when
        CommonResponse<Void> response = taskService.modifyTask(task);

        // then
        assertEquals(ResponseStatus.SC, response.getStatus());
        verify(taskMapper, times(1)).modifyTask(task);
    }

    @Test
    @DisplayName("deleteTask - 성공")
    void deleteTask_Success() {
        // given
        Task task = new Task();
        when(taskMapper.deleteTask(eq(task), anyList())).thenReturn(1);

        // when
        CommonResponse<Void> response = taskService.deleteTask(task);

        // then
        assertEquals(ResponseStatus.SC, response.getStatus());
        verify(taskMapper, times(1)).deleteTask(eq(task), anyList());
    }

    @Test
    @DisplayName("deleteTask - 권한 없음")
    void deleteTask_NoPermission() {
        // given
        Task task = new Task();
        when(taskMapper.deleteTask(eq(task), anyList())).thenReturn(0);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(task));
    }

    @Test
    @DisplayName("changeStatus - 성공")
    void changeStatus_Success() {
        // given
        TaskInfo currentTask = new TaskInfo();
        currentTask.setTaskStatus(TaskStatus.CREATED);
        when(taskMapper.getTaskStatus(eq(1L), anyList())).thenReturn(currentTask);

        // when
        CommonResponse<Void> response = taskService.changeStatus(1L);

        // then
        assertEquals(ResponseStatus.SC, response.getStatus());
        verify(taskLogMapper, times(1)).addTaskLog(any(TaskLog.class));
    }

    @Test
    @DisplayName("getTaskDetail - 성공")
    void getTaskDetail_Success() {
        // given
        TaskDetail taskDetail = new TaskDetail();
        when(taskMapper.getTaskDetail(eq(1L), anyList())).thenReturn(taskDetail);

        // when
        CommonResponse<TaskDetail> response = taskService.getTaskDetail(1L);

        // then
        assertEquals(ResponseStatus.SC, response.getStatus());
        assertEquals(taskDetail, response.getData());
    }
}
