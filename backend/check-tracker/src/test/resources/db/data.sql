INSERT INTO check_list (check_list_id, title, created_at, created_by)
VALUES (1, '낮잠자기', NOW(), 'test');

INSERT INTO check_list (check_list_id, title, created_at, created_by)
VALUES (2, '물 마시기', NOW(), 'test');

INSERT INTO check_list (check_list_id, title, created_at, created_by)
VALUES (3, '달리기', NOW(), 'test');

INSERT INTO check_log(check_log_id, check_list_id, check_date, created_at, created_by)
value(1, 1, str_to_date('2025-05-25', '%Y-%m-%d'), now(), 'nsh');

INSERT INTO users(user_id, user_name, password, created_at, created_by, modified_at, modified_by, group_seq)
values('nsh', 'dorothy', '{bcrypt}$2a$10$dVXnXTSs9hD2TjGCQUnqvuwV2IF0RDC0UKzAbHDN01UIy.sZfrO6G', now(), 1, now(), 1, 1);