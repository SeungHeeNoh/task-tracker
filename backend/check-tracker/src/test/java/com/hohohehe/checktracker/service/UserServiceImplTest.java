package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.User;
import com.hohohehe.checktracker.dto.v1.UserDto;
import com.hohohehe.checktracker.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void givenUserDto_whenNoneExistId_thenSaveUser() {
        // given
        String userId = "test";
        UserDto param = createUserDto(userId);
        given(userMapper.findByUserId(any(String.class))).willReturn(Optional.empty());
        willDoNothing().given(userMapper).save(any(User.class));

        // when
        userService.saveUser(param);

        // then
        then(userMapper).should().findByUserId(userId);
        verify(userMapper, times(1)).save(any(User.class));
    }

    @Test
    void givenUserDto_whenExistId_thenThrowingIllegalArgumentException() {
        // given
        String userId = "nsh";
        UserDto param = createUserDto(userId);
        given(userMapper.findByUserId(any(String.class))).willReturn(Optional.of(createUser("nsh")));

        // when && then
        assertThatThrownBy(() -> userService.saveUser(param))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
        then(userMapper).should().findByUserId(any(String.class));
    }

    @Test
    void givenUserId_whenExistUser_thenReturningUser() {
        // given
        String userId = "nsh";
        given(userMapper.findByUserId(any(String.class))).willReturn(Optional.of(createUser(userId)));

        // when && then
        userService.loadUserByUsername(userId);

        // then
        then(userMapper).should().findByUserId(any(String.class));
    }

    @Test
    void givenUserId_whenNoneExistUser_thenThrowingUsernameNotFoundException() {
        // given
        String userId = "test";
        given(userMapper.findByUserId(any(String.class))).willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> userService.loadUserByUsername(userId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("해당 유저 없음: " + userId);
        then(userMapper).should().findByUserId(any(String.class));
    }

    // fixture
    private User createUser(String userId) {
        return User.of(userId, "1234");
    }

    private UserDto createUserDto(String userId) {
        return UserDto.builder()
                .userId(userId)
                .password("password")
                .build();
    }
}