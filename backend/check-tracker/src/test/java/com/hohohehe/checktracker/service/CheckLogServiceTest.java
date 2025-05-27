package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.domain.CheckLog;
import com.hohohehe.checktracker.repository.CheckListRepository;
import com.hohohehe.checktracker.repository.CheckLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CheckLogServiceTest {

    @Mock
    private CheckLogRepository checkLogRepository;

    @Mock
    private CheckListRepository checkListRepository;

    @InjectMocks
    private CheckLogServiceImpl checkLogService;

    @Test
    void givenCheckListIdAndCheckDate_whenNonExistCheckLog_thenSaveCheckLog() {
        // given
        Long checkListId = 1L;
        LocalDate checkDate = LocalDate.now();
        CheckList checkList = createCheckList(checkListId);
        CheckLog param = createCheckLog(checkList);
        given(checkLogRepository.findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class))).willReturn(Optional.empty());
        given(checkListRepository.findById(any(Long.class))).willReturn(Optional.of(checkList));
        given(checkLogRepository.save(any(CheckLog.class))).willReturn(param);

        // when
        checkLogService.saveCheckLog(checkListId, checkDate);

        // then
        then(checkLogRepository).should().findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class));
        then(checkListRepository).should().findById(any(Long.class));
        then(checkLogRepository).should().save(any(CheckLog.class));
    }

    @Test
    void givenCheckListIdAndCheckDate_whenExistCheckLog_thenThrowingIllegalArgumentException() {
        // given
        Long checkListId = 1L;
        LocalDate checkDate = LocalDate.now();
        CheckLog param = createCheckLog(checkListId);
        given(checkLogRepository.findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class))).willReturn(Optional.of(param));

        // when & then
        assertThatThrownBy(() -> checkLogService.saveCheckLog(checkListId, checkDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 체크된 항목입니다.");
        then(checkLogRepository).should().findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class));
    }

    @Test
    void givenCheckListIdAndCheckDate_whenExistCheckLog_thenDeleteCheckLog() {
        // given
        Long checkListId = 1L;
        LocalDate checkDate = LocalDate.now();
        CheckLog param = createCheckLog(checkListId);
        given(checkLogRepository.findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class))).willReturn(Optional.of(param));
        willDoNothing().given(checkLogRepository).delete(any(CheckLog.class));

        // when
        checkLogService.deleteCheckLog(checkListId, checkDate);

        // then
        then(checkLogRepository).should().findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class));
        then(checkLogRepository).should().delete(any(CheckLog.class));
    }

    @Test
    void givenCheckListIdAndCheckDate_whenNonExistCheckLog_thenThrowingIllegalArgumentException() {
        // given
        Long checkListId = 1L;
        LocalDate checkDate = LocalDate.now();
        given(checkLogRepository.findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class))).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> checkLogService.deleteCheckLog(checkListId, checkDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("체크된 적 없는 항목입니다.");
        then(checkLogRepository).should().findByCheckList_CheckListIdAndCheckDate(any(Long.class), any(LocalDate.class));
    }

    // fixture
    private CheckLog createCheckLog(Long checkListId) {
        return CheckLog.of(createCheckList(checkListId), LocalDate.now());
    }

    private CheckLog createCheckLog(CheckList checkList) {
        return CheckLog.of(checkList, LocalDate.now());
    }

    private CheckList createCheckList(Long checkListId) {
        CheckList checkList = CheckList.of("test");
        ReflectionTestUtils.setField(checkList, "checkListId", checkListId);

        return checkList;
    }
}