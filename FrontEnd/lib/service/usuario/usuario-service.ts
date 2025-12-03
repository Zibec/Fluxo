import { api } from "@/lib/axios";
import {
	createProfileFormData,
	createSecurityFormData,
} from "./usuario-schema";
import { authService } from "../auth/auth-service";

class UsuarioService {
	async getUsuarioInfo() {
		const response = await api.get("/user/me");
		return response.data;
	}

	async alterarSenha(data: createSecurityFormData) {
		await api.post("/user/alterar-senha", data);
	}

	async alterarEmail(newEmail: string) {
		await api.post("/user/alterar-email", { newEmail: newEmail });
	}

	async alterarPreferencias(preferences: createProfileFormData) {
		await api.post("/user/preferences", preferences);

		localStorage.setItem("fluxo_user", JSON.stringify(preferences));
	}
}

export const usuarioService = new UsuarioService();
