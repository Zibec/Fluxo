package taxaSelic;

import java.math.BigDecimal;

public class TaxaSelicRepository {
    private TaxaSelic taxaSelic;

    public void salvar(TaxaSelic taxaSelic){
        this.taxaSelic = taxaSelic;
    }

    public TaxaSelic obter(){
        return taxaSelic;
    }
}
