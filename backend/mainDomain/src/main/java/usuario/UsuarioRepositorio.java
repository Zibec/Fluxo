package usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.notNull;

public interface UsuarioRepositorio {

     void salvarUsuario(Usuario usuario);

     Optional<Usuario> obterUsuario(String contaId);

     boolean emailExistente(String email);

     boolean usernameExistente(String username);
}
