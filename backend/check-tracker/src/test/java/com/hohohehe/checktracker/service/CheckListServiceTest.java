package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.dto.v1.CheckListDTO;
import com.hohohehe.checktracker.mapper.CheckListMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class CheckListServiceTest {

    @Mock
    private CheckListMapper checkListMapper;

    @InjectMocks
    private CheckListServiceImpl checkListService;

    @Test
    void givenNoting_whenSearchCheckList_returningChecklists() {
        // given
        given(checkListMapper.findAll()).willReturn(List.of(new CheckListDTO()));

        // when
        List<CheckListDTO> result = checkListService.searchCheckList();

        // then
        assertThat(result)
                .isNotNull();
        then(checkListMapper).should().findAll();
    }

    @Test
    void givenTestData_whenSaveCheckList_InsertCheckList() {
        // given
        CheckList expectedCheckList = createCheckList(1L);
        given(checkListMapper.save(any(CheckList.class))).willReturn(expectedCheckList);
        CheckList checkList = CheckList.of("test");

        // when
        CheckList insertResult = checkListService.saveCheckList(checkList);

        // then
        assertThat(insertResult)
                .isEqualTo(expectedCheckList);
        then(checkListMapper).should().save(checkList);
    }

    // fixture
    private CheckList createCheckList(Long checkListId) {
        CheckList checkList = CheckList.of("test");
        ReflectionTestUtils.setField(checkList, "checkListId", checkListId);

        return checkList;
    }
}