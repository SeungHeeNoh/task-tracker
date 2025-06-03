package com.hohohehe.checktracker.repository;

import com.hohohehe.checktracker.config.TestJpaConfig;
import com.hohohehe.checktracker.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestJpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenUserId_whenFindUser_thenReturningUser() throws Exception {
        // given
        String userId = "nsh";

        // when
        Optional<User> result = userRepository.findByUserId(userId);

        // then
        assertThat(result.isPresent()).isTrue();
    }
}