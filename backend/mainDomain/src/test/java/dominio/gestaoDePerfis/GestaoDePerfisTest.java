package dominio.gestaoDePerfis;

import cartao.Cartao;
import cartao.CartaoNumero;
import cartao.CartaoRepositorio;
import conta.Conta;
import conta.ContaRepositorio;
import infraestrutura.persistencia.memoria.Repositorio;
import io.cucumber.java.en.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import perfil.Perfil;
import perfil.PerfilRepository;
import perfil.PerfilService;
import transacao.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GestaoDePerfisTest {

    private static final Log log = LogFactory.getLog(GestaoDePerfisTest.class);
    private Perfil perfil;
    private PerfilRepository perfilRepository = new Repositorio();
    private Transacao transacao;
    private TransacaoRepositorio transacaoRepositorio = new Repositorio();
    private Exception excecaoCapturada = null;
    private Conta contaDeTeste;
    private ContaRepositorio contaRepositorio = new Repositorio();
    private PerfilService perfilService = new PerfilService(perfilRepository);
    private CartaoRepositorio cartaoRepositorio = new Repositorio();
    private TransacaoService transacaoService = new TransacaoService(transacaoRepositorio, contaRepositorio, cartaoRepositorio, new Repositorio());
    //Regra de negócio: Ao criar uma nova despesa, deve ser obrigatório selecionar qual perfil realizou o gasto. A transação no banco deve conter referência ao perfil que a realizou.

    //Cenário: Registrar gasto associado a um perfil com sucesso

    @Given("que existe um perfil {string}")
    public void criarPerfil(String nome){
        perfil = new Perfil("0", nome);
        perfilService.salvarPerfil(perfil);
        contaDeTeste = new Conta("conta-teste-id", "Conta de Teste", "Banco Teste", BigDecimal.ZERO);

        contaRepositorio.salvar(contaDeTeste);
        transacaoRepositorio = new Repositorio();

    }

    @When("eu registro uma nova despesa de {double} reais para {string}, e seleciono o perfil Filho")
    public void registrarDespesa(double valor, String descricao){
        transacao = new Transacao(
                "0",
                null,
                descricao,
                new BigDecimal(valor),
                LocalDateTime.now(),
                StatusTransacao.EFETIVADA,
                contaRepositorio.obterConta(contaDeTeste.getId().getId()).get().getId(),
                true,
                Tipo.DESPESA,
                "0"
        );

        try {
            transacaoService.salvarTransacao(transacao);
        }catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("a transação deve ser registrada no sistema no perfil Filho")
    public void registroDeTransacao(){

        String perfilid = null;
        transacao = null;
        for (Perfil per : perfilService.obterTodosPerfis()){
            if (per.getNome().equals("Filho")){
                perfilid = per.getId();
            }
        }

        assertNotNull(perfilid);

        for (Transacao tr : transacaoService.listarTodasTransacoes()) {
            if (tr.getPerfilId().equals(perfilid)){
                transacao = tr;
            }
        }

        assertNotNull(transacao);
        assertEquals(transacao.getPerfilId(),perfilid);

    }

    //Cenário: Tentativa de registrar gasto sem selecionar perfil

    @Given("que existem perfis cadastrados")
    public void criarPerfis(){
        perfilService.salvarPerfil(new Perfil("0", "Pai"));
        perfilService.salvarPerfil(new Perfil("1", "Mãe"));
        perfilService.salvarPerfil(new Perfil("2", "Filho"));
        contaDeTeste = new Conta("conta-teste-id", "Conta de Teste", "Banco Teste", BigDecimal.ZERO);
        contaRepositorio.salvar(contaDeTeste);
    }

    @When("eu registro uma nova despesa de {double} reais para {string}, mas não seleciono nenhum perfil")
    public void registroSemPefil(double valor, String descricao){
        transacao = new Transacao(
                "0",
                null,
                descricao,
                new BigDecimal(valor),
                LocalDateTime.now(),
                StatusTransacao.EFETIVADA,
                contaRepositorio.obterConta(contaDeTeste.getId().getId()).get().getId(),
                true,
                Tipo.DESPESA,
                null
        );

        try {
            transacaoService.salvarTransacao(transacao);
        }catch (Exception e){
            excecaoCapturada = e;
        }

    }

    @Then("o sistema deve impedir o registro da transacao no sistema")
    public void impedirResistro(){
        boolean repoVazio = transacaoService.listarTodasTransacoes().isEmpty();
        assertTrue(repoVazio);
    }

    @And("deve exibir uma mensagem informando que é obrigatório a seleção de um perfil")
    public void exibirMensagem(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("É obrigatório a seleção de um perfil."));
    }
}