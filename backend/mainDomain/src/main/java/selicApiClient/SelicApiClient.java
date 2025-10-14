package selicApiClient;

public class SelicApiClient {
    // Em produção: chamada HTTP ao Banco Central
    // Aqui simulamos a resposta

    private boolean status = true;

    public Double buscarTaxaSelicDiaria() {
        if (status){
            return 0.01; // 1% por exemplo
        }
        else{
            throw new RuntimeException("Falha em buscar a taxa selic.");
        }

    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
