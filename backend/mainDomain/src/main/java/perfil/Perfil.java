package perfil;

public class Perfil {

    private String id;
    private String nome;

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

}