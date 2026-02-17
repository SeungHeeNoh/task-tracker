package com.hohohehe.tasktracker.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class Users {
    private Long userSeq;
    private String userId;
    private String userName;
    private String password;
    private String avatarImg;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    private List<Groups> group = new ArrayList<>();
}
