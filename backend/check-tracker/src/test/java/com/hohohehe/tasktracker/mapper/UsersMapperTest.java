package com.hohohehe.tasktracker.mapper;

import com.hohohehe.tasktracker.model.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UsersMapperTest {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByUserId_ShouldPopulateGroups() {
        // given
        String userId = "testUserForGroups";
        jdbcTemplate.update(
                "INSERT INTO users (user_id, user_name, password, avatar_img, created_at, created_by, modified_at, modified_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                userId, "Test User", "pass", "img", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L
        );
        Long userSeq = jdbcTemplate.queryForObject("SELECT user_seq FROM users WHERE user_id = ?", Long.class, userId);

        jdbcTemplate.update(
                "INSERT INTO `groups` (group_name, created_at, created_by, modified_at, modified_by) VALUES (?, ?, ?, ?, ?)",
                "Group 1", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L
        );
        Long groupSeq1 = jdbcTemplate.queryForObject("SELECT group_seq FROM `groups` WHERE group_name = ? LIMIT 1", Long.class, "Group 1");

        jdbcTemplate.update(
                "INSERT INTO `groups` (group_name, created_at, created_by, modified_at, modified_by) VALUES (?, ?, ?, ?, ?)",
                "Group 2", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L
        );
        Long groupSeq2 = jdbcTemplate.queryForObject("SELECT group_seq FROM `groups` WHERE group_name = ? LIMIT 1", Long.class, "Group 2");

        jdbcTemplate.update(
                "INSERT INTO user_group_map (group_seq, user_seq, created_at, created_by) VALUES (?, ?, ?, ?)",
                groupSeq1, userSeq, LocalDateTime.now(), 1L
        );
        jdbcTemplate.update(
                "INSERT INTO user_group_map (group_seq, user_seq, created_at, created_by) VALUES (?, ?, ?, ?)",
                groupSeq2, userSeq, LocalDateTime.now(), 1L
        );

        // when
        Users user = usersMapper.findByUserId(userId);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getGroup()).hasSize(2);
        assertThat(user.getGroup()).extracting("groupName").containsExactlyInAnyOrder("Group 1", "Group 2");
    }

    @Test
    void findByUserId_ShouldReturnEmptyGroups_WhenUserHasNoGroups() {
        // given
        String userId = "userWithNoGroups";
        jdbcTemplate.update(
                "INSERT INTO users (user_id, user_name, password, avatar_img, created_at, created_by, modified_at, modified_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                userId, "No Group User", "pass", "img", LocalDateTime.now(), 1L, LocalDateTime.now(), 1L
        );

        // when
        Users user = usersMapper.findByUserId(userId);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getGroup()).isEmpty();
    }
}
