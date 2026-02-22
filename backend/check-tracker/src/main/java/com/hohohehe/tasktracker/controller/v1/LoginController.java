package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.config.jwt.TokenProvider;
import com.hohohehe.tasktracker.model.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("login")
    public CommonResponse<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.userId(), loginRequest.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(SecurityContext.getCurrentUser());
        Map<String, String> data = Map.of("accessToken", accessToken);

        return CommonResponse.success("로그인에 성공하였습니다.", data);
    }
}
