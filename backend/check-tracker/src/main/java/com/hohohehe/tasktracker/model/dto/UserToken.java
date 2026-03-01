package com.hohohehe.tasktracker.model.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;

    public static UserToken of(String accessToken, String refreshToken) {
        return UserToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
