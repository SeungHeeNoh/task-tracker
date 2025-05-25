package com.hohohehe.checktracker.dto.response;

import lombok.*;

public record Response(
        String result,
        String message
) {

    public static Response of(String result, String message) {
        return new Response(result, message);
    }

    public static Response of(String result) {
        return of(result, "");
    }
}
