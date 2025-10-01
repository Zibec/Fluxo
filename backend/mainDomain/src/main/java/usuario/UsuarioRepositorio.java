package usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public class UsuarioRepositorio {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    public void salvar(Usuario usuario) {
        notNull(usuario, "O usuario não pode ser nulo");
        usuarios.put(usuario.getId(), usuario);
    }

    public Optional<Usuario> obter(String contaId) {
        notNull(contaId, "O ID do Usuario não pode ser nulo");
        return Optional.ofNullable(usuarios.get(contaId));
    }

    public boolean emailExistente(String email) {
        notNull(email, "O email não pode ser nulo");
        return usuarios.values().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public boolean usernameExistente(String username) {
        notNull(username, "O username não pode ser nulo");
        return usuarios.values().stream().anyMatch(u -> u.getUsername().equals(username));
    }
}
