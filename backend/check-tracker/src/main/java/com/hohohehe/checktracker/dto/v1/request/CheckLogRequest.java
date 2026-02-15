package com.hohohehe.checktracker.dto.v1.request;

import java.time.LocalDate;

public record CheckLogRequest(
        Long checkListId,
        LocalDate checkDate
) {

}
