import { api } from "@/lib/axios";

class RelatorioService {
    async getPatrimonio() {
        const response = await api.get("/patrimonio/atual")
        return response.data
    }

    async getPatrimonioHistorico() {
        const response = await api.get("/patrimonio/historico")
        return response.data
    }

    async getDinheiroTotal() {
        const response = await api.get("/relatorios/dinheiro-total")
        return response.data
    }
}

export const relatorioService = new RelatorioService()