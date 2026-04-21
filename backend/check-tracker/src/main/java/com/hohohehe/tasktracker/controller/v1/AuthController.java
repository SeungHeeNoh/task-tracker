package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import com.hohohehe.tasktracker.common.exception.JwtAuthenticationException;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.model.dto.request.JoinRequest;
import com.hohohehe.tasktracker.model.dto.request.LoginRequest;
import com.hohohehe.tasktracker.model.dto.request.ReissueRequest;
import com.hohohehe.tasktracker.model.entity.Users;
import com.hohohehe.tasktracker.service.AuthService;
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

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;

    @PostMapping("/login")
    public CommonResponse<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.userId(), loginRequest.password());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<String, Object> data = authService.getLoginResponse();

            return CommonResponse.success("로그인에 성공하였습니다.", data);
        } catch (BadCredentialsException e) {
            return CommonResponse.fail(ErrorCode.AUTH_LOGIN_FAILED);
        }
    }

    @PostMapping("/join")
    public CommonResponse<Void> join(@RequestBody JoinRequest joinRequest) {
        try {
            joinRequest.checkValidation();
            usersService.join(Users.from(joinRequest));
        } catch (IllegalArgumentException e) {
            return CommonResponse.fail(ErrorCode.INVALID_REQUEST, e.getMessage());
        } catch (Exception e) {
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return CommonResponse.success("회원가입에 성공했습니다.\n 로그인해주세요.");
    }

    @PostMapping("/reissue")
    public CommonResponse<Map<String, Object>> reissue(@RequestBody ReissueRequest reissueRequest) {
        try {
            reissueRequest.checkValidation();
            String refreshToken = reissueRequest.refreshToken();
            Map<String, Object> data = authService.getNewAccessTokenResponse(refreshToken);

            return CommonResponse.success("토큰 재발급에 성공하였습니다.", data);
        } catch (JwtAuthenticationException e) {
            return CommonResponse.fail(e.getErrorCode());
        } catch (IllegalArgumentException e) {
            return CommonResponse.fail(ErrorCode.INVALID_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("토큰 재발급 중 오류 발생: ", e);
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("logout")
    public CommonResponse<Void> logout() {
        try {
            authService.logout();
            return CommonResponse.success("로그아웃 되었습니다.");
        } catch (Exception e) {
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
