package usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public interface UsuarioRepositorio {

     void salvarUsuario(Usuario usuario);

     Optional<Usuario> obterUsuario(String contaId);

    Optional<Usuario> obterUsuarioPorEmail(String contaEmail);

     void deletarUsuario(String id);

     boolean emailExistente(String email);

     boolean usernameExistente(String username);
}
