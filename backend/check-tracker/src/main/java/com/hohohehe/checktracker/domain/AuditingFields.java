package com.hohohehe.checktracker.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public abstract class AuditingFields {

    private LocalDateTime createdAt;

    private Long createdBy;

    private LocalDateTime modifiedAt;

    private Long modifiedBy;
}
