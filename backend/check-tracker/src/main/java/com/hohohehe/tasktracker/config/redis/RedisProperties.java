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
        private String invitationPrefix;
        private String invitationUsesSuffix;
    }

    @Getter @Setter
    public static class Ttl {
        private long profileTtl;
        private long tokenTtl;
        private long invitationTtl;
    }

    public String getUserProfileKey(String userId) {
        return keys.userPrefix + userId + keys.profileSuffix;
    }

    public String getUserTokenKey(String userId) {
        return keys.userPrefix + userId + keys.tokenSuffix;
    }

    public String getInvitationKey(String code) {
        return keys.invitationPrefix + code;
    }

    public String getInvitationUsesKey(String code) {
        return keys.invitationPrefix + code + keys.invitationUsesSuffix;
    }
}
