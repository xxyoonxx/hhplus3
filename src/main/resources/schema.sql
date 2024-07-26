CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT  PRIMARY KEY,
                       user_name VARCHAR(50) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       phone VARCHAR(20)
);

CREATE TABLE balance (
                         balance_id BIGINT AUTO_INCREMENT  PRIMARY KEY,
                         USER_ID BIGINT,
                         UNIQUE(USER_ID),
                         balance INT
);

CREATE TABLE balance_history (
                                 balance_history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 balance_id BIGINT NOT NULL,
                                 amount INT NOT NULL,
                                 type VARCHAR(50) NOT NULL,
                                 created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE concert (
                         concert_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         concert_title VARCHAR(255)
);

CREATE TABLE concert_detail (
                                detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                concert_id BIGINT,
                                concert_date DATETIME
);

CREATE TABLE concert_seat (
                              seat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              detail_id BIGINT,
                              seat_no VARCHAR(50),
                              status VARCHAR(50) CHECK (status IN ('OCCUPIED', 'AVAILABLE')),
                              seat_price INT
);

CREATE TABLE reservation (
                             reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT,
                             seat_id BIGINT,
                             concert_title VARCHAR(255),
                             reservation_date TIMESTAMP,
                             status VARCHAR(50) CHECK (status IN ('WAITING', 'DONE', 'EXPIRED')),
                             total_price INT
);

CREATE TABLE reservation_pay (
                                 pay_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 reservation_id BIGINT,
                                 pay_date TIMESTAMP,
                                 pay_amount INT,
                                 status VARCHAR(50) CHECK (status IN ('WAITING', 'DONE', 'EXPIRED'))
);

CREATE TABLE user_queue (
                            queue_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT,
                            token VARCHAR(255),
                            status VARCHAR(50) CHECK (status IN ('WAITING', 'PROCESSING', 'EXPIRED')),
                            created_date TIMESTAMP,
                            expiry_date TIMESTAMP
);
