package com.hohohehe.tasktracker.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import com.hohohehe.tasktracker.common.exception.JwtAuthenticationException;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.entity.Users;
import com.hohohehe.tasktracker.service.RedisService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final TokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

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

        try {
            if (token != null && tokenProvider.validToken(token)) {
                String userId = tokenProvider.getUserId(token);

                if (StringUtils.isNotEmpty(userId) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // redis에서 유저 세션 확인
                    UserProfile userProfile = redisService.getUserProfileCache(userId);

                    // redis에 유저 세션 없는 경우
                    if (userProfile == null) {
                        log.warn("Unauthorized access attempt or expired session for user: {}", userId);
                        throw new JwtAuthenticationException(ErrorCode.AUTH_SESSION_EXPIRED);
                    }

                    // redis cache 객체를 Users 객체로 변환 후 security context에 세팅
                    Users userDetails = Users.from(userProfile);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            log.warn("JWT token validation failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();

            CommonResponse<Void> body = CommonResponse.fail(e.getErrorCode());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(body));
            return;
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

