import { api, HttpResponse } from "@/lib/axios";
import { createCategoriaFormData } from "./categoria-schemas";

class CategoriaService {
    async getAllCategorias() {
        const response = await api.get<HttpResponse<createCategoriaFormData>>('/categoria');
        return response.data;
    }

    async getCategoriaById(id: string) {
        const response = await api.get(`/categoria/id/${id}`);
        return response.data;
    }

    async createCategoria(data: createCategoriaFormData) {
        const response = await api.post('/categoria', data);
        return response.data;
    }

    async updateCategoria(id: string, data: createCategoriaFormData) {
        const response = await api.put(`/categoria/${id}`, data);
        return response.data;
    }

    async deleteCategoria(id: string) {
        const response = await api.delete(`/categoria/${id}`);
        return response.data;
    }
}

export const categoriasService = new CategoriaService();