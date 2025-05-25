package com.hohohehe.checktracker.domain;

import com.hohohehe.checktracker.dto.request.CheckListRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class CheckList extends AuditingFields {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long checkListId;

    @Setter
    @Column(nullable = false)
    private String title;

    public static CheckList of(String title) {
        CheckList checkList = new CheckList();
        checkList.setTitle(title);

        return checkList;
    }

    public static CheckList of(CheckListRequest request) {
        CheckList checkList = new CheckList();
        checkList.setTitle(request.title());

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
