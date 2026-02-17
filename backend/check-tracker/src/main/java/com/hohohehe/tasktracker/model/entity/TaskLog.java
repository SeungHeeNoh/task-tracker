package com.hohohehe.tasktracker.model.entity;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TaskLog {
    private Long taskLogId;
    private Long taskId;
    private TaskStatus taskStatus;
    private LocalDateTime createdAt;
    private Long createdBy;

    public static TaskLog of(Long taskId, TaskStatus taskStatus) {
        return TaskLog.builder()
                .taskId(taskId)
                .taskStatus(taskStatus)
                .createdBy(SecurityContext.getCurrentUser().getUserSeq())
                .build();
    }
}
