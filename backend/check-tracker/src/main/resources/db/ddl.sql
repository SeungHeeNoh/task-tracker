DROP TABLE IF EXISTS check_log;
DROP TABLE IF EXISTS check_list;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    user_id VARCHAR(50) primary key,
    password VARCHAR(255) NOT NULL,
    created_at datetime NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    modified_at datetime,
    modified_by VARCHAR(50)
);

CREATE TABLE check_list(
    check_list_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    modified_at DATETIME,
    modified_by VARCHAR(50)
);

CREATE TABLE check_log(
    check_log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_list_id BIGINT NOT NULL,
    check_date DATE NOT NULL,
    created_at DATETIME NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    modified_at DATETIME,
    modified_by VARCHAR(50)
);