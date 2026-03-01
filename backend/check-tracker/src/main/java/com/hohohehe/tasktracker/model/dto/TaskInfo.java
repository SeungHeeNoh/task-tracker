package com.hohohehe.tasktracker.model.dto;

import com.hohohehe.tasktracker.common.enumCode.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TaskInfo {

    private Long taskId;
    private String title;
    private String duedate;
    private TaskStatus taskStatus;
    private String creator;
    private String modifier;

}
