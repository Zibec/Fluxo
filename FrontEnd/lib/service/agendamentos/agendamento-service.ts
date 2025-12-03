import { api } from "@/lib/axios";
import { createAgendamentoFormData } from "./agendamento-schema";

class AgendamentoService {
	async getAllAgendamentos() {
		const response = await api.get("/agendamento/todos");
		return response.data;
	}

	async getAgendamentoById(id: string) {
		const response = await api.get(`/agendamento/${id}`);
		return response.data;
	}

	async createAgendamento(data: createAgendamentoFormData) {
		const response = await api.post("/agendamento/criar", data);
		return response.data;
	}

	async updateAgendamento(id: string, data: createAgendamentoFormData) {
		const response = await api.put(`/agendamento/atualizar/${id}`, data);
		return response.data;
	}

	async deleteAgendamento(id: string) {
		const response = await api.delete(`/agendamento/deletar/${id}`);
		return response.data;
	}
}

export const agendamentoService = new AgendamentoService();
