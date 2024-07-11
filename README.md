## 콘서트 예약 서비스

## [Milestone](https://github.com/users/xxyoonxx/projects/2)

## step05

<details>
<summary>요구사항 명세서</summary>

[대기열 API]
- 토큰 발급: 유저의 UUID, 대기순서, 잔여시간을 포함한 토큰 발급
- 대기열 검증: 유효한 대기열인지 검증
- 대기번호 조회: 현재 대기순서를 조회
- 대기열 만료: 좌석 배정 완료, 전 좌석 매진, 임시 배정 시간 초과 시 토큰을 만료시킴

[콘서트 좌석 조회 API]
- 콘서트 조회: 진행되는 콘서트 조회
- 날짜 조회: 특정 콘서트의 예약 가능 날짜 조회
- 좌석 조회: 특정 콘서트, 특정 날짜의 좌석 정보 조회

[좌석 예약 API]
- 예약 요청: 콘서트, 좌석번호, 날짜 정보로 예약 요청
- 좌석 임시 배정: 예약 요청 성공시 최종 배정 전까지 5분간 좌석 임시 배정
- 좌석 최종 배정: 임시 배정 시간 내 결제 완료시 좌석 최종 배정

[잔액 충전/조회 API]
- 잔액 충전: 유저 정보와 충전금액을 받아 잔액 충전
- 잔액 조회: 유저 정보로 잔액 조회

[결제 API]
- 결제 생성: 좌석 임시 배정 시 결제 정보 생성하여 반환
- 결제 처리: 사용자의 잔액으로 결제를 요청하고 처리
- 결제 내역 조회: 결제 완료 된 내역 조회
</details>
<p>
<details>
<summary>시퀀스 다이어그램</summary>

![queueApi.png](assets%2Fsequence%2FqueueApi.png)

![seatApi.png](assets%2Fsequence%2FseatApi.png)

![reservationApi.png](assets%2Fsequence%2FreservationApi.png)

![balance.png](assets%2Fsequence%2Fbalance.png)

![chargeApi.png](assets%2Fsequence%2FchargeApi.png)

![paymentApi.png](assets%2Fsequence%2FpaymentApi.png)

</details>

## step06

<details>
<summary>ERD 설계</summary>

![](assets/erd/erd.png)

</details>
<p>
<details>
<summary>API 명세서</summary>

## [원본 링크](https://fancy-act-7c0.notion.site/ef679d726eb54065a7fac47add8d0b30?v=c041866b5a224b788f056cdd8caf4ceb&pvs=4)

![apispec.png](assets%2Fapispec.png)

</details>
<p>
<details>
<summary>Mock API</summary>

- /queue POST 대기열 요청

![queue.png](assets%2Fmockapi%2Fqueue.png)

- /queue/status GET 대기열 확인

![queueStatus.PNG](assets%2Fmockapi%2FqueueStatus.PNG)

- /concerts [GET 콘서트 목록 조회](https://4d27ed9a-e6d8-40be-beb9-c3124be0a8ff.mock.pstmn.io/concerts)

- /concerts/{concertId}/dates [GET 예약 가능 날짜 조회](https://4d27ed9a-e6d8-40be-beb9-c3124be0a8ff.mock.pstmn.io/concerts/1/dates)

- /concerts/{concertId}/dates/{detailId}/seats [GET 예약 가능 좌석 조회](https://4d27ed9a-e6d8-40be-beb9-c3124be0a8ff.mock.pstmn.io/concerts/1/dates/2/seats)

- /reservation POST 좌석 예약 요청

![reservation.png](assets%2Fmockapi%2Freservation.png)

- /payment/{userId}/balance [GET 잔액 조회](https://4d27ed9a-e6d8-40be-beb9-c3124be0a8ff.mock.pstmn.io/user/1/balance)

- /payment/{userId}/charge PATCH 잔액 충전

![charge.PNG](assets%2Fmockapi%2Fcharge.PNG)

- /payment POST 결제

![payment.png](assets%2Fmockapi%2Fpayment.png)

</details>