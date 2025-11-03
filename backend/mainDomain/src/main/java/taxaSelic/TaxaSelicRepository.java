package taxaSelic;

import java.math.BigDecimal;

public interface TaxaSelicRepository {
    void salvar(TaxaSelic taxaSelic);

    TaxaSelic obterTaxaSelic();
}
