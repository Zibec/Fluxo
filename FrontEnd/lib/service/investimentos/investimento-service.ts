import { api } from "@/lib/axios";
import { createInvestimentoFormData } from "./investimento-schema";

class InvestimentoService {
    async getInvestimentosByUserId() {
        const data = api.get(`/investimentos/`)
        return data;
    }

    async getTaxaSelic() {
        const data = api.get(`/investimentos/taxa-selic`)
        return data;
    }

    async getInvestimento(id: string) {
        const data = api.get(`/investimentos/${id}`)
        return data;
    }

    async getHistoricoInvestimento(id: string) {
        const data = api.get(`/investimentos/historicos/${id}`)
        return data;
    }

    async createInvestimento(investimento: createInvestimentoFormData) {
        const data = api.post(`/investimentos/`, investimento)
        return data;
    }

    async resgateTotal(id: string) {
        const data = api.post(`/investimentos/resgate-total/${id}`)
        return data;
    }

    async resgateParcial(id: string, valor: number) {
        const data = api.put(`/investimentos/resgate-parcial/${id}`, { valor: valor })
        return data;
    }

}

export const investimentoService = new InvestimentoService();