package usuario;

public enum Moeda {
    BRL("BRL"),
    USD("USD"),
    EUR("EUR");

    private final String codigo;

    Moeda(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
