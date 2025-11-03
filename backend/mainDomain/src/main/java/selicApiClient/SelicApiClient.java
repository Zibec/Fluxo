package selicApiClient;

public class SelicApiClient {
    private boolean status = true;

    public Double buscarTaxaSelicDiaria() {
        if (status){
            return 0.01;
        }
        else{
            throw new RuntimeException("Falha em buscar a taxa selic.");
        }

    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public SelicApiClient(boolean status) {
        this.status = status;
    }
}
