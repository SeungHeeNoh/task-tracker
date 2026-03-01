package com.hohohehe.tasktracker.common.enumCode;

public enum ResponseStatus {
    SC("Success"),
    FA("Fail");

    private final String description;

    ResponseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
