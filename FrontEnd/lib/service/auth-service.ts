// services/auth.service.ts
import { api, auth } from '../axios';

export interface ApiResponse<T> {
  data: T;
  status: number;
}

export interface UserInfo {
  id: string;
  name: string;
  email: string;
}

class AuthService {
  // Faz o registro e salva cookie HttpOnly automaticamente
  async register(name: string, email: string, password: string): Promise<ApiResponse<any>> {
    const response = await auth.post(
      '/auth/register',
      { name, email, password },
      { withCredentials: true } // fundamental
    );

    // O backend já deve ter enviado o cookie HttpOnly
    // Aqui só buscamos o usuário logado
    await this.fetchAndSaveUserInfo();
    return { data: response.data, status: response.status };
  }

  // Faz login e backend define o cookie HttpOnly (JWT)
  async login(username: string, password: string): Promise<ApiResponse<any>> {
    const response = await auth.post(
      '/login',
      { username, password },
      { withCredentials: true } // envia e recebe cookies
    );

    if (response.status !== 200) {
      throw new Error(response.data?.message || 'Erro ao fazer login');
    }

    await this.fetchAndSaveUserInfo();
    return { data: response.data, status: response.status };
  }

  // Busca o usuário atual
  async getCurrentUser(): Promise<UserInfo> {
    const response = await api.get('/user/me', {
      withCredentials: true, // inclui o cookie automaticamente
    });

    return response.data;
  }

  private setUserInfo(userInfo: UserInfo): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem('fluxo_user', JSON.stringify(userInfo));
    }
  }

  getUserInfo(): UserInfo | null {
    if (typeof window === 'undefined') return null;
    const userInfo = localStorage.getItem('fluxo_user');
    return userInfo ? JSON.parse(userInfo) : null;
  }

  private async fetchAndSaveUserInfo(): Promise<void> {
    try {
      const userInfo = await this.getCurrentUser();
      this.setUserInfo(userInfo);
    } catch {
      this.logout();
    }
  }

  async logout(): Promise<void> {
    try {
      await api.post('/logout', {}, { withCredentials: true });
    } catch (e) {
      console.error('Erro ao deslogar', e);
    } finally {
      if (typeof window !== 'undefined') {
        localStorage.removeItem('fluxo_user');
      }
    }
  }
}

export const authService = new AuthService();
