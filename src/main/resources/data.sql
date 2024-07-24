-- users 테이블
INSERT INTO users (user_name, email, phone) VALUES ('John Doe', 'john@example.com', '123-456-7890');

-- concert 테이블
INSERT INTO concert (concert_title) VALUES ('콘서트01');

-- concert_detail 테이블
INSERT INTO concert_detail (concert_id, concert_date) VALUES (1, '2024-10-01 18:00:00');

-- concert_seat 테이블
INSERT INTO concert_seat (detail_id, seat_no, status, seat_price) VALUES
                                                                      (1, 'A01', 'AVAILABLE', 10000),
                                                                      (1, 'B01', 'OCCUPIED', 5000);

-- reservation 테이블
INSERT INTO reservation (user_id, seat_id, concert_title, reservation_date, status, total_price) VALUES
    (1, 1, '콘서트01', NOW(), 'WAITING', 10000);

-- reservation_pay 테이블
INSERT INTO reservation_pay (reservation_id, pay_date, pay_amount, status) VALUES
    (1, NOW(), 10000, 'WAITING');