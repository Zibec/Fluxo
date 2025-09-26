package cartao;

public class Cvv {
    private String codigo;

    public Cvv(String codigo) {
        if (!isValid(codigo)) {
            throw new IllegalArgumentException("Código inválido");
        }
        this.codigo = codigo;
    }

    private boolean isValid(String codigo) {
        return codigo != null && codigo.matches("^\\d{3,4}$");
    }
}
