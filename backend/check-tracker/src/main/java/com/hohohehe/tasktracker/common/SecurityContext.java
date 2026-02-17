package com.hohohehe.tasktracker.common;

import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.Users;

public class SecurityContext {

    public static Users getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("인증 정보가 없습니다.");
//        }
//
//        // Principal 객체를 가져와 User 타입으로 캐스팅
//        return (User) authentication.getPrincipal();

        Users users = new Users();
        users.setUserSeq(1L);
        users.setUserId("nsh");

        Groups groups = new Groups();
        groups.setGroupSeq(1L);
        users.getGroup().add(groups);

        return users;
    }
}
