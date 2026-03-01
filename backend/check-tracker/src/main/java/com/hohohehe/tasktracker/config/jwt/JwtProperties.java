package com.hohohehe.tasktracker.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

    private String issuer;
    private String secretKey;
    private long accessTokenExpiration = 900000; // 15분 기본값
    private long refreshTokenExpiration = 604800000;    // 일주일 기본값
    private List<String> excludePaths;
}
