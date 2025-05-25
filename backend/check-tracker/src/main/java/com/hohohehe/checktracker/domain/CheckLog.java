package com.hohohehe.checktracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString(callSuper = true)
@Table(uniqueConstraints = {@UniqueConstraint(name = "uq_check_list_id_date", columnNames = {"check_list_id", "check_date"})})
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

    protected CheckLog() {

    }

    private CheckLog(CheckList checkList, LocalDate checkDate) {
        this.checkList = checkList;
        this.checkDate = checkDate;
    }

    public static CheckLog of(final CheckList checkList, final LocalDate checkDate) {
        return new CheckLog(checkList, checkDate);
    }
}
