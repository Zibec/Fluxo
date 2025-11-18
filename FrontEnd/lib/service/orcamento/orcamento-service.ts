import { api } from "@/lib/axios";
import { createOrcamentoFormData } from "./orcamento-schema";

class OrcamentoService {
    async createOrcamento(data: createOrcamentoFormData) {
        const response = await api.post('/orcamento/criar', data)
        return response.data
    }

    async getOrcamentos() {
        const response = await api.get('/orcamento/todos/by-user')

        if(response.status == 404) {
            return []
        }

        return response.data
     }

    async getOrcamentoById(data: createOrcamentoFormData) {
        const response = await api.get(`/orcamento/${data.categoriaId}/${data.anoMes}`)
        return response.data
    }

    async getOrcamentoTotalSpent(categoriaId: string, anoMes: string) {
        const response = await api.get(`/orcamento/saldo-total/${categoriaId}/${anoMes}`)
        return response.data
    }

    async updateOrcamento(data: createOrcamentoFormData) {
        const response = await api.put(`/orcamento/${data.categoriaId}/${data.anoMes}`, data)
        return response.data
    }
}

export const orcamentoService = new OrcamentoService()