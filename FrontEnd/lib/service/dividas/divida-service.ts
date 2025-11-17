import { api } from "@/lib/axios"
import { createDividaFormData } from "./divida-schema"

class DividaService {
    async getAllDividas() {
        await api.get("/metas-quitacao/by-user").then(response => {
            return response.data;
        }).catch(error => {
            if (error.response && error.response.status === 404) {
                return []
            } 

            throw error
        })
    }

    async createDivida(data: createDividaFormData) {
        const response = await api.post("/metas-quitacao", data)
        return response.data
    }

    async updateDivida(dividaId: string, data: createDividaFormData) {
        const response = await api.put(`/metas-quitacao/${dividaId}`, data)
        return response.data
    }

    async deleteDivida(dividaId: string) {
        await api.delete(`/metas-quitacao/${dividaId}`)
    }

    async addPayment(dividaId: string, amount: number) {
        const response = await api.post(`/metas-quitacao/${dividaId}/aporte`, { valor: amount })
        return response.data
    }
}

export const dividaService = new DividaService()