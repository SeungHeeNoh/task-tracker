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
public class InvitationPreview {

    private Long groupSeq;
    private String groupName;
    private int memberCount;
}
