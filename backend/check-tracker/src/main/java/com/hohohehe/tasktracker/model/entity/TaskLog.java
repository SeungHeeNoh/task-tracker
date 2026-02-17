package com.hohohehe.tasktracker.model.entity;

import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class TaskLog {
    private Long taskLogId;
    private Long taskId;
    private TaskStatus taskStatus;
    private LocalDateTime createdAt;
    private Long createdBy;
}
