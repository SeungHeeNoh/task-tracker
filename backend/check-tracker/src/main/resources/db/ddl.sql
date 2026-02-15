DROP TABLE IF EXISTS check_log;
DROP TABLE IF EXISTS check_list;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    user_seq BIGINT AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at datetime NOT NULL,
    created_by BIGINT NOT NULL,
    modified_at datetime NOT NULL,
    modified_by BIGINT NOT NULL,
    group_seq BIGINT NOT NULL,
    PRIMARY KEY (user_seq),
    UNIQUE KEY (user_id)
);

CREATE TABLE check_list(
    check_list_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    created_at datetime NOT NULL,
    created_by BIGINT NOT NULL,
    modified_at datetime NOT NULL,
    modified_by BIGINT NOT NULL
);

CREATE TABLE check_log(
    check_log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_list_id BIGINT NOT NULL,
    check_date DATE NOT NULL,
    created_at datetime NOT NULL,
    created_by BIGINT NOT NULL,
    modified_at datetime NOT NULL,
    modified_by BIGINT NOT NULL,
    UNIQUE (check_list_id, check_date)
);