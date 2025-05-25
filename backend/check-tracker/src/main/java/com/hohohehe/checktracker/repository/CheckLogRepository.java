package com.hohohehe.checktracker.repository;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.domain.CheckLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CheckLogRepository extends JpaRepository<CheckLog, Long> {

    List<CheckLog> findByCheckList_CheckListIdAndCheckDate(Long checkListId, LocalDate checkDate);
}
