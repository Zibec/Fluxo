package categoria;

import static org.apache.commons.lang3.Validate.notBlank;

public class Categoria {

    private final String id;
    private String nome;

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
}

