package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import com.hohohehe.tasktracker.common.exception.JwtAuthenticationException;
import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.config.jwt.TokenProvider;
import com.hohohehe.tasktracker.model.dto.UserToken;
import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UsersService usersService;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    public Map<String, Object> getLoginResponse() {
        Users currentUser = SecurityContext.getCurrentUser();
        String accessToken = tokenProvider.generateAccessToken(currentUser);
        String refreshToken = tokenProvider.generateRefreshToken(currentUser);

        redisService.saveUserCache(currentUser, UserToken.of(accessToken, refreshToken));

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> token = new HashMap<>();
        token.put("accessToken", accessToken);
        token.put("refreshToken", refreshToken);
        data.put("token", token);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userSeq", currentUser.getUserSeq());
        userInfo.put("userId", currentUser.getUserId());
        userInfo.put("userName", currentUser.getUsername());
        userInfo.put("avatarImg", currentUser.getAvatarImg());
        data.put("userInfo", userInfo);

        List<Map<String, Object>> groupList = new ArrayList<>();

        for(Groups group : SecurityContext.getCurrentUser().getGroup()) {
            Map<String, Object> groupInfo = new HashMap<>();
            groupInfo.put("groupSeq", group.getGroupSeq());
            groupInfo.put("groupName", group.getGroupName());
            groupList.add(groupInfo);
        }
        data.put("groupList", groupList);

        return data;
    }

    public Map<String, Object> getNewAccessTokenResponse(String refreshToken) {
        try {
            tokenProvider.validToken(refreshToken);
        } catch (JwtAuthenticationException e) {
            if (e.getErrorCode() == ErrorCode.AUTH_TOKEN_EXPIRED) {
                throw new JwtAuthenticationException(ErrorCode.AUTH_REFRESH_TOKEN_EXPIRED);
            }
            throw new JwtAuthenticationException(ErrorCode.AUTH_INVALID_REFRESH_TOKEN);
        }

        String userId = tokenProvider.getUserId(refreshToken);
        UserToken cachedToken = redisService.getUserTokenCache(userId);

        if (cachedToken == null || !cachedToken.getRefreshToken().equals(refreshToken)) {
            throw new JwtAuthenticationException(ErrorCode.AUTH_INVALID_REFRESH_TOKEN);
        }

        Users user = (Users) usersService.loadUserByUsername(userId);
        String newAccessToken = tokenProvider.generateAccessToken(user);

        cachedToken.setAccessToken(newAccessToken);
        redisService.updateTokenCache(userId, cachedToken);

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", newAccessToken);

        return data;
    }

    public void logout() {
        String userId = SecurityContext.getCurrentUser().getUserId();

        redisService.clearUserCache(userId);
        SecurityContextHolder.clearContext();

    }
}
