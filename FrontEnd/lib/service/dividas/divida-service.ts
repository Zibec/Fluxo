import { api } from "@/lib/axios"
import { createDividaFormData } from "./divida-schema"

class DividaService {
    async getAllDividas() {
        const response = await api.get("/metas-quitacao/by-user")
        console.log(response.data)
        return response.data
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