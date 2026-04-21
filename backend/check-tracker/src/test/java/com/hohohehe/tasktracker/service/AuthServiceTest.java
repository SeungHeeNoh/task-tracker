package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.config.jwt.TokenProvider;
import com.hohohehe.tasktracker.model.dto.UserToken;
import com.hohohehe.tasktracker.model.dto.request.PasswordChangeRequest;
import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsersService usersService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private AuthService authService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setUserSeq(1L);
        testUser.setUserId("testUser");
        testUser.setUserName("Test User");
        testUser.setAvatarImg("default.png");
        Groups group = new Groups();
        group.setGroupSeq(1L);
        group.setGroupName("Test Group");
        testUser.setGroup(Collections.singletonList(group));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("getLoginResponse - 성공")
    void getLoginResponse_Success() {
        // given
        when(tokenProvider.generateAccessToken(any(Users.class))).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken(any(Users.class))).thenReturn("refresh-token");

        // when
        Map<String, Object> response = authService.getLoginResponse();

        // then
        assertNotNull(response);
        Map<String, Object> token = (Map<String, Object>) response.get("token");
        assertEquals("access-token", token.get("accessToken"));
        assertEquals("refresh-token", token.get("refreshToken"));

        Map<String, Object> userInfo = (Map<String, Object>) response.get("userInfo");
        assertEquals("testUser", userInfo.get("userId"));

        verify(redisService, times(1)).saveUserCache(any(Users.class), any(UserToken.class));
    }

    @Test
    @DisplayName("getNewAccessTokenResponse - 성공")
    void getNewAccessTokenResponse_Success() throws SystemException {
        // given
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        UserToken cachedToken = UserToken.of("old-access-token", refreshToken);

        when(tokenProvider.validToken(refreshToken)).thenReturn(true);
        when(tokenProvider.getUserId(refreshToken)).thenReturn("testUser");
        when(redisService.getUserTokenCache("testUser")).thenReturn(cachedToken);
        when(usersService.loadUserByUsername("testUser")).thenReturn(testUser);
        when(tokenProvider.generateAccessToken(testUser)).thenReturn(newAccessToken);

        // when
        Map<String, Object> response = authService.getNewAccessTokenResponse(refreshToken);

        // then
        assertEquals(newAccessToken, response.get("accessToken"));
        verify(redisService, times(1)).updateTokenCache(eq("testUser"), any(UserToken.class));
    }

    @Test
    @DisplayName("getNewAccessTokenResponse - 유효하지 않은 토큰")
    void getNewAccessTokenResponse_InvalidToken() {
        // given
        String invalidToken = "invalid-token";
        when(tokenProvider.validToken(invalidToken)).thenReturn(false);

        // when & then
        assertThrows(SystemException.class, () -> authService.getNewAccessTokenResponse(invalidToken));
    }

    @Test
    @DisplayName("getNewAccessTokenResponse - 캐시된 토큰과 불일치")
    void getNewAccessTokenResponse_TokenMismatch() {
        // given
        String refreshToken = "valid-but-mismatched-token";
        UserToken cachedToken = UserToken.of("access", "different-refresh");

        when(tokenProvider.validToken(refreshToken)).thenReturn(true);
        when(tokenProvider.getUserId(refreshToken)).thenReturn("testUser");
        when(redisService.getUserTokenCache("testUser")).thenReturn(cachedToken);

        // when & then
        assertThrows(SystemException.class, () -> authService.getNewAccessTokenResponse(refreshToken));
    }

    @Test
    @DisplayName("logout - 성공")
    void logout_Success() {
        // when
        authService.logout();

        // then
        verify(redisService, times(1)).clearUserCache("testUser");
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
