import { api } from "@/lib/axios";

class CategoriaService {
    async getAllCategorias() {
        const response = await api.get('/categoria');
        return response.data;
    }

    async createCategoria(data: { nome: string }) {
        const response = await api.post('/categoria', data);
        return response.data;
    }

    async updateCategoria(id: string, data: { id: string; nome: string }) {
        const response = await api.put(`/categoria/${id}`, data);
        return response.data;
    }

    async deleteCategoria(id: string) {
        const response = await api.delete(`/categoria/${id}`);
        return response.data;
    }
}

export const categoriasService = new CategoriaService();