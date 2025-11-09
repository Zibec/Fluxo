import { api } from "@/lib/axios";
import { createMetaFormData } from "./meta-schema";

class MetaService {
    async createMeta(data: createMetaFormData) {
        console.log('Creating meta with data:', data);
        const response = await api.post('/metas-poupanca', data);
        return response.data;
    } 

    async updateMeta(id: string, data: createMetaFormData) {
        const response = await api.put(`/metas-poupanca/${id}`, data);
        return response.data;
    }

    async deleteMeta(id: string) {
        const response = await api.delete(`/metas-poupanca/${id}`);
        return response.data;
    }

    async getAllMetas() {
        const response = await api.get('/metas-poupanca/by-user');
        return response.data;
    }

    async getMetaById(id: string) {
        const response = await api.get(`/metas-poupanca/id/${id}`);
        return response.data;
    }

    async addValueToMeta(id: string, value: number, accountId: string) {
        const response = await api.post(`/metas-poupanca/${id}/aporte`, { valor: value, contaId: accountId });
        return response.data;
    }
}

export const metaService = new MetaService()