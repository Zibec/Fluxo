package cartao;

public class CartaoNumero {
    private String codigo;

    public CartaoNumero(String codigo) {
        /*if (!isValid(codigo)) {
            throw new IllegalArgumentException("Código inválido");
        }*/
        this.codigo = codigo;
    }

    private boolean isValid(String codigo) {
        return codigo != null && codigo.matches("(\\b[4|5|6]\\d{3}[\\s-]?(\\d{4}[\\s-]?){2}\\d{1,4}\\b)|(\\b\\d{4}[\\s-]?\\d{6}[\\s-]?\\d{5}\\b)");
    }
}
