package com.hohohehe.tasktracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.config.redis.RedisProperties;
import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    public void saveUserCache(Users users, List<Long> groups) {
        UserProfile userProfile = UserProfile.of(users, groups);
        String key = redisProperties.getUserProfileKey(userProfile.getUserId());

        redisTemplate.opsForValue().set(key, userProfile, Duration.ofSeconds(redisProperties.getProfileTtl()));
    }

    public void getUserCache(String userId) {
        redisTemplate.opsForValue().get(redisProperties.getUserProfileKey(userId));
    }
}
