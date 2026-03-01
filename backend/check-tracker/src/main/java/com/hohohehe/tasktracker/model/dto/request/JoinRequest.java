package com.hohohehe.tasktracker.model.dto.request;

public record JoinRequest(
        String userId,
        String userName,
        String password,
        String avatarImg
) {
}
