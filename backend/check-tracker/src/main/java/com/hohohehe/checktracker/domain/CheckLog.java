package com.hohohehe.checktracker.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString(callSuper = true)
public class CheckLog extends AuditingFields {

    private long checkLogId;
    private CheckList checkList;
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
