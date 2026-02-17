package com.hohohehe.tasktracker.common.enumCode;

public enum TaskStatus {

    CREATED("CREATED"),
    COMPLETED("COMPLETED"),
    UNCOMPLETED("UNCOMPLETED");

    private final String code;

    TaskStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public TaskStatus getNextStatus() {
        return switch (this) {
            case CREATED, UNCOMPLETED -> COMPLETED;
            case COMPLETED -> UNCOMPLETED;
        };
    }
}
