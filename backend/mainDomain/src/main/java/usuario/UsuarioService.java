package usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

public class UsuarioService {
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public void salvar(Usuario usuario) {
        if(usuario.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Usuário precisa de um nome");
        }

        if(usuario.getEmail().getEndereco().isEmpty()) {
            throw new IllegalArgumentException("Usuário precisa de um email");
        }

        if(usuarioRepositorio.usernameExistente(usuario.getUsername())) {
            throw new IllegalArgumentException("Usuario esse username já existe");
        }

        if(usuarioRepositorio.emailExistente(usuario.getEmail().getEndereco())) {
            throw new IllegalArgumentException("Usuario com esse email já existe");
        }

        usuarioRepositorio.salvarUsuario(usuario);
    }

    public Usuario obter(String usuarioId) {
        return usuarioRepositorio.obterUsuario(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public Usuario obterPorNome(String username) {
        return usuarioRepositorio.obterPorNome(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public void deletar(String usuarioId) {
        usuarioRepositorio.deletarUsuario(usuarioId);
    }

    public boolean emailExistente(String email) {
        return usuarioRepositorio.emailExistente(email);
    }

    public boolean usernameExistente(String username) {
        return usuarioRepositorio.usernameExistente(username);
    }

    public void changeUsername(String usuarioId, String newUsername, String password) {
        Usuario usuario = obter(usuarioId);

        if (!usuario.getPassword().equals(password)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (usernameExistente(newUsername)) {
            throw new IllegalArgumentException("Nome de usuário já está em uso");
        }
        usuario.setUsername(newUsername);
        usuarioRepositorio.deletarUsuario(usuarioId);
        salvar(usuario);
    }

    public void changeEmail(Usuario usuario, Email oldEmail, String newEmail, String password) {
        if (!usuario.getPassword().equals(password)) {
            throw new SecurityException("Senha incorreta");
        }
        if (emailExistente(newEmail)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        if (!usuario.getEmail().getEndereco().equals(oldEmail.getEndereco())) {
            throw new IllegalArgumentException("O e-mail não corresponde ao e-mail atual");
        }
        if (usuario.getEmail().verifyEmail(newEmail)) {
            usuario.setEmail(new Email(newEmail));
        } else {
            throw new IllegalArgumentException("Formato de e-mail inválido");
        }

        usuarioRepositorio.atualizarUsuario(usuario);
    }

    public void changePassword(Usuario usuario, String oldPassword, String newPassword) {
        BCryptPasswordEncoder passwordEncoder  = new BCryptPasswordEncoder ();

        if (!passwordEncoder.matches(oldPassword, usuario.getPassword())) {
            throw new SecurityException("Senha incorreta");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
    }
}
