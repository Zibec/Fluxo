import axios, { AxiosHeaders } from 'axios';
import { api } from '../../axios';

const cartao = axios.create({
    baseURL: api.defaults.baseURL + '/cartao',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json',
    }),
    withCredentials: true,
})   

const conta = axios.create({
    baseURL: api.defaults.baseURL + '/conta',
    headers: new AxiosHeaders({
        'Content-Type': 'application/json',
    }),
    withCredentials: true,
})

class ContaService {
    async getAllContas() {
        try {
            console.log('Fetching all contas in ' + conta.defaults.baseURL + '/by-user');
            const response = await conta.get('/by-user');
            return response.data;
        } catch (error) {
            console.error('Error fetching contas:', error);
            throw error;
        }
        
    }

    async getContaById(id: string) {
        const response = await conta.get(`/${id}`);
        return response.data;
    }

    async createConta(data: any) {
        const response = await conta.post('/', data);
        return response.data;
    }

    async updateConta(id: string, data: any) {
        const response = await conta.put(`/${id}`, data);
        return response.data;
    }

    async deleteConta(id: string) {
        const response = await conta.delete(`/${id}`);
        return response.data;
    }
}

class CartaoService {
    async getAllCartoes() {
        try {
            console.log('Fetching all cartoes in ' + cartao.defaults.baseURL + '/by-user');
            const response = await cartao.get('/by-user');
            return response.data;
        } catch (error) {
            console.error('Error fetching cartoes:', error);
            throw error;
        }
        
    }

    async getCartaoById(id: string) {
        const response = await cartao.get(`/${id}`);
        return response.data;
    }

    async createCartao(data: any) {
        console.log('Creating cartao with data:', data);
        const response = await cartao.post('/', data);
        return response.data;
    }

    async updateCartao(id: string, data: any) {
        const response = await cartao.put(`/${id}`, data);
        return response.data;
    }

    async deleteCartao(id: string) {
        const response = await cartao.delete(`/${id}`);
        return response.data;
    }

}

export const cartoesService = new CartaoService();

export const contasService = new ContaService();