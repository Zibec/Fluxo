package analise;

import cartao.CartaoService;
import conta.ContaService;
import generics.FormaPagamento;

import java.math.BigDecimal;

public class RelatoriosService {

    private ContaService contaService;

    private CartaoService cartaoService;

    public RelatoriosService(ContaService contaService, CartaoService cartaoService) {
        this.cartaoService = cartaoService;
        this.contaService = contaService;
    }

    public BigDecimal calcularDinheiroTotal(String userId){
        BigDecimal total = BigDecimal.ZERO;

        BigDecimal totalContas = contaService.obterPorUsuarioId(userId).stream()
                .map(FormaPagamento::getSaldo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCartoes = cartaoService.obterPorUsuarioId(userId).stream()
                .map(FormaPagamento::getSaldo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        total = total.add(totalContas);
        total = total.add(totalCartoes);

        return total;
    }
}
