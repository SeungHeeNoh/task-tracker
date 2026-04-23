package com.hohohehe.tasktracker.model.dto.request;

public record IssueInvitationRequest(
        Long groupSeq,
        Integer maxUses
) {
}
