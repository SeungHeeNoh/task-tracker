package com.hohohehe.checktracker.dto.response;

import com.hohohehe.checktracker.domain.CheckList;

import java.time.format.DateTimeFormatter;

public record CheckListResponse(
        Long checkListId,
        String title,
        String createdBy,
        String createdAt
) {

    public static CheckListResponse from(final CheckList checkList) {
        return new CheckListResponse(
                checkList.getCheckListId(),
                checkList.getTitle(),
                checkList.getCreatedBy(),
                checkList.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }
}
