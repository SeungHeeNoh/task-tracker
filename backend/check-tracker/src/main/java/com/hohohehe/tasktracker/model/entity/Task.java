package com.hohohehe.tasktracker.model.entity;


import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.model.dto.request.AddTaskRequest;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Task {
    private Long taskId;
    private String title;
    private LocalDate duedate;
    private String deletedYn;
    private Long groupSeq;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime modifiedAt;
    private Long modifiedBy;

    public static Task from(AddTaskRequest addTaskRequest) {
        return Task
                .builder()
                .title(addTaskRequest.title())
                .duedate(LocalDate.parse(addTaskRequest.duedate()))
                .groupSeq(addTaskRequest.groupSeq())
                .createdBy(SecurityContext.getCurrentUser().getUserSeq())
                .modifiedBy(SecurityContext.getCurrentUser().getUserSeq())
                .build();
    }
}
