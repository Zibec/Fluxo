import { AxiosError } from 'axios';
import { api } from '../../axios';

export interface Fatura {
    cartaoId: string,
    valorTotal: number,
    dataVencimento: Date,
    status: string,
    transacoes: string[]
}

class ContaService {
    async getAllContas() {
        try {
            const response = await api.get('/conta/by-user');
            return response.data;
        } catch (error) {
            if (error instanceof AxiosError && error?.status === 404) {
                return [];
            }
            console.error('Error fetching contas:', error);
            throw error;
        } 
    }

    async getContaById(id: string) {
        const response = await api.get(`/conta/${id}`);
        return response.data;
    }

    async createConta(data: any) {
        const response = await api.post('/conta/', data);
        return response.data;
    }

    async updateConta(id: string, data: any) {
        const response = await api.put(`/conta/${id}`, data);
        return response.data;
    }

    async deleteConta(id: string) {
        const response = await api.delete(`/conta/${id}`);
        return response.data;
    }
}

class CartaoService {
    async getAllCartoes() {
        try {
            console.log('Fetching all cartoes in ' + api.defaults.baseURL + '/cartao/by-user');
            const response = await api.get('/cartao/by-user');
            return response.data;
        } catch (error) {
            if (error instanceof AxiosError && error?.status === 404) {
                return [];
            }
            console.error('Error fetching cartoes:', error);
            throw error;
        }
        
    }

    async getFaturaByCartaoId(id: string) {
        const response = await api.get(`/cartao/fatura/${id}`);
        return response.data;
    }

    async closeFaturaByCartaoId(id: string) {
        const response = await api.post(`/cartao/fatura/${id}/fechar`);
        return response.data;
    }

    async payFaturaByCartaoId(id: string) {
        const response = await api.post(`/cartao/fatura/${id}/pagar`);
        return response.data;


    }

    async getCartaoById(id: string) {
        const response = await api.get(`/cartao/${id}`);
        return response.data;
    }

    async createCartao(data: any) {
        console.log('Creating cartao with data:', data);
        const response = await api.post('/cartao/', data);
        return response.data;
    }

    async updateCartao(id: string, data: any) {
        const response = await api.put(`/cartao/${id}`, data);
        return response.data;
    }

    async deleteCartao(id: string) {
        const response = await api.delete(`/cartao/${id}`);
        return response.data;
    }

}

export const cartoesService = new CartaoService();

export const contasService = new ContaService();