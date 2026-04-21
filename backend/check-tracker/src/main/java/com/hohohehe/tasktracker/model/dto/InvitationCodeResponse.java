package com.hohohehe.tasktracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationCodeResponse {

    private String code;
    private int maxUses;
    private long expiresInSeconds;
}
