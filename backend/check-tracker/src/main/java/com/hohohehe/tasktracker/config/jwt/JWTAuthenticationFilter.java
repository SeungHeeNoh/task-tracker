package com.hohohehe.tasktracker.config.jwt;

import com.hohohehe.tasktracker.common.exception.JwtAuthenticationException;
import com.hohohehe.tasktracker.config.redis.RedisProperties;
import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.entity.Users;
import com.hohohehe.tasktracker.service.RedisService;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final TokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final RedisProperties redisProperties;

    private final static String HEADER_AUTHORIZATION = "X-AccessToken";
    private final static String TOKEN_PREFIX = "Bearer ";
    private final RedisService redisService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 설정된 리스트 중 하나라도 현재 경로와 일치하면 필터를 태우지 않음 (true 반환)
        return jwtProperties.getExcludePaths().stream()
                .anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = extractToken(authorizationHeader);

        if(token != null && tokenProvider.validToken(token)) {
            try {
                String userId = tokenProvider.getUserId(token);

                if(StringUtils.isNotEmpty(userId) && SecurityContextHolder.getContext().getAuthentication() == null ) {
                    // redis에서 유저 세션 확인
                    UserProfile userProfile = redisService.getUserProfileCache(userId);

                    // redis에 유저 세션 없는 경우
                    if(userProfile == null) {
                        log.warn("Unauthorized access attempt or expired session for user: {}", userId);
                        throw new JwtAuthenticationException("세션이 만료되었거나 로그아웃된 계정입니다.");
                    }

                    // redis cache 객체를 Users 객체로 변환 후 security context에 세팅
                    Users userDetails = Users.from(userProfile);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch ( JwtException | IllegalArgumentException e ) {
                log.warn("JWT token validation failed: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}

