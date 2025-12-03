import { api } from "@/lib/axios";
import { createInvestimentoFormData } from "./investimento-schema";

class InvestimentoService {
	async getInvestimentosByUserId() {
		const response = await api.get(`/investimentos/`);
		return response.data;
	}

	async getTaxaSelic() {
		const response = await api.get(`/investimentos/taxa-selic`);
		return response.data;
	}

	async getInvestimento(id: string) {
		const response = await api.get(`/investimentos/${id}`);
		return response.data;
	}

	async getHistoricoInvestimento(id: string) {
		const response = await api.get(`/investimentos/historicos/${id}`);
		return response.data;
	}

	async createInvestimento(investimento: createInvestimentoFormData) {
		const response = await api.post(`/investimentos/`, investimento);
		return response.data;
	}

	async resgateTotal(id: string) {
		const response = await api.post(`/investimentos/resgate-total/${id}`);
		return response.data;
	}

	async resgateParcial(id: string, valor: number) {
		const response = await api.put(`/investimentos/resgate-parcial/${id}`, {
			valor: valor,
		});
		return response.data;
	}
}

export const investimentoService = new InvestimentoService();
