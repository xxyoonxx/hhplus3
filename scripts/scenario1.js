import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

/*
시나리오1: 평시 트래픽 시나리오 (vuser 100)

*/
export let options = {
    scenarios: {
        queue_scenario: {
            vus: 100, // Number of virtual users
            exec: 'queue_scenario',
            executor: 'per-vu-iterations', // Each VU will run a fixed number of iterations
            iterations: 10
        }
    }
};

export function queue_scenario() {
    // Generate a random userId between 1 and 1000
    let userId = __VU;
    const payload = JSON.stringify({ userId: userId });

    // Step 1: Enter Queue
    let res = http.post('http://localhost:8080/queue/', payload, {
        headers: {
            'Content-Type': 'application/json',
        },
        tags: { name: 'enter_queue' }
    });
    console.log("**Response body: " + res.body);

    // Check if entering queue was successful and extract the authorization token
    check(res, { 'is status 200': (r) => r.status === 200 });
    const authorization = res.json().token;

    // Step 2: Check Queue Status
    res = http.get('http://localhost:8080/queue/status', {
        headers: {
            'Authorization': authorization,
            'Content-Type': 'application/json',
        },
        tags: { name: 'check_queue_status' }
    });

    // Check the queue status response
    check(res, {
        'is status 200': (r) => r.status === 200,
        'queue position is valid': (r) => r.json().queuePosition >= 1
    });

    // Check if fetching the concert list was successful
    check(res, { 'is status 200': (r) => r.status === 200 });

    sleep(1); // Pause for a second before the next iteration
}
