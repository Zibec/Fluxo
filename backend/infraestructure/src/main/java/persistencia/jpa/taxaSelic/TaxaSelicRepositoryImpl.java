package persistencia.jpa.taxaSelic;

import org.postgresql.shaded.com.ongres.saslprep.SASLprep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import persistencia.jpa.Mapper;
import taxaSelic.TaxaSelic;
import taxaSelic.TaxaSelicRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class TaxaSelicRepositoryImpl implements TaxaSelicRepository {

    @Autowired
    private TaxaSelicRepositoryJpa taxaSelicRepositoryJpa;

    @Autowired
    private Mapper mapper;

    @Override
    public void salvar(TaxaSelic taxaSelic) {
        if (taxaSelic != null){
            var taxaSelicJpa = mapper.map(taxaSelic, TaxaSelicJpa.class);
            taxaSelicRepositoryJpa.save(taxaSelicJpa);
        }
    }

    @Override
    public TaxaSelic obterTaxaSelic() {
        TaxaSelicJpa selicJpa = taxaSelicRepositoryJpa.findAll().getFirst();
        if (selicJpa != null){
            var taxaSelic = mapper.map(selicJpa, TaxaSelic.class);
            return taxaSelic;
        }
        return null;
    }
}
