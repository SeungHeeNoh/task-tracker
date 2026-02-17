package com.hohohehe.tasktracker.model.dto.request;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.model.entity.Groups;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public record ManageTaskRequest(
        String title,
        Long groupSeq,
        String duedate
) {
    public void checkValidation() {
        // 필수 값 검증
        if(!StringUtils.hasLength(title)) {
            throw new IllegalArgumentException("할 일의 이름을 입력해 주세요.");
        }

        if(title.length() > 50) {
            throw new IllegalArgumentException("이름이 너무 길어요. 50자 이내로 작성해 주세요.");
        }

        if(groupSeq == null) {
            throw new IllegalArgumentException("어느 그룹의 할 일인지 선택해 주세요.");
        }

        if(duedate == null) {
            throw new IllegalArgumentException("마감 기한을 설정해 주세요.");
        }

        // 형식 검증
        try {
            LocalDate.parse(duedate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. (예: 2026-02-17)");
        }

        // 비즈니스 권한 검증
        SecurityContext.getCurrentUser().getGroup()
                .stream()
                .map(Groups::getGroupSeq)
                .filter(seq -> Objects.equals(seq, groupSeq))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹에 접근할 수 있는 권한이 없습니다."));

    }
}
