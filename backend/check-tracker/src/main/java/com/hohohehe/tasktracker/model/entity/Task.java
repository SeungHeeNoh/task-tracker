package com.hohohehe.tasktracker.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
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
    private LocalDateTime updatedAt;
    private Long updatedBy;
}
