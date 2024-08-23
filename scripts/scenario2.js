import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

/*
시나리오1: 평시 트래픽 시나리오 (vuser 100)
*/
export let options = {
    scenarios: {
        reservation_scenario: {
            vus: 100, // Number of virtual users
            exec: 'reservation_scenario',
            executor: 'per-vu-iterations', // Each VU will run a fixed number of iterations
            iterations: 1
        }
    }
};

export function reservation_scenario() {
    // Generate random data for reservationx
    let detailId = 1
    let seatId = randomIntBetween(1, 50);
    let userId = __VU;
    let totalPrice = 10000
    let reservationDate = new Date().toISOString(); // Current timestamp in ISO format

    // Create reservation payload
    const payload = JSON.stringify({
        detailId: detailId,
        seatId: seatId,
        userId: userId,
        totalPrice: totalPrice,
        reservationDate: reservationDate
    });

    let res = http.post('http://localhost:8080/queue/', payload, {
        headers: {
            'Content-Type': 'application/json',
        },
    });
    check(res, { 'is status 200': (r) => r.status === 200 });
    console.log("* Queue Response body: " + res.body);
    const authorization = res.json().token;
    console.log("** payload: " + payload);

    // Step 1: Reserve a seat
    res = http.post('http://localhost:8080/reservation/', payload, {
        headers: {
            'Authorization': authorization,
            'Content-Type': 'application/json',
        },
        tags: { name: 'reserve_seat' }
    });

    // Check if reservation was successful
    check(res, { 'is status 200': (r) => r.status === 200 });
    // Optionally, log the response
    console.log("*** Reservation Response body: " + res.body);

    sleep(1); // Pause for a second before the next iteration
}
