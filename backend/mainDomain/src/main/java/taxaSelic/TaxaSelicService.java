package taxaSelic;

import selicApiClient.SelicApiClient;

import java.math.BigDecimal;

public class TaxaSelicService {

    private SelicApiClient selicApiClient;
    private TaxaSelicRepository taxaSelicRepository;

    public void atualizarTaxaSelic(){

        BigDecimal valor = new BigDecimal(selicApiClient.buscarTaxaSelicDiaria());

        TaxaSelic novaTaxaSelic = new TaxaSelic(valor);

        taxaSelicRepository.salvar(novaTaxaSelic);

    }

    public TaxaSelicService(SelicApiClient selicApiClient, TaxaSelicRepository taxaSelicRepository) {
        this.selicApiClient = selicApiClient;
        this.taxaSelicRepository = taxaSelicRepository;
    }
}
