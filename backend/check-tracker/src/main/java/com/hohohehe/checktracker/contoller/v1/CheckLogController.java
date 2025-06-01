package com.hohohehe.checktracker.contoller.v1;

import com.hohohehe.checktracker.dto.v1.request.CheckLogRequest;
import com.hohohehe.checktracker.dto.v1.response.Response;
import com.hohohehe.checktracker.service.CheckLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checklogs")
public class CheckLogController {

    private final CheckLogService checkLogService;

    @PostMapping("/confirm")
    public ResponseEntity<Response> confirmCheckLog(@RequestBody CheckLogRequest request) {
        Response response = Response.of("SC");

        try {
            checkLogService.saveCheckLog(request.checkListId(), request.checkDate());
        } catch (IllegalArgumentException e) {
            response = Response.of("FA", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/release")
    public ResponseEntity<Response> releaseCheckLog(@RequestBody CheckLogRequest request) {
        Response response = Response.of("SC");

        try {
            checkLogService.deleteCheckLog(request.checkListId(), request.checkDate());
        } catch (IllegalArgumentException e) {
            response = Response.of("FA", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
