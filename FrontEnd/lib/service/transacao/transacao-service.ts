import { api } from "@/lib/axios"
import { createTransacaoFormData } from "./transacao-schema"

class TransacaoService {
    async getTransacao(id: string) {
        const response = await api.get(`/transacao/${id}`)
        return response.data
    }

    async getTransacoesByUser() {
        const response = await api.get(`/transacao/by-user`)
        return response.data
    }

    async getTransacaoByAgendamento(agendamentoId: string, data: string) {
        const response = await api.get(`/transacao/${agendamentoId}/${data}`)
        return response.data
    }

    async getAllTransacoes() {
        const response = await api.get('/transacao')
        console.log(response.data)
        return response.data
    }

    async updateTransacao(id: string, data: createTransacaoFormData) {
        const response = await api.put(`/transacao/${id}`, data)
        return response.data
    }

    async createTransacao(data: createTransacaoFormData) {
        console.log(data)
        const response = await api.post(`/transacao`, data)
        return response.data
    }

    async useReembolso(data: createTransacaoFormData) {
        data.transacaoOriginalId = data.id
        const response = await api.post(`/transacao/reembolso`, data)
        return response.data
    }

    async deleteTransacao(id: string) {
        const response = await api.delete(`/transacao/${id}`)
        return response.data
    }

}

export const transacaoService = new TransacaoService()

