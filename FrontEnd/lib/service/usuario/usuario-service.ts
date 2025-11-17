import { api } from "@/lib/axios"
import { createSecurityFormData } from "./usuario-schema"

class UsuarioService {
    async alterarSenha(data: createSecurityFormData) {
        await api.post('/user/alterar-senha', data)
    }

    async alterarEmail(newEmail: string) {
        await api.post('/user/alterar-email', { newEmail: newEmail })
    }

    async alterarPreferencias(preferences: any) {
        await api.post('/user/preferences', preferences)
    }
}

export const usuarioService = new UsuarioService()

