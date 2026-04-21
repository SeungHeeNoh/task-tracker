package com.hohohehe.tasktracker.config.jwt;

import com.hohohehe.tasktracker.common.exception.JwtAuthenticationException;
import com.hohohehe.tasktracker.model.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    private TokenProvider tokenProvider;

    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setIssuer("testIssuer");
        jwtProperties.setSecretKey("testSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKeytestSecretKey");
        jwtProperties.setAccessTokenExpiration(1000 * 60 * 15);
        jwtProperties.setRefreshTokenExpiration(1000 * 60 * 60 * 24);

        tokenProvider = new TokenProvider(jwtProperties);
    }

    @Test
    @DisplayName("generateAccessToken - 성공")
    void generateAccessToken_Success() {
        // given
        Users user = new Users();
        user.setUserId("testUser");

        // when
        String token = tokenProvider.generateAccessToken(user);

        // then
        assertNotNull(token);
        assertTrue(tokenProvider.validToken(token));
        assertEquals("testUser", tokenProvider.getUserId(token));
    }

    @Test
    @DisplayName("validToken - 유효하지 않은 토큰")
    void validToken_Invalid() {
        // given
        String invalidToken = "invalidToken";

        // when & then
        assertThrows(JwtAuthenticationException.class, () -> tokenProvider.validToken(invalidToken));
    }

    @Test
    @DisplayName("validToken - 만료된 토큰")
    void validToken_Expired() {
        // given
        jwtProperties.setAccessTokenExpiration(-1000); // 1초 전에 만료됨
        TokenProvider expiredProvider = new TokenProvider(jwtProperties);
        Users user = new Users();
        user.setUserId("testUser");
        String token = expiredProvider.generateAccessToken(user);

        // when & then
        assertThrows(JwtAuthenticationException.class, () -> tokenProvider.validToken(token));
    }
}
