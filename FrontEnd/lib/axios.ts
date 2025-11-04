import axios, { AxiosHeaders } from 'axios';
import { authService } from './service/auth-service';

const BASE_URL = 'http://localhost:8080';

export const auth = axios.create({
    baseURL: BASE_URL + '/auth',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json'
    }),
    withCredentials: true
})

export const api = axios.create({
    baseURL: BASE_URL + '/api',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json'
    }),
    withCredentials: true
})

auth.interceptors.request.use(config => {
  if (typeof window !== 'undefined') {
    const tokens = localStorage.getItem('@fluxo:tokens')
    if (tokens) {
      try {
        const parsedTokens = JSON.parse(tokens)
        if (parsedTokens?.token) {
          // Cria novos headers se necessário
          if (!(config.headers instanceof AxiosHeaders)) {
            config.headers = new AxiosHeaders(config.headers)
          }
          config.headers.set('Authorization', `Bearer ${parsedTokens.token}`)
        }
      } catch (e) {
        console.error('Error parsing tokens:', e)
      }
    }
  }
  return config;
}, error => {
  return Promise.reject(error);
})

auth.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      authService.logout()
      if (typeof window !== 'undefined') {
        window.location.href = '/'
      }
    }
    return Promise.reject(error)
  }
);
