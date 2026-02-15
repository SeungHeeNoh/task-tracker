package com.hohohehe.checktracker.common;

import com.hohohehe.checktracker.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }

        // Principal 객체를 가져와 User 타입으로 캐스팅
        return (User) authentication.getPrincipal();
    }
}
