package perfil;

import java.util.ArrayList;
import java.util.List;

public interface PerfilRepository {

    void salvarPerfil(Perfil perfil);

    Perfil obterPerfil(String id);

    List<Perfil> obterTodosPerfis();

    void alterarPerfil(String id,Perfil perfil);

    void deletarPerfil(String id);

    List<Perfil> obterTodosPerfisPorUsuarioId(String id);

}