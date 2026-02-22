package com.hohohehe.tasktracker.common;

import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Users;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class SecurityContext {

    public static Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 익명 사용자인 경우 처리
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }

        // CustomUserDetails에 Users 엔티티를 담아두었다고 가정할 때
        Object principal = authentication.getPrincipal();
        if (principal instanceof Users) {
            return ((Users) principal);
        }

        throw new RuntimeException("유효하지 않은 인증 객체 타입입니다.");
    }

    public static List<Long> getCurrentUserGroupSeq() {
        return SecurityContext.getCurrentUser().getGroup()
                .stream()
                .map(Groups::getGroupSeq)
                .toList();
    }
}
