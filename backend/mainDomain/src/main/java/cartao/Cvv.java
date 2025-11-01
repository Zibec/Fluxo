package cartao;

public class Cvv {
    private String codigo;

    public Cvv() {}

    public Cvv(String codigo) {
        if (!isValid(codigo)) {
            throw new IllegalArgumentException("Código inválido");
        }
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    private boolean isValid(String codigo) {
        return codigo != null && codigo.matches("^\\d{3,4}$");
    }
}
