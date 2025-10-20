package patrimonio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.notNull;

public interface PatrimonioRepositorio {

    void salvarPatrimonio(Patrimonio snapshot);

    List<Patrimonio> obterTodosPatrimonios();

    void limparPatrimonio();
}
