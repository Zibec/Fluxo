package persistencia.jpa.usuario;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import usuario.Email;
import usuario.Usuario;
import usuario.UsuarioRepositorio;
import usuario.UsuarioService;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepositorio {
    @Autowired
    private UsuarioJpaRepository repository;

    @Autowired
    private Mapper mapper;


    @Override
    public void salvarUsuario(Usuario usuario) {
        var usuarioObj = mapper.map(usuario, UsuarioJpa.class);
        repository.save(usuarioObj);
    }

    @Override
    public Optional<Usuario> obterUsuario(String contaId) {
        if(repository.findById(contaId).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.map(repository.findById(contaId), Usuario.class));
    }

    public Optional<Usuario> obterUsuarioPorNome(String username) {
        if(!repository.existsByUsername(username)) {
            return Optional.empty();
        }

        return Optional.of(mapper.map(repository.findByUsername(username), Usuario.class));
    }

    public List<Usuario> obterUsuarios() {
        var usuarios = repository.findAll();
        return mapper.map(usuarios, List.class);
    }

    public Optional<Usuario> obterUsuarioPorEmail(String email) {
        if(!repository.existsByUserEmail(email)) {
            return Optional.empty();
        }

        return Optional.of(mapper.map(repository.findByUserEmail(email), Usuario.class));
    }

    @Override
    public Optional<Usuario> obterPorNome(String username) {
        return Optional.of(mapper.map(repository.findByUsername(username), Usuario.class));
    }

    @Override
    public void deletarUsuario(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean emailExistente(String email) {
        return repository.findByUserEmail(email);
    }

    @Override
    public boolean usernameExistente(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    @Transactional
    public void atualizarUsuario(Usuario usuario) {
        var oldUsuarioObj = repository.findById(usuario.getId()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        oldUsuarioObj.username = usuario.getUsername();
        oldUsuarioObj.userEmail = usuario.getEmail().getEndereco();
        oldUsuarioObj.password = usuario.getPassword();
        oldUsuarioObj.moedaPreferida = usuario.getMoedaPreferida().toString();
        oldUsuarioObj.formatoDataPreferido = usuario.getFormatoDataPreferido().toString();
    }
}
