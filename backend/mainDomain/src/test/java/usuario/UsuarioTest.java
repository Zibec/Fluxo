package usuario;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {
    private Usuario usuario;
    private final UsuarioService service = new UsuarioService(new UsuarioRepositorio());

    private String senha;
    private String novaSenha;

    @And("acesso a página de configurações")
    public void acessoAPaginaDeConfiguracoes() {
        // assume que o usuário está na página
    }

    @When("eu altero meu e-mail para {string}")
    public void euAlteroMeuEMailPara(String novoEmail) {
        usuario = new Usuario("usuarioTeste", "antigo_email@teste.com", "SenhaSegura123");
        usuario.changeEmail("antigo_email@teste.com", novoEmail, service);
        assertEquals(novoEmail, usuario.getEmail());
    }

    @And("o e-mail não está em uso por outro usuário")
    public void oEMailNaoEstaEmUsoPorOutroUsuario() {
        boolean emailExistente = service.emailExistente(usuario.getEmail());
        assertFalse(emailExistente);
    }

    @Then("o sistema deve salvar o novo e-mail")
    public void oSistemaDeveSalvarONovoEMail() {
        service.salvar(usuario);
        assertEquals(usuario.getEmail(), service.obter(usuario.getId()).getEmail());
    }

    @And("acesso a página de configurações de cartao")
    public void acessoAPaginaDeConfiguracoesDeCartao() {
        // assume que o usuário está na página
    }

    @And("o e-mail já está em uso por outro usuário")
    public void oEMailJaEstaEmUsoPorOutroUsuario() {
        Usuario outroUsuario = new Usuario("outroUsuario", "email_existente@teste.com", "OutraSenha123");
        service.salvar(outroUsuario);
    }

    @Then("o sistema deve recusar a alteração")
    public void oSistemaDeveRecusarAAlteracao() {
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.changeEmail("antigo_email@teste.com", "email_existente@teste.com", service);
        });
    }

    @When("eu informo minha senha atual {string}")
    public void euInformoMinhaSenhaAtual(String senhaAtual) {
        this.senha = senhaAtual;
        usuario = new Usuario("usuarioTeste", "email_teste@teste.com", senhaAtual);
    }

    @And("informo a nova senha {string}")
    public void informoANovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    @Then("o sistema deve validar a senha atual")
    public void oSistemaDeveValidarASenhaAtual() {
        assertTrue(usuario.changePassword(this.senha,this.novaSenha));
    }

    @When("eu informo uma senha atual incorreta {string}")
    public void euInformoUmaSenhaAtualIncorreta(String senhaIncorreta) {
        usuario = new Usuario("usuarioTeste", "teste@teste.com", "SenhaCorreta123");
        this.senha = senhaIncorreta;
    }

    @And("tento cadastrar a nova senha {string}")
    public void tentoCadastrarANovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.changePassword(this.senha,this.novaSenha);
        });
    }

    @When("eu seleciono a moeda {string}")
    public void euSelecionoAMoeda(String moeda) {
        usuario = new Usuario("usuarioTeste", "teste@teste.com", "SenhaCorreta123");
        usuario.setMoedaPreferida(moeda);
    }

    @And("seleciono o formato de data {string}")
    public void selecionoOFormatoDeData(String formato) {
        usuario.setFormatoDataPreferido(formato);
    }

    @Then("o sistema deve salvar as preferências")
    public void oSistemaDeveSalvarAsPreferencias() {
        assertEquals(Moeda.USD, usuario.getMoedaPreferida());
        assertEquals(DataFormato.MMDDYYYY, usuario.getFormatoDataPreferido());
    }
}
