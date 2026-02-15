package com.hohohehe.checktracker.mapper;

import com.hohohehe.checktracker.domain.CheckLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Optional;

@Mapper
public interface CheckLogMapper {
    void save(CheckLog entity);

    void delete(Long checkLogId);

    Optional<CheckLog> findByCheckList_CheckListIdAndCheckDate(@Param("checkListId") Long checkListId, @Param("checkDate") LocalDate checkDate);
}
