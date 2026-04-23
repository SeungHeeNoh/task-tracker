-- ============================================
-- 테스트 데이터 DML (초대 기능 검증용)
-- 비밀번호: 모두 'password' (BCrypt, strength 10)
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE user_group_map;
TRUNCATE TABLE task_log;
TRUNCATE TABLE task;
TRUNCATE TABLE `groups`;
TRUNCATE TABLE users;

-- ------------------------------------------------
-- USERS
-- ------------------------------------------------
INSERT INTO users (user_seq, user_id, user_name, password, avatar_img, created_at, created_by, modified_at, modified_by) VALUES
(1, 'owner01',  '오너일',   '{bcrypt}$2a$10$hVU9zym85ekNgMaEuhRWl.CWqLkfdOT.A4RDpyYCnDjoP7ui.etJ2', '', NOW(), 1, NOW(), 1),
(2, 'member01', '멤버일',   '{bcrypt}$2a$10$hVU9zym85ekNgMaEuhRWl.CWqLkfdOT.A4RDpyYCnDjoP7ui.etJ2', '', NOW(), 2, NOW(), 2),
(3, 'member02', '멤버이',   '{bcrypt}$2a$10$hVU9zym85ekNgMaEuhRWl.CWqLkfdOT.A4RDpyYCnDjoP7ui.etJ2', '', NOW(), 3, NOW(), 3),
(4, 'guest01',  '게스트일', '{bcrypt}$2a$10$hVU9zym85ekNgMaEuhRWl.CWqLkfdOT.A4RDpyYCnDjoP7ui.etJ2', '', NOW(), 4, NOW(), 4),
(5, 'full01',   '풀계정',   '{bcrypt}$2a$10$hVU9zym85ekNgMaEuhRWl.CWqLkfdOT.A4RDpyYCnDjoP7ui.etJ2', '', NOW(), 5, NOW(), 5);

-- ------------------------------------------------
-- GROUPS
-- group_seq = 1: 기본 시나리오 그룹 (owner01 OWNER, member01 MEMBER)
-- group_seq = 2: OWNER 전용 그룹 (owner01 only)
-- group_seq = 3: 타 그룹 (member02 OWNER) — 권한 거부 테스트용
-- group_seq = 4~8: full01 MEMBER 5개 — USER_GROUP_LIMIT_EXCEEDED 테스트용
-- ------------------------------------------------
INSERT INTO `groups` (group_seq, group_name, created_at, created_by, modified_at, modified_by) VALUES
    (1, '우리 가족',       NOW(), 1, NOW(), 1),
                                                                                                                              (2, '스터디 모임',     NOW(), 1, NOW(), 1),
                                                                                                                              (3, '빈 그룹',         NOW(), 2, NOW(), 2),
                                                                                                                              (4, '더미 그룹 A',     NOW(), 5, NOW(), 5),
                                                                                                                              (5, '더미 그룹 B',     NOW(), 5, NOW(), 5),
                                                                                                                              (6, '더미 그룹 C',     NOW(), 5, NOW(), 5),
                                                                                                                              (7, '더미 그룹 D',     NOW(), 5, NOW(), 5),
                                                                                                                              (8, '더미 그룹 E',     NOW(), 5, NOW(), 5);

-- ------------------------------------------------
-- USER_GROUP_MAP
-- ------------------------------------------------
INSERT INTO user_group_map (group_seq, user_seq, role, created_at, created_by) VALUES
                                                                                   (1, 1, 'OWNER',  NOW(), 1),
                                                                                   (1, 2, 'MEMBER', NOW(), 1),
                                                                                   (2, 1, 'OWNER',  NOW(), 1),
                                                                                   (3, 3, 'OWNER',  NOW(), 3),
                                                                                   (4, 5, 'MEMBER', NOW(), 5),
                                                                                   (5, 5, 'MEMBER', NOW(), 5),
                                                                                   (6, 5, 'MEMBER', NOW(), 5),
                                                                                   (7, 5, 'MEMBER', NOW(), 5),
                                                                                   (8, 5, 'MEMBER', NOW(), 5);

-- ------------------------------------------------
-- TASK (로그인 후 화면 확인용)
-- ------------------------------------------------
INSERT INTO task (task_id, title, duedate, deleted_yn, created_at, created_by, modified_at, modified_by, group_seq) VALUES
                                                                                                                        (1, '장보기',        CURDATE(),                           'N', NOW(), 1, NOW(), 1, 1),
                                                                                                                        (2, '빨래 돌리기',   DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'N', NOW(), 1, NOW(), 1, 1),
                                                                                                                        (3, '주간 회고',     DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'N', NOW(), 2, NOW(), 2, 1),
                                                                                                                        (4, '알고리즘 공부', CURDATE(),                           'N', NOW(), 1, NOW(), 1, 2);

-- ------------------------------------------------
-- TASK_LOG
-- ------------------------------------------------
INSERT INTO task_log (task_id, task_status, created_at, created_by) VALUES
                                                                        (1, 'CREATED',   NOW(), 1),
                                                                        (2, 'CREATED',   NOW(), 1),
                                                                        (3, 'CREATED',   NOW(), 2),
                                                                        (3, 'COMPLETED', NOW(), 2),
(4, 'CREATED',   NOW(), 1);

SET FOREIGN_KEY_CHECKS = 1;
