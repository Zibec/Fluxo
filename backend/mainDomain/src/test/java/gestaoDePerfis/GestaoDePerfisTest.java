package gestaoDePerfis;

import historicoInvestimento.HistoricoInvestimento;
import historicoInvestimento.HistoricoInvestimentoRepositorio;
import historicoInvestimento.HistoricoInvestimentoService;
import investimento.Investimento;
import investimento.InvestimentoRepositorio;
import investimento.InvestimentoService;
import io.cucumber.java.en.*;
import jobScheduler.JobScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import perfil.Perfil;
import perfil.PerfilRepository;
import selicApiClient.SelicApiClient;
import taxaSelic.TaxaSelicRepository;
import taxaSelic.TaxaSelicService;
import transacao.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.rmi.server.ExportException;
import java.time.LocalDate;
import java.util.*;

public class GestaoDePerfisTest {

    private static final Log log = LogFactory.getLog(GestaoDePerfisTest.class);
    private Perfil perfil;
    private PerfilRepository perfilRepository = new PerfilRepository();
    private Transacao transacao;
    private TransacaoRepositorio transacaoRepositorio = new InMemoryTransacaoRepositorio();
    private TransacaoService transacaoService = new TransacaoService(transacaoRepositorio);
    private Exception excecaoCapturada = null;
    //Regra de negócio: Ao criar uma nova despesa, deve ser obrigatório selecionar qual perfil realizou o gasto. A transação no banco deve conter referência ao perfil que a realizou.

    //Cenário: Registrar gasto associado a um perfil com sucesso

    @Given("que existe um perfil {string}")
    public void criarPerfil(String nome){
        perfil = new Perfil("0", nome);
        perfilRepository.salvar(perfil);
    }

    @When("eu registro uma nova despesa de {double} reais para {string}, e seleciono o perfil Filho")
    public void registrarDespesa(double valor, String descricao){
        transacao = new Transacao(
                "0",
                null,
                descricao,
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.EFETIVADA,
                "0"
        );

        try {
            transacaoRepositorio.salvar(transacao);
        }catch (Exception e){
            excecaoCapturada = e;
        }
    }

    @Then("a transação deve ser registrada no sistema no perfil Filho")
    public void registroDeTransacao(){

        String perfilid = null;
        transacao = null;
        boolean transacaoRegistradaComPerfil = false;
        for (Perfil per : perfilRepository.obterTodos()){
            if (per.getNome().equals("Filho")){
                perfilid = per.getId();
            }
        }

        assertNotNull(perfilid);

        for (Transacao tr : transacaoRepositorio.listarTodas()) {
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
        perfilRepository.salvar(new Perfil("0", "Pai"));
        perfilRepository.salvar(new Perfil("1", "Mãe"));
        perfilRepository.salvar(new Perfil("2", "Filho"));
    }

    @When("eu registro uma nova despesa de {double} reais para {string}, mas não seleciono nenhum perfil")
    public void registroSemPefil(double valor, String descricao){
        transacao = new Transacao(
                "0",
                null,
                descricao,
                new BigDecimal(valor),
                LocalDate.now(),
                StatusTransacao.EFETIVADA,
                null
        );

        try {
            transacaoRepositorio.salvar(transacao);
        }catch (Exception e){
            excecaoCapturada = e;
        }

    }

    @Then("o sistema deve impedir o registro da transacao no sistema")
    public void impedirResistro(){
        boolean repoVazio = transacaoRepositorio.listarTodas().isEmpty();
        assertTrue(repoVazio);
    }

    @And("deve exibir uma mensagem informando que é obrigatório a seleção de um perfil")
    public void exibirMensagem(){
        assertNotNull(excecaoCapturada);
        assertTrue(excecaoCapturada.getMessage().contains("É obrigatório a seleção de um perfil."));
    }
}