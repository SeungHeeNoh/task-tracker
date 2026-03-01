package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.config.jwt.TokenProvider;
import com.hohohehe.tasktracker.model.dto.request.JoinRequest;
import com.hohohehe.tasktracker.model.dto.request.LoginRequest;
import com.hohohehe.tasktracker.model.entity.Users;
import com.hohohehe.tasktracker.service.RedisService;
import com.hohohehe.tasktracker.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;

    @PostMapping("login")
    public CommonResponse<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.userId(), loginRequest.password());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Users currentUser = SecurityContext.getCurrentUser();
            String accessToken = tokenProvider.generateAccessToken(currentUser);
            String refreshToken = tokenProvider.generateRefreshToken(currentUser);

            redisService.saveUserCache(currentUser, SecurityContext.getCurrentUserGroupSeq());

            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", accessToken);
            data.put("refreshToken", refreshToken);
            data.put("userSeq", currentUser.getUserSeq());
            data.put("userId", currentUser.getUserId());
            data.put("userName", currentUser.getUsername());
            data.put("avatarImg", currentUser.getAvatarImg());

            return CommonResponse.success("로그인에 성공하였습니다.", data);
        } catch (BadCredentialsException e) {
            return CommonResponse.fail("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    @PostMapping("/join")
    public CommonResponse<Void> join(@RequestBody JoinRequest joinRequest) {
        try {
            usersService.join(Users.from(joinRequest));
        } catch (Exception e) {
            return CommonResponse.fail(e.getMessage());
        }

        return CommonResponse.success("회원가입에 성공했습니다.\n 로그인해주세요.");
    }
}
