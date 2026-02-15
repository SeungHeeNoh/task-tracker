package com.hohohehe.checktracker.mapper;

import com.hohohehe.checktracker.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {

    Optional<User> findByUserId(String userId);

    void save(User entity);
}
