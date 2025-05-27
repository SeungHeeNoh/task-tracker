package com.hohohehe.checktracker.repository;

import com.hohohehe.checktracker.domain.CheckLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CheckLogRepository extends JpaRepository<CheckLog, Long> {

    Optional<CheckLog> findByCheckList_CheckListIdAndCheckDate(Long checkListId, LocalDate checkDate);
}
