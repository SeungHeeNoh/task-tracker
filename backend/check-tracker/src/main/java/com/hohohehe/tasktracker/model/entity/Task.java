package com.hohohehe.tasktracker.model.entity;


import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.model.dto.request.ManageTaskRequest;
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

    public static Task from(ManageTaskRequest addTaskRequest) {
        return Task
                .builder()
                .title(addTaskRequest.title())
                .duedate(LocalDate.parse(addTaskRequest.duedate()))
                .groupSeq(addTaskRequest.groupSeq())
                .createdBy(SecurityContext.getCurrentUser().getUserSeq())
                .modifiedBy(SecurityContext.getCurrentUser().getUserSeq())
                .build();
    }

    public static Task of(Long taskId, ManageTaskRequest addTaskRequest) {
        return Task
                .builder()
                .taskId(taskId)
                .title(addTaskRequest.title())
                .duedate(LocalDate.parse(addTaskRequest.duedate()))
                .groupSeq(addTaskRequest.groupSeq())
                .createdBy(SecurityContext.getCurrentUser().getUserSeq())
                .modifiedBy(SecurityContext.getCurrentUser().getUserSeq())
                .build();
    }

    public static Task of(Long taskId) {
        return Task
                .builder()
                .taskId(taskId)
                .modifiedBy(SecurityContext.getCurrentUser().getUserSeq())
                .build();
    }
}
