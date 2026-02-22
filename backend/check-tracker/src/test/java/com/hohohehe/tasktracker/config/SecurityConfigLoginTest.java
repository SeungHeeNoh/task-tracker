package com.hohohehe.tasktracker.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class SecurityConfigLoginTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("존재하는 아이디와 올바른 비밀번호로 로그인하면 성공해야 한다.")
    void loginSuccessTest() {
        // given
        // 실제 DB에 존재하는 아이디와 비밀번호를 입력해주세요.
        String actualUserId = "user01";
        String actualPassword = "1234";

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(actualUserId, actualPassword);

        // when
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // then
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getName()).isEqualTo("김철수");
    }

    @Test
    @DisplayName("비밀번호가 틀리면 BadCredentialsException이 발생해야 한다.")
    void loginFailTest_WrongPassword() {
        // given
        String actualUserId = "testUser";
        String wrongPassword = "wrongPassword123!";

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(actualUserId,
                wrongPassword);

        // when & then
        assertThatThrownBy(() -> authenticationManager.authenticate(token))
                .isInstanceOf(BadCredentialsException.class);
    }
}
