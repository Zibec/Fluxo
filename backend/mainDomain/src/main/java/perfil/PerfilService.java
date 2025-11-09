package perfil;

import java.util.ArrayList;
import java.util.List;

public class PerfilService {

    private PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public void salvarPerfil(Perfil perfil) {
        perfilRepository.salvarPerfil(perfil);
    }

    public Perfil obterPerfil(String id) {
        return perfilRepository.obterPerfil(id);
    }

    public List<Perfil> obterTodosPerfis(){
        return perfilRepository.obterTodosPerfis();
    }

    public void alterarPerfil(String id,Perfil perfil){
        perfilRepository.alterarPerfil(id, perfil);
    }

    public void deletarPerfil(String id){
        perfilRepository.deletarPerfil(id);
    }

    public List<Perfil> obterTodosPerfisPorUsuarioId(String id){
        return perfilRepository.obterTodosPerfisPorUsuarioId(id);
    }

}
