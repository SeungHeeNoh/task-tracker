package com.hohohehe.checktracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
