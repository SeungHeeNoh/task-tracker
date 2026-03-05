package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.mapper.UsersMapper;
import com.hohohehe.tasktracker.model.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UsersService implements UserDetailsService {

    private final UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = usersMapper.findByUserId(userId);

        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId);
        }

        return user;
    }

    public void join(Users users) {
        try {
            if (usersMapper.findByUserId(users.getUserId()) != null) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }
            usersMapper.joinUser(users);
            usersMapper.updateCreatorInfo(users);
        } catch (DataAccessException | PersistenceException e) {
            throw new SystemException(e, "잠시 후 다시 시도해주세요.");
        }
    }

    public void modifyUser(Users users) {
        try {
            if (usersMapper.findByUserId(users.getUserId()) == null) {
                throw new IllegalArgumentException("잘못된 접근입니다.");
            }
            usersMapper.modifyUser(users);
        } catch (DataAccessException | PersistenceException e) {
            throw new SystemException(e, "잠시 후 다시 시도해주세요.");
        }
    }
}
