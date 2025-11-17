import { api } from "@/lib/axios";
import { createSecurityFormData } from "./usuario-schema";

class UsuarioService {
    async alterarSenha(data: createSecurityFormData) {
        await api.post('/usuario/alterar-senha', data);
    }

    async alterarEmail(newEmail: string) {
}
