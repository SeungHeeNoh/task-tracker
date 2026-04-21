package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.common.enumCode.GroupRole;
import com.hohohehe.tasktracker.model.entity.UserGroupMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserGroupMapMapper {

    void insertMember(UserGroupMap userGroupMap);

    boolean existsMember(@Param("groupSeq") Long groupSeq, @Param("userSeq") Long userSeq);

    GroupRole findRoleByUserAndGroup(@Param("groupSeq") Long groupSeq, @Param("userSeq") Long userSeq);

    int countByGroupSeq(@Param("groupSeq") Long groupSeq);

    int countByUserSeq(@Param("userSeq") Long userSeq);
}
