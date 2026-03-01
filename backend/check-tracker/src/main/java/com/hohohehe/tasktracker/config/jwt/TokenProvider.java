package com.hohohehe.tasktracker.config.jwt;

import com.hohohehe.tasktracker.common.exception.JwtAuthenticationException;
import com.hohohehe.tasktracker.model.entity.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private final Key key;

    public TokenProvider(@Autowired JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Users user) {
        return generateToken(user.getUserId(), jwtProperties.getAccessTokenExpiration());
    }

    public String generateRefreshToken(Users user) {
        return generateToken(user.getUserId(), jwtProperties.getRefreshTokenExpiration());
    }

    private String generateToken(String userId, long expirationMillis) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validToken(String token) {
        try {
            getClaims(token);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty.");
        }

        return false;
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .requireIssuer(jwtProperties.getIssuer())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtAuthenticationException("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT claims string is empty.");
        } catch (IncorrectClaimException e) {
            throw new JwtAuthenticationException("JWT claims is not correct.");
        }
    }
}
