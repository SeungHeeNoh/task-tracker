package com.hohohehe.checktracker.contoller.v1;

import com.hohohehe.checktracker.dto.v1.request.UserRequest;
import com.hohohehe.checktracker.dto.v1.response.Response;
import com.hohohehe.checktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody UserRequest userRequest) {
        Response response;

        try {
            userService.saveUser(userRequest.toDto());
            response = Response.of("SC");
        } catch (IllegalArgumentException e) {
            response = Response.of("FA", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
