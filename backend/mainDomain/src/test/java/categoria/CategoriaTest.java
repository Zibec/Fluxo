package categoria;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    private CategoriaRepositorio categoriaRepositorio = new CategoriaRepositorio();
    private CategoriaService categoriaService = new CategoriaService(categoriaRepositorio);

    private Categoria categoria;
    private Exception excecaoCapturada;

    private int contagemInicialDeCategorias;

    // cenario 1
    @Given("que não existe uma categoria chamada {string} no sistema")
    public void queNaoExisteUmaCategoriaChamada(String nome){

        Optional<Categoria>  categoriaExistente = categoriaRepositorio.obterPorNome(nome);

        assertFalse(categoriaExistente.isPresent(),
                "Pré-condição falhou: a categoria '" + nome + "' já existia no sistema antes do teste começar.");
    }

    @When("o usuário insere o nome {string} e salva")
    public void oUsuarioInsereONome(String nome){
        String id = UUID.randomUUID().toString();

        this.categoria = new Categoria(id,nome);

        try{
            categoriaService.salvar(categoria);
        } catch(Exception e){
            this.excecaoCapturada = e;
        }
    }

    @Then("a categoria {string} deve aparecer na lista de categorias")
    public void aCategoriaDeveAparecerNaLista(String nome){
        Categoria categoria = categoriaRepositorio.obter(this.categoria.getId())
                .orElseThrow(() -> new AssertionError("A meta deveria ter sido salva, mas não foi encontrada."));

        assertEquals(nome, categoria.getNome());
    }

    //cenario 2
    @Given("que a categoria {string} já existe na lista")
    public void queACategoriaJaExisteNaLista(String nome){
        String id = UUID.randomUUID().toString();
        this.categoria = new Categoria(id,nome);
        categoriaService.salvar(new Categoria(id,nome));

        this.contagemInicialDeCategorias = categoriaRepositorio.contagem();
    }

    @When("o usuário tenta criar uma nova categoria com o nome {string}")
    public void oUsuarioTentaCriarUmaNovaCategoriaComONome(String nome){
        this.categoria = new Categoria(this.categoria.getId(),nome);

        try{
            categoriaService.salvar(this.categoria);
        }  catch(Exception e){
            this.excecaoCapturada = e;
        }
    }

    @Then("o sistema deve exibir uma mensagem de erro {string}")
    public void oSistemaDeveExibirUmaMensagemDeErro(String erro){
        assertNotNull(erro, "Uma exceção era esperada, mas não ocorreu.");

        assertEquals(erro, excecaoCapturada.getMessage());
    }

    @And("a lista de categorias não deve ser alterada")
    public void aListaDeCategoriasNaoDeveSerAlterada () {
        int contagemFinalDeCategorias = categoriaRepositorio.contagem();

        assertEquals(this.contagemInicialDeCategorias, contagemFinalDeCategorias,
                "A contagem de categorias não deveria ter sido alterada.");
    }

    //cenario 3
    @When("o usuário escolhe deletar a categoria {string}")
    public void o_usuario_escolhe_deletar_a_categoria(String nomeCategoria) {
        try {
            Optional<Categoria> categoriaParaDeletar = categoriaRepositorio.obterPorNome(nomeCategoria);

            if (categoriaParaDeletar.isPresent()) {
                categoriaService.deletar(categoriaParaDeletar.get().getId());
            } else {
                String idInexistente = UUID.randomUUID().toString();
                categoriaService.deletar(idInexistente);
            }
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Then("a categoria {string} deve ser removida da lista")
    public void aCategoriaDeveSerRemovidaDaLista(String nome){
        Optional<Categoria> resultadoBusca =  categoriaRepositorio.obterPorNome(nome);
        assertTrue(resultadoBusca.isEmpty(), "A categoria não foi excluída corretamente e ainda foi encontrada no sistema.");
    }

}
