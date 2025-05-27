package com.hohohehe.checktracker.service;

import java.time.LocalDate;

public interface CheckLogService {

    void saveCheckLog(Long checkListId, LocalDate checkDate);

    void deleteCheckLog(Long checkListId, LocalDate checkDate);
}