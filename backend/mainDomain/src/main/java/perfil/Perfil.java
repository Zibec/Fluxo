package perfil;

import java.util.UUID;

public class Perfil {

    private final String id;
    private String nome;

    private String usuarioId;

    public Perfil() {
        this.id = UUID.randomUUID().toString();
    }

    public Perfil(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}