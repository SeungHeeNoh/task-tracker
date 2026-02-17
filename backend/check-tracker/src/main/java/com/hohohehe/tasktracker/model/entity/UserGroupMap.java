package com.hohohehe.tasktracker.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class UserGroupMap {
    private Long groupSeq;
    private Long userSeq;
    private LocalDateTime createdAt;
    private Long createdBy;
}
