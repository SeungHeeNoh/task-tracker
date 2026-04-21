package com.hohohehe.tasktracker.common.response;

import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import com.hohohehe.tasktracker.common.enumCode.ResponseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonResponseTest {

    @Test
    @DisplayName("success - 데이터 없음")
    void success_NoData() {
        CommonResponse<Void> response = CommonResponse.success("Success");

        assertEquals(ResponseStatus.SC, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    @DisplayName("success - 데이터 포함")
    void success_WithData() {
        String data = "test data";
        CommonResponse<String> response = CommonResponse.success("Success", data);

        assertEquals(ResponseStatus.SC, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    @DisplayName("fail - 성공")
    void fail_Success() {
        CommonResponse<Void> response = CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);

        assertEquals(ResponseStatus.FA, response.getStatus());
        assertEquals("Error", response.getMessage());
        assertNull(response.getData());
    }
}
