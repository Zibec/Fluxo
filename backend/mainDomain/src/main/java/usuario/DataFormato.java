package usuario;

public enum DataFormato {
    MMDDYYYY("MM-dd-yyyy"),
    DDMMYYYY("dd-MM-yyyy"),
    YYYYMMDD("yyyy-MM-dd");

    private final String formato;

    DataFormato(String formato) {
        this.formato = formato;
    }

    public String getFormato() {
        return formato;
    }
}
