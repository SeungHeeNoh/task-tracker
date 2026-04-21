package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.model.entity.Groups;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupsMapper {

    Groups findBySeq(@Param("groupSeq") Long groupSeq);
}
