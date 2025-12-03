import { api } from "@/lib/axios";

class PerfilService {
	async getPerfil(id: string) {
		const response = await api.get(`/perfis/${id}`);
		return response.data;
	}

	async getAllPerfis() {
		const response = await api.get("/perfis/");
		return response.data;
	}

	async updatePerfil(id: string, data: { nome: string }) {
		const response = await api.put(`/perfis/${id}`, data);
		return response.data;
	}

	async createPerfil(data: { nome: string }) {
		const response = await api.post(`/perfis/`, data);
		return response.data;
	}
}

export const perfilService = new PerfilService();
