package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.domain.CheckLog;
import com.hohohehe.checktracker.mapper.CheckListMapper;
import com.hohohehe.checktracker.mapper.CheckLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class CheckLogServiceImpl implements CheckLogService {

    private final CheckLogMapper checkLogMapper;
    private final CheckListMapper checkListMapper;

    @Override
    public void saveCheckLog(Long checkListId, LocalDate checkDate) {
        try {
            Optional<CheckLog> checkLog = checkLogMapper.findByCheckList_CheckListIdAndCheckDate(checkListId, checkDate);

            if (checkLog.isPresent()) {
                throw new IllegalArgumentException("이미 체크된 항목입니다.");
            } else {
                CheckList checkList = checkListMapper.findById(checkListId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 체크 리스트가 존재하지 않습니다."));
                checkLogMapper.save(CheckLog.of(checkList, checkDate));
            }
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("데이터 무결성 오류입니다.");
        }
    }

    @Override
    public void deleteCheckLog(Long checkListId, LocalDate checkDate) {
        Optional<CheckLog> checkLog = checkLogMapper.findByCheckList_CheckListIdAndCheckDate(checkListId, checkDate);

        if(checkLog.isPresent()) {
            checkLogMapper.delete(checkLog.get().getCheckLogId());
        } else {
            throw new IllegalArgumentException("체크된 적 없는 항목입니다.");
        }
    }
}
