package com.hohohehe.tasktracker.model.entity;

import com.hohohehe.tasktracker.common.enumCode.GroupRole;
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
    private GroupRole role;
    private LocalDateTime createdAt;
    private Long createdBy;
}
