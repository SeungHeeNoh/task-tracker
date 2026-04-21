package com.hohohehe.tasktracker.common;

import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Users;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Users user = new Users();
        user.setUserSeq(customUser.userSeq());
        user.setUserId(customUser.userId());
        user.setUserName(customUser.userName());
        
        // 권한 검증을 위해 그룹 추가 (기본적으로 groupSeq 1L 추가)
        Groups group = new Groups();
        group.setGroupSeq(1L);
        user.setGroup(List.of(group));

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
