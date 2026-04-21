package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.config.redis.RedisProperties;
import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.dto.UserToken;
import com.hohohehe.tasktracker.model.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        
        RedisProperties.Ttl ttl = new RedisProperties.Ttl();
        ttl.setProfileTtl(600L);
        ttl.setTokenTtl(600L);
        when(redisProperties.getTtl()).thenReturn(ttl);
    }

    @Test
    @DisplayName("saveUserCache - 성공")
    void saveUserCache_Success() {
        // given
        Users user = new Users();
        user.setUserId("testUser");
        UserToken token = UserToken.of("access", "refresh");
        
        when(redisProperties.getUserProfileKey(anyString())).thenReturn("profile:testUser");
        when(redisProperties.getUserTokenKey(anyString())).thenReturn("token:testUser");
        
        // Keys for clearUserCache
        RedisProperties.Keys keys = new RedisProperties.Keys();
        keys.setUserPrefix("user:");
        when(redisProperties.getKeys()).thenReturn(keys);

        // when
        redisService.saveUserCache(user, token);

        // then
        verify(valueOperations, times(2)).set(anyString(), any(), any(Duration.class));
    }

    @Test
    @DisplayName("getUserProfileCache - 성공")
    void getUserProfileCache_Success() {
        // given
        UserProfile profile = new UserProfile();
        when(redisProperties.getUserProfileKey("testUser")).thenReturn("profile:testUser");
        when(valueOperations.get("profile:testUser")).thenReturn(profile);

        // when
        UserProfile result = redisService.getUserProfileCache("testUser");

        // then
        assertEquals(profile, result);
    }

    @Test
    @DisplayName("getUserTokenCache - 성공")
    void getUserTokenCache_Success() {
        // given
        UserToken token = UserToken.of("access", "refresh");
        when(redisProperties.getUserTokenKey("testUser")).thenReturn("token:testUser");
        when(valueOperations.get("token:testUser")).thenReturn(token);

        // when
        UserToken result = redisService.getUserTokenCache("testUser");

        // then
        assertEquals(token, result);
    }

    @Test
    @DisplayName("updateTokenCache - 성공")
    void updateTokenCache_Success() {
        // given
        UserToken token = UserToken.of("access", "refresh");
        when(redisProperties.getUserTokenKey("testUser")).thenReturn("token:testUser");

        // when
        redisService.updateTokenCache("testUser", token);

        // then
        verify(valueOperations, times(1)).set(eq("token:testUser"), eq(token), any(Duration.class));
    }
}
