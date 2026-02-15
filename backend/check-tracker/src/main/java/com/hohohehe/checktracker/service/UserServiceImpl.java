package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.User;
import com.hohohehe.checktracker.dto.v1.UserDto;
import com.hohohehe.checktracker.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserDto userDto) {
        if(userMapper.findByUserId(userDto.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        userMapper.save(User.of(userDto.getUserId(), passwordEncoder.encode(userDto.getPassword())));
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음: " + userId));
    }
}
