package perfil;

import java.util.ArrayList;

public interface PerfilRepository {

    void salvarPerfil(Perfil perfil);

    Perfil obterPerfil(String id);

    ArrayList<Perfil> obterTodosPerfis();

    void alterarPerfil(String id,Perfil perfil);

    void deletarPerfil(String id);

}