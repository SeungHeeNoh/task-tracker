package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.model.entity.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {

    Users findByUserId(String userId);

    void joinUser(Users users);

    void updateCreatorInfo(Users users);

    void modifyUser(Users users);
}
