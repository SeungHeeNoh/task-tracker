package com.hohohehe.tasktracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.config.redis.RedisProperties;
import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.dto.UserToken;
import com.hohohehe.tasktracker.model.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    public void saveUserCache(Users users, List<Long> groups, UserToken userToken) {
        UserProfile userProfile = UserProfile.of(users, groups);
        String profileKey = redisProperties.getUserProfileKey(userProfile.getUserId());
        String tokenKey = redisProperties.getUserTokenKey(userProfile.getUserId());

        clearUserCache(userProfile.getUserId());

        redisTemplate.opsForValue().set(profileKey, userProfile, Duration.ofSeconds(redisProperties.getTtl().getProfileTtl()));
        redisTemplate.opsForValue().set(tokenKey, userToken, Duration.ofSeconds(redisProperties.getTtl().getTokenTtl()));
    }

    public UserCache getUserProfileCache(String userId) {
        return (UserCache) redisTemplate.opsForValue().get(redisProperties.getUserProfileKey(userId));
    }

    public UserToken getUserTokenCache(String userId) {
        return (UserToken) redisTemplate.opsForValue().get(redisProperties.getUserTokenKey(userId));
    }

    public void updateTokenCache(String userId, UserToken userToken) {
        String tokenKey = redisProperties.getUserTokenKey(userId);
        redisTemplate.opsForValue().set(tokenKey, userToken, Duration.ofSeconds(redisProperties.getTtl().getTokenTtl()));
    }

    public void clearUserCache(String userId) {
        String pattern = redisProperties.getKeys().getUserPrefix() + userId + ":*";

        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Cleared all Redis data for user {}: {} keys removed", userId, keys.size());
        }
    }
}
