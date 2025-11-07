import axios, { AxiosHeaders } from 'axios'

const url = 'http://localhost:8080'

export const auth = axios.create({
    baseURL: url + '/auth',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json'
    }),
    withCredentials: true
})

export const api = axios.create({
    baseURL: url + '/api',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json'
    }),
    withCredentials: true
})