package com.hohohehe.tasktracker.common.enumCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    @Test
    @DisplayName("getNextStatus - 상태 순서 확인")
    void getNextStatus_Order() {
        assertEquals(TaskStatus.COMPLETED, TaskStatus.CREATED.getNextStatus());
        assertEquals(TaskStatus.UNCOMPLETED, TaskStatus.COMPLETED.getNextStatus());
        assertEquals(TaskStatus.COMPLETED, TaskStatus.UNCOMPLETED.getNextStatus());
    }

    @Test
    @DisplayName("getCode - 코드 반환 확인")
    void getCode_Success() {
        assertEquals("CREATED", TaskStatus.CREATED.getCode());
    }
}
