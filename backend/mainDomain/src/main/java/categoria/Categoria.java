package categoria;

import java.util.UUID;

import static org.apache.commons.lang3.Validate.notBlank;

public class Categoria {

    private String id;
    private String nome;
    private String usuarioId;

    public Categoria() {
        this.id = UUID.randomUUID().toString();
    }

    public Categoria(String id,String nome) {
        notBlank(nome, "O nome da categoria não pode ser vazio.");
        this.id = id;
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        notBlank(nome, "O nome da categoria não pode ser vazio.");
        this.nome = nome;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getUsuarioId(){
        return this.usuarioId;
    }
    public void setUsuarioId(String usuarioId){
        this.usuarioId = usuarioId;
    }
}

