package perfil;

import java.util.ArrayList;

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

    public ArrayList<Perfil> obterTodosPerfis(){
        return perfilRepository.obterTodosPerfis();
    }

    public void alterarPerfil(String id,Perfil perfil){
        perfilRepository.alterarPerfil(id, perfil);
    }

    public void deletarPerfil(String id){
        perfilRepository.deletarPerfil(id);
    }
}
