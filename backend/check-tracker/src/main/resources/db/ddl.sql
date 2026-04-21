DROP TABLE IF EXISTS user_group_map;
DROP TABLE IF EXISTS task_log;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS `groups`;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_group_map;

CREATE TABLE `users`
(
    user_seq    BIGINT AUTO_INCREMENT,
    user_id     VARCHAR(50)  NOT NULL,
    user_name   VARCHAR(50)  NOT NULL,
    password    VARCHAR(255) NOT NULL,
    avatar_img  LONGTEXT NOT NULL,
    created_at  datetime     NOT NULL,
    created_by  BIGINT       NOT NULL,
    modified_at datetime     NOT NULL,
    modified_by BIGINT       NOT NULL,
    PRIMARY KEY (user_seq),
    UNIQUE KEY (user_id)
);

CREATE TABLE `groups`
(
    group_seq   BIGINT AUTO_INCREMENT,
    group_name  VARCHAR(50) NOT NULL,
    created_at  datetime    NOT NULL,
    created_by  BIGINT      NOT NULL,
    modified_at datetime    NOT NULL,
    modified_by BIGINT      NOT NULL,
    PRIMARY KEY (group_seq)
);

CREATE TABLE `user_group_map`
(
    group_seq  BIGINT   NOT NULL,
    user_seq   BIGINT   NOT NULL,
    role       VARCHAR(10) NOT NULL DEFAULT 'MEMBER',
    created_at datetime NOT NULL,
    created_by BIGINT   NOT NULL,
    PRIMARY KEY (group_seq, user_seq)
);


CREATE TABLE task
(
    task_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(50) NOT NULL,
    duedate     DATE        NOT NULL,
    deleted_yn  varchar(1)  NOT NULL DEFAULT 'N',
    created_at  datetime    NOT NULL,
    created_by  BIGINT      NOT NULL,
    modified_at datetime    NOT NULL,
    modified_by BIGINT      NOT NULL,
    group_seq   BIGINT      NOT NULL
);

CREATE TABLE task_log
(
    task_log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id     BIGINT      NOT NULL,
    task_status VARCHAR(25) NOT NULL,
    created_at  datetime    NOT NULL,
    created_by  BIGINT      NOT NULL
);