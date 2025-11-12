package taxaSelic;

import selicApiClient.SelicApiClient;

import java.math.BigDecimal;

public class TaxaSelicService {
    // Fonte do observador que gera os eventos e possuia lista de observadores
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

    public void salvar(TaxaSelic taxaSelic){
        taxaSelicRepository.salvar(taxaSelic);
    }

    public TaxaSelic obterTaxaSelic(){
        return taxaSelicRepository.obterTaxaSelic();
    }
}
