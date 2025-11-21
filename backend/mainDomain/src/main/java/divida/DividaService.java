package divida;

import java.util.List;
import static org.apache.commons.lang3.Validate.notNull;

public class DividaService {
    private final DividaRepositorio dividaRepositorio;

    public DividaService(DividaRepositorio dividaRepositorio) {
        this.dividaRepositorio = dividaRepositorio;
    }

    public void salvar(Divida divida) {
        notNull(divida, "Divida não pode ser nula");
        dividaRepositorio.salvar(divida);
    }

    public List<Divida> obterTodas() {
        return dividaRepositorio.obterTodosDivida();
    }

    public void limparDivida() {
        dividaRepositorio.limparDivida();
    }

    public List<Divida> obterDividasPorUsuario(String usuarioId) {
        return dividaRepositorio.obterDividaPorUsuarioId(usuarioId);
    }

}