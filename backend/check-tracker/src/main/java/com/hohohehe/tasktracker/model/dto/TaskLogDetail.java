package com.hohohehe.tasktracker.model.dto;

import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TaskLogDetail {
    private Long id;
    private TaskStatus status;
    private String createdAt;
    private String creator;
}
