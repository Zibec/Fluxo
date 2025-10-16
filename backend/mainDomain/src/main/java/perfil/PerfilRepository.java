package perfil;

import java.util.ArrayList;

public class PerfilRepository {

    private ArrayList<Perfil> perfis = new ArrayList<Perfil>();

    public void salvar(Perfil perfil){
        perfis.add(perfil);
    }

    public Perfil obter(String id){
        for (Perfil per : perfis){
            if (id.equals(per.getId())){
                return per;
            }
        }

        return null;
    }

    public ArrayList<Perfil> obterTodos(){
        return perfis;
    }

    public void alterar(String id,Perfil perfil){
        for (Perfil per : perfis){
            if (id.equals(per.getId())){
                per = perfil;
                return;
            }
        }

        throw new RuntimeException("Perfil não encontrado.");
    }

    public void deletar(String id){
        for (Perfil perf : perfis){
            if (id.equals(perf.getId())){
                perfis.remove(perf);
            }
        }
    }

}