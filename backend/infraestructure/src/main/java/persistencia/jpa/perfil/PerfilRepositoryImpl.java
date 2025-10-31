package persistencia.jpa.perfil;

import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import perfil.Perfil;
import perfil.PerfilRepository;
import persistencia.jpa.Mapper;

import java.util.ArrayList;

@Repository
public class PerfilRepositoryImpl implements PerfilRepository {
    @Autowired
    private PerfilJpaRepository repository;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvarPerfil(Perfil perfil) {
        var perfilJpa = mapper.map(perfil, PerfilJpa.class);
        repository.save(perfilJpa);
    }

    @Override
    public Perfil obterPerfil(String id) {
        var perfilJpa = repository.findById(id);
        return mapper.map(perfilJpa, Perfil.class);
    }

    @Override
    public ArrayList<Perfil> obterTodosPerfis() {
        var perfilJpa = repository.findAll();
        return mapper.map(perfilJpa, ArrayList.class);
    }

    @Override
    public void alterarPerfil(String id, Perfil perfil) {
        repository.deleteById(id);
        var perfilJpa = mapper.map(perfil, PerfilJpa.class);
        perfilJpa.id = id;
        repository.save(perfilJpa);
    }

    @Override
    public void deletarPerfil(String id) {
        repository.deleteById(id);
    }
}
