package com.hohohehe.checktracker.contoller.v1;

import com.hohohehe.checktracker.config.jwt.TokenProvider;
import com.hohohehe.checktracker.domain.User;
import com.hohohehe.checktracker.dto.v1.request.LoginRequest;
import com.hohohehe.checktracker.dto.v1.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.userId(), loginRequest.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = tokenProvider.generateAccessToken(user);

        return ResponseEntity.ok(LoginResponse.from(user, accessToken));
    }
}
