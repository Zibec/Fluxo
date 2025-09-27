package orcamento;

import java.time.YearMonth;
import java.util.Objects;

public class OrcamentoChave {

    private final String usuarioId;
    private final YearMonth anoMes;
    private final String categoriaId;

    public OrcamentoChave(String usuarioId, YearMonth anoMes, String categoriaId) {
        this.usuarioId = usuarioId;
        this.anoMes = anoMes;
        this.categoriaId = categoriaId;
    }

    public YearMonth getAnoMes() {
        return anoMes;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    @Override public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof OrcamentoChave)) return false;
        OrcamentoChave that = (OrcamentoChave) o;
        return usuarioId.equals(that.usuarioId)
                && anoMes.equals(that.anoMes)
                && categoriaId.equals(that.categoriaId);
    }

    @Override public int hashCode(){
        return Objects.hash(usuarioId, anoMes, categoriaId);
    }

}
