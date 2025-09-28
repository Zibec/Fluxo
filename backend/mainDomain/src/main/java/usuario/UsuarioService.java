package usuario;

public class UsuarioService {
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public void salvar(Usuario usuario) {
        usuarioRepositorio.salvar(usuario);
    }

    public Usuario obter(String usuarioId) {
        return usuarioRepositorio.obter(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public boolean emailExistente(String email) {
        return usuarioRepositorio.emailExistente(email);
    }
}
