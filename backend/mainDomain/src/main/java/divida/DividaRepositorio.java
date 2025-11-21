package divida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public interface DividaRepositorio {

    void salvar(Divida divida);

    List<Divida> obterTodosDivida();

    void limparDivida();

    List<Divida> obterDividaPorUsuarioId(String usuarioId);
}
