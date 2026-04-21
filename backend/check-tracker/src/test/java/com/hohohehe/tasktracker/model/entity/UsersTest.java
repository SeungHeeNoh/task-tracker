package com.hohohehe.tasktracker.model.entity;

import com.hohohehe.tasktracker.model.dto.request.ModifyUserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    @BeforeEach
    void setUp() {
        Users testUser = new Users();
        testUser.setUserSeq(1L);
        testUser.setUserId("testUser");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("of - 성공")
    void of_Success() {
        ModifyUserRequest request = new ModifyUserRequest("newName", "newAvatar");
        Users user = Users.of(1L, request);

        assertEquals(1L, user.getUserSeq());
        assertEquals("newName", user.getUsername());
        assertEquals("newAvatar", user.getAvatarImg());
    }

    @Test
    @DisplayName("UserDetails methods - 성공")
    void userDetailsMethods_Success() {
        Users user = new Users();
        
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
        assertNotNull(user.getAuthorities());
    }
}
