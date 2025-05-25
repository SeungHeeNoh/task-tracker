package com.hohohehe.checktracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString(callSuper = true)
@Table
@Entity
public class CheckLog extends AuditingFields {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long checkLogId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_list_id", nullable = false)
    @ToString.Exclude
    private CheckList checkList;

    @Setter
    @Column
    private LocalDate checkDate;

}
