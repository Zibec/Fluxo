import axios, { AxiosHeaders } from 'axios';

const BASE_URL = 'http://localhost:8080';

export const auth = axios.create({
    baseURL: BASE_URL,
    headers: new AxiosHeaders({
        'Content-Type': 'application/json'
    }),
});

export const api = axios.create({
    baseURL: BASE_URL + '/api',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json'
    }),
});
