package com.hohohehe.tasktracker.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hohohehe.tasktracker.common.enumCode.ResponseStatus;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse<T> { // 클래스 레벨에 <T> 선언

    private ResponseStatus status; // SC 또는 FA
    private String message;
    private T data;         // 제네릭 타입 T를 사용하여 다양한 객체 수용

    public static <T> CommonResponse<T> success(String message, T data) {
        return CommonResponse.<T>builder()
                .status(ResponseStatus.SC)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> success(String message) {
        return CommonResponse.<T>builder()
                .status(ResponseStatus.SC)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> fail(String message) {
        return CommonResponse.<T>builder()
                .status(ResponseStatus.FA)
                .message(message)
                .build();
    }
}