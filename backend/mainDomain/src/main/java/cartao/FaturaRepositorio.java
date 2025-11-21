package cartao;

public interface FaturaRepositorio {
    void salvarFatura(Fatura fatura);

    Fatura obterFatura(String id);

    void deletarFatura(String id);
}
