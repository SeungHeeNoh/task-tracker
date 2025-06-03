package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.User;
import com.hohohehe.checktracker.dto.v1.UserDto;
import com.hohohehe.checktracker.repository.UserRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void givenUserDto_whenNoneExistId_thenSaveUser() {
        // given
        String userId = "test";
        UserDto param = createUserDto(userId);
        given(userRepository.existsById(any(String.class))).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(any(User.class));

        // when
        userService.saveUser(param);

        // then
        then(userRepository).should().existsById(any(String.class));
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void givenUserDto_whenExistId_thenThrowingIllegalArgumentException() {
        // given
        String userId = "nsh";
        UserDto param = createUserDto(userId);
        given(userRepository.existsById(any(String.class))).willReturn(true);

        // when && then
        assertThatThrownBy(() -> userService.saveUser(param))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
        then(userRepository).should().existsById(any(String.class));
    }

    @Test
    void givenUserId_whenExistUser_thenReturningUser() {
        // given
        String userId = "nsh";
        given(userRepository.findByUserId(any(String.class))).willReturn(Optional.of(createUser(userId)));

        // when && then
        userService.loadUserByUsername(userId);

        // then
        then(userRepository).should().findByUserId(any(String.class));
    }

    @Test
    void givenUserId_whenNoneExistUser_thenThrowingUsernameNotFoundException() {
        // given
        String userId = "test";
        given(userRepository.findByUserId(any(String.class))).willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> userService.loadUserByUsername(userId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("해당 유저 없음: " + userId);
        then(userRepository).should().findByUserId(any(String.class));
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