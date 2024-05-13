import http from "k6/http";
import { check, group, sleep, fail } from 'k6';

export const options = {
    stages: [
        { duration: '5s', target: 1 },
        { duration: '5s', target: 3 },
        // { duration: '5s', target: 16 },
        // { duration: '5s', target: 32 },
        // { duration: '5s', target: 16 },
        // { duration: '5s', target: 8 },
        { duration: '5s', target: 1 },
    ],

    thresholds: {
        http_req_duration: ['p(95)<1000'],
    }
};

export default function () {

    // register item to wishlist
    const url1 = 'http://localhost:8080/item-service/item/1/1';

    const payload1 = JSON.stringify({
        "count": 1,
    });

    const params1 = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MzEyNjRhOC00MjZlLTQ0ZDAtODNkYi0zOWFlNjQ3YzZkYWEiLCJpYXQiOjE3MTU2MTEzMzksImF1dGgiOiJVU0VSIiwiZXhwIjoxNzE1NjE0OTM5fQ.7rN3LRWuaWZ3NOyB82NAZGNjyYe4SiJeRjIF3scBG6joke9R1s3_QSmvhYaPigFM9Mhq9-u4Cwdn5u5a_kD_zg',
        },
    };

    let post1 = http.post(url1, payload1, params1);

    check(post1, {
        'success to register item to wishlist': (res) => res.status === 200,
    });

    // order items in wishlist
    const url2 = 'http://localhost:8080/item-service/wish-item/1/test';

    const payload2 = JSON.stringify({
    });

    const params2 = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MzEyNjRhOC00MjZlLTQ0ZDAtODNkYi0zOWFlNjQ3YzZkYWEiLCJpYXQiOjE3MTU2MTEzMzksImF1dGgiOiJVU0VSIiwiZXhwIjoxNzE1NjE0OTM5fQ.7rN3LRWuaWZ3NOyB82NAZGNjyYe4SiJeRjIF3scBG6joke9R1s3_QSmvhYaPigFM9Mhq9-u4Cwdn5u5a_kD_zg',
        },
    };

    let post2 = http.post(url2, payload2, params2);

    check(post2, {
        'success to order items in wishlist': (res) => res.status === 200,
    });
}