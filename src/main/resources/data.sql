-- users 테이블
INSERT INTO users (user_name, email, phone) VALUES ('John Doe', 'john@example.com', '123-456-7890');

-- user_queue 테이블
INSERT INTO user_queue(user_id, token, status, created_date, expiry_date) VALUES (1, 'test-token', 'PROCESSING', now(), DATEADD(MINUTE, 30, now()));

-- concert 테이블
INSERT INTO concert (concert_title) VALUES ('콘서트01');

-- concert_detail 테이블
INSERT INTO concert_detail (concert_id, concert_date) VALUES (1, '2024-10-01 18:00:00');

-- concert_seat 테이블
INSERT INTO concert_seat (detail_id, seat_no, status, seat_price) VALUES
                                                                      (1, 'A01', 'AVAILABLE', 10000),
                                                                      (2, 'B01', 'OCCUPIED', 5000);
-- BALANCE 테이블
INSERT INTO BALANCE VALUES(1,1,0);
/*
-- reservation 테이블
INSERT INTO reservation (user_id, seat_id, concert_title, reservation_date, status, total_price) VALUES
    (1, 1, '콘서트01', NOW(), 'WAITING', 10000);

-- reservation_pay 테이블
INSERT INTO reservation_pay (reservation_id, pay_date, pay_amount, status) VALUES
    (1, NOW(), 10000, 'WAITING');
*/
INSERT INTO concert (concert_title)
SELECT
    CONCAT('Concert ', LPAD(x, 7, '0')) as concert_title
FROM SYSTEM_RANGE(1, 10000);