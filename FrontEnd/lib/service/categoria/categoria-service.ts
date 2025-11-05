import { api } from "@/lib/axios";
import axios, { AxiosHeaders } from "axios";

const categoria = axios.create({
    baseURL: api.defaults.baseURL + '/categoria',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json',
    }),
    withCredentials: true,
})

class CategoriaService