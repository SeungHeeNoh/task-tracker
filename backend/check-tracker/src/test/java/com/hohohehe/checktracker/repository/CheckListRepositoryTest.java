//package com.hohohehe.checktracker.repository;
//
//import com.hohohehe.checktracker.config.TestJpaConfig;
//import com.hohohehe.checktracker.domain.CheckList;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@Import(TestJpaConfig.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class CheckListRepositoryTest {
//
//    @Autowired
//    private CheckListRepository checkListRepository;
//
//    @Test
//    void givenNoting_whenFindAll_returningChecklists() {
//        // given
//
//        // when
//        List<CheckList> result = checkListRepository.findAll();
//
//        // then
//        assertThat(result)
//                .isNotNull();
//    }
//
//    @Test
//    void givenTestData_whenSave_InsertCheckList() {
//        // given
//        long prevCount = checkListRepository.count();
//        CheckList checkList = CheckList.of("test");
//
//        // when
//        checkListRepository.save(checkList);
//
//        // then
//        assertThat(checkListRepository.count())
//                .isEqualTo(prevCount + 1);
//    }
//}