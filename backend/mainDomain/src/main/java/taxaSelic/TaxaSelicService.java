package taxaSelic;

import generics.Observer;
import selicApiClient.SelicApiClient;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TaxaSelicService {
    // Fonte do observador que gera os eventos e possuia lista de observadores
    private SelicApiClient selicApiClient;
    private TaxaSelicRepository taxaSelicRepository;

    private ArrayList<Observer> observers = new ArrayList<>();

    public void atualizarTaxaSelic(){

        BigDecimal valor = new BigDecimal(selicApiClient.buscarTaxaSelicDiaria());

        TaxaSelic novaTaxaSelic = new TaxaSelic(valor);

        taxaSelicRepository.salvar(novaTaxaSelic);

        notifyObservers();

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

    public void attach(Observer o){
        observers.add(o);
    }

    private void notifyObservers(){
        for (Observer o : observers){
            o.update();
        }
    }
}
