package com.hohohehe.tasktracker.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("redis")
public class RedisProperties {

    private Keys keys;
    private Ttl ttl;

    @Getter @Setter
    public static class Keys {
        private String userPrefix;
        private String profileSuffix;
        private String tokenSuffix;
    }

    @Getter @Setter
    public static class Ttl {
        private long profileTtl;
        private Long tokenTtl;
    }

    public String getUserProfileKey(String userId) {
        return keys.userPrefix + userId + keys.profileSuffix;
    }

    public String getUserTokenKey(String userId) {
        return keys.userPrefix + userId + keys.tokenSuffix;
    }
}
