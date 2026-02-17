package com.hohohehe.tasktracker.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class TaskDetail {

    private Long taskId;
    private String title;
    private String duedate;
    private String creator;
    private String modifier;
    private String groupName;
    List<TaskLogDetail> taskLogDetailList;
}
