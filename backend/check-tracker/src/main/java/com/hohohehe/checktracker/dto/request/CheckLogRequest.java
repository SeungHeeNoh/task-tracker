package com.hohohehe.checktracker.dto.request;

import com.hohohehe.checktracker.domain.CheckLog;

import java.time.LocalDate;

public record CheckLogRequest(
        Long checkListId,
        LocalDate checkDate
) {

}
