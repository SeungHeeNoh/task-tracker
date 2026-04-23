package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.config.redis.RedisProperties;
import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.dto.UserToken;
import com.hohohehe.tasktracker.model.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;

    public void saveUserCache(Users users, UserToken userToken) {
        UserProfile userProfile = UserProfile.of(users);
        String profileKey = redisProperties.getUserProfileKey(userProfile.getUserId());
        String tokenKey = redisProperties.getUserTokenKey(userProfile.getUserId());

        clearUserCache(userProfile.getUserId());

        redisTemplate.opsForValue().set(profileKey, userProfile, Duration.ofSeconds(redisProperties.getTtl().getProfileTtl()));
        redisTemplate.opsForValue().set(tokenKey, userToken, Duration.ofSeconds(redisProperties.getTtl().getTokenTtl()));
    }

    public UserProfile getUserProfileCache(String userId) {
        return (UserProfile) redisTemplate.opsForValue().get(redisProperties.getUserProfileKey(userId));
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

    public void saveInvitationCode(String code, Long groupSeq, Long createdBy, int maxUses) {
        String metaKey = redisProperties.getInvitationKey(code);
        String usesKey = redisProperties.getInvitationUsesKey(code);
        long ttl = redisProperties.getTtl().getInvitationTtl();

        Map<String, Object> payload = new HashMap<>();
        payload.put("groupSeq", groupSeq);
        payload.put("createdBy", createdBy);
        payload.put("maxUses", maxUses);

        redisTemplate.opsForValue().set(metaKey, payload, Duration.ofSeconds(ttl));
        redisTemplate.opsForValue().set(usesKey, maxUses, Duration.ofSeconds(ttl));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findInvitationCode(String code) {
        String metaKey = redisProperties.getInvitationKey(code);
        Object payload = redisTemplate.opsForValue().get(metaKey);
        return (Map<String, Object>) payload;
    }

    public Long decrementInvitationUses(String code) {
        String usesKey = redisProperties.getInvitationUsesKey(code);
        return redisTemplate.opsForValue().increment(usesKey, -1);
    }

    public void deleteInvitationCode(String code) {
        String metaKey = redisProperties.getInvitationKey(code);
        String usesKey = redisProperties.getInvitationUsesKey(code);
        redisTemplate.delete(metaKey);
        redisTemplate.delete(usesKey);
    }
}
