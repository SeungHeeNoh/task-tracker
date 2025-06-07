package com.hohohehe.checktracker.dto.v1.response;

import com.hohohehe.checktracker.domain.User;

public record LoginResponse(
        String userId,
        String accessToken
) {

    public static LoginResponse from(User user, String accessToken) {
        return new LoginResponse(user.getUserId(), accessToken);
    }
}
