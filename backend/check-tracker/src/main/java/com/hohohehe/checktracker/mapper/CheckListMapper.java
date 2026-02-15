package com.hohohehe.checktracker.mapper;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.dto.v1.CheckListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CheckListMapper {

    List<CheckListDTO> findAll();

    CheckList save(CheckList entity);

    Optional<CheckList> findById(Long checkListId);
}
