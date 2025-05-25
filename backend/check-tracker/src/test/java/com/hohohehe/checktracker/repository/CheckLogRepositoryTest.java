package com.hohohehe.checktracker.repository;

import com.hohohehe.checktracker.config.TestJpaConfig;
import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.domain.CheckLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestJpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CheckLogRepositoryTest {

    @Autowired
    private CheckLogRepository checkLogRepository;

    @Test
    void givenCheckListIdAndCheckDate_whenSearch_thenReturningMatchedCheckLogs() {
        // given
        CheckLog param = createCheckLog();

        // when
        List<CheckLog> result = checkLogRepository.findByCheckList_CheckListIdAndCheckDate(param.getCheckList().getCheckListId(), param.getCheckDate());

        // then
        assertThat(result.size())
                .isEqualTo(1);
    }

    @Test
    void givenTestData_whenSave_thenInsertCheckLog() {
        // given
        long prevCount = checkLogRepository.count();
        CheckLog param = CheckLog.of(createCheckList(2L), LocalDate.now());

        // when
        checkLogRepository.save(param);

        // then
        assertThat(checkLogRepository.count())
                .isEqualTo(prevCount + 1);
    }

    // fixture
    private CheckLog createCheckLog() {
        return CheckLog.of(createCheckList(1L), LocalDate.now());
    }

    private CheckList createCheckList(Long checkListId) {
        CheckList checkList = CheckList.of("test");
        ReflectionTestUtils.setField(checkList, "checkListId", checkListId);

        return checkList;
    }
}