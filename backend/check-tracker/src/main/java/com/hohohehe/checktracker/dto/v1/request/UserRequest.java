package com.hohohehe.checktracker.dto.v1.request;

import com.hohohehe.checktracker.dto.v1.UserDto;

public record UserRequest(
        String userId,
        String password) {


    public UserDto toDto() {
        return UserDto.builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
