package categoria;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import perfil.Perfil;
import perfil.PerfilRepository;
import transacao.StatusTransacao;
import transacao.Tipo;
import transacao.Transacao;
import transacao.TransacaoRepositorio;
import conta.Conta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    private CategoriaRepositorio categoriaRepositorio;
    private TransacaoRepositorio transacaoRepositorio;
    private CategoriaService categoriaService;

    private Categoria categoria;
    private Exception excecaoCapturada;
    private int contagemInicialDeCategorias;

    private Perfil perfil = new Perfil("0", "Pai");
    private PerfilRepository perfilRepository = new PerfilRepository();

    @Before
    public void setup() {
        this.categoriaRepositorio = new CategoriaRepositorio();
        this.transacaoRepositorio = new TransacaoRepositorio();
        this.categoriaService = new CategoriaService(this.categoriaRepositorio, this.transacaoRepositorio);
        this.excecaoCapturada = null;
        this.categoria = null;
        this.contagemInicialDeCategorias = 0;
        perfilRepository.salvar(perfil);
    }

    // Cenário: Adicionar uma nova categoria que não existe
    @Given("que não existe uma categoria chamada {string} no sistema")
    public void queNaoExisteUmaCategoriaChamada(String nome) {
        assertFalse(categoriaRepositorio.obterPorNome(nome).isPresent());
    }

    @When("o usuário insere o nome {string} e salva")
    public void oUsuarioInsereONomeESalva(String nome) {
        String id = UUID.randomUUID().toString();
        this.categoria = new Categoria(id, nome);
        try {
            categoriaService.salvar(this.categoria);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Then("a categoria {string} deve aparecer na lista de categorias")
    public void aCategoriaDeveAparecerNaLista(String nome) {
        assertTrue(categoriaRepositorio.obterPorNome(nome).isPresent());
    }

    // --- Cenários de Categoria Existente ---

    @Given("que a categoria {string} já existe na lista")
    public void queACategoriaJaExisteNaLista(String nome) {
        String id = UUID.randomUUID().toString();
        this.categoria = new Categoria(id, nome);
        categoriaRepositorio.salvar(this.categoria);
        this.contagemInicialDeCategorias = categoriaRepositorio.contagem(); // Salva a contagem inicial
    }

    @When("o usuário tenta criar uma nova categoria com o nome {string}")
    public void oUsuarioTentaCriarUmaNovaCategoriaComONome(String nome) {
        try {
            // Tenta salvar uma nova categoria com o mesmo nome
            categoriaService.salvar(new Categoria(UUID.randomUUID().toString(), nome));
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Then("o sistema deve exibir uma mensagem de erro {string}")
    public void oSistemaDeveExibirUmaMensagemDeErro(String mensagemDeErro) {
        assertNotNull(excecaoCapturada, "Uma exceção era esperada, mas não ocorreu.");
        assertEquals(mensagemDeErro, excecaoCapturada.getMessage());
    }

    @And("a lista de categorias não deve ser alterada")
    public void aListaDeCategoriasNaoDeveSerAlterada() {
        assertEquals(this.contagemInicialDeCategorias, categoriaRepositorio.contagem());
    }

    // --- Cenários de Deleção ---

    @And("não existe nenhuma transação associada à categoria {string}")
    public void naoExisteNenhumaTransacaoAssociadaACategoria(String nomeCategoria) {
        assertFalse(transacaoRepositorio.existePorCategoriaId(this.categoria.getId()));
    }

    @And("existe pelo menos uma transação associada à categoria {string}")
    public void existePeloMenosUmaTransacaoAssociadaACategoria(String nomeCategoria) {
        Conta contaDeTeste = new Conta("conta-id-teste", "Conta de Teste", "Banco Teste", BigDecimal.ZERO);

        Transacao transacao = new Transacao(
                UUID.randomUUID().toString(),
                null,
                "Teste",
                BigDecimal.TEN,
                LocalDate.now(),
                StatusTransacao.EFETIVADA,
                this.categoria.getId(),
                contaDeTeste,
                false,
                Tipo.DESPESA,
                perfilRepository.obter("0").getId()
        );
        transacaoRepositorio.salvar(transacao);
    }

    @When("o usuário escolhe deletar a categoria {string}")
    public void oUsuarioEscolheDeletarACategoria(String nomeCategoria) {
        try {
            // Lógica para lidar com deleção de categoria existente ou não existente
            Optional<Categoria> catParaDeletar = categoriaRepositorio.obterPorNome(nomeCategoria);
            if (catParaDeletar.isPresent()) {
                categoriaService.deletar(catParaDeletar.get().getId());
            } else {
                // Força um erro de "não encontrada"
                categoriaService.deletar(UUID.randomUUID().toString());

            }
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Then("a categoria {string} deve ser removida da lista")
    public void aCategoriaDeveSerRemovidaDaLista(String nome) {
        assertNull(excecaoCapturada, "Uma exceção foi lançada quando não deveria: " + excecaoCapturada);
        assertFalse(categoriaRepositorio.obterPorNome(nome).isPresent(), "A categoria não foi removida.");
    }

    @And("a categoria {string} deve continuar na lista")
    public void aCategoriaDeveContinuarNaLista(String nome) {
        assertTrue(categoriaRepositorio.obterPorNome(nome).isPresent(), "A categoria foi removida, mas não deveria.");
    }
}
