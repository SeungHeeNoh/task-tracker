package com.hohohehe.checktracker.dto.v1.response;

import com.hohohehe.checktracker.dto.v1.CheckListDTO;

import java.time.format.DateTimeFormatter;

public record CheckListResponse(
        Long checkListId,
        String title,
        String createdBy,
        String createdAt
) {

    public static CheckListResponse from(final CheckListDTO checkList) {
        return new CheckListResponse(
                checkList.getCheckListId(),
                checkList.getTitle(),
                checkList.getCreateUserName(),
                checkList.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }
}
