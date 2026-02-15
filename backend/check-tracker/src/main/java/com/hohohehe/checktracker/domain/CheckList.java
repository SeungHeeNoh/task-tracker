package com.hohohehe.checktracker.domain;

import com.hohohehe.checktracker.common.SecurityContext;
import com.hohohehe.checktracker.dto.v1.request.CheckListRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@ToString(callSuper = true)
public class CheckList extends AuditingFields {

    private Long checkListId;
    private String title;
    private List<CheckLog> checkLogs = new ArrayList<>();

    public static CheckList of(String title) {
        CheckList checkList = new CheckList();
        checkList.setTitle(title);

        return checkList;
    }

    public static CheckList ofCreateRequest(CheckListRequest request) {
        CheckList checkList = new CheckList();
        User currentUser = SecurityContext.getCurrentUser();

        checkList.setTitle(request.title());
        checkList.setCreatedBy(currentUser.getUserSeq());

        return checkList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckList checkList)) return false;
        return Objects.equals(checkListId, checkList.checkListId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(checkListId);
    }
}
