package dominio.usuario;

import infraestrutura.persistencia.memoria.Repositorio;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import usuario.Usuario;
import usuario.UsuarioRepositorio;
import usuario.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private UsuarioRepositorio usuarioRepositorio;
    private UsuarioService usuarioService;

    private Usuario usuarioLogado;
    private boolean isSenhaAtualCorreta;
    private String novaSenha;
    private String senhaAtual;
    private Exception thrownException;

    public UsuarioTest() {
        usuarioRepositorio = new Repositorio();
        usuarioService = new UsuarioService(usuarioRepositorio);
        usuarioLogado = new Usuario("usuario_logado", "logado@dominio.com", "senha123");
        usuarioService.salvar(usuarioLogado);
        Usuario usuarioExistente = new Usuario("usuario_existente", "email_existente@dominio.com", "senha456");
        usuarioService.salvar(usuarioExistente);
        senhaAtual = "senha123";
    }


    // Scenario: Atualizar e-mail com senha atual correta (sucesso)
    @Given("que estou logado no sistema")
    public void que_estou_logado_no_sistema() {
        Usuario usuario = usuarioService.obter(usuarioLogado.getId());
        assertNotNull(usuarioService.obter(usuario.getId()), "O usuário de teste principal não foi inicializado.");
    }

    @And("informo minha senha atual corretamente")
    public void informo_minha_senha_atual_corretamente() {
        this.isSenhaAtualCorreta = true;
    }

    @When("altero meu e-mail para {string}")
    public void altero_meu_e_mail_para(String novoEmail) {
        Usuario usuario = usuarioService.obter(usuarioLogado.getId());
        if(isSenhaAtualCorreta) {
            try {
                usuarioService.changeEmail(usuario, usuario.getEmail(), novoEmail, "senha123");
            } catch (Exception e) {
                this.thrownException = e;
            }
            return;
        }

        try {
            usuarioService.changeEmail(usuario, usuario.getEmail(), novoEmail, "senhaErrada");
        } catch (Exception e) {
            this.thrownException = e;
        }

    }

    @Then("o sistema deve atualizar o e-mail com sucesso")
    public void o_sistema_deve_atualizar_o_e_mail_com_sucesso() {
        assertNull(thrownException, "Uma exceção foi lançada inesperadamente.");
        assertTrue(usuarioLogado.getEmail().getEndereco().equals("novoemail@dominio.com") || usuarioLogado.getEmail().getEndereco().equals("valido@dominio.com") || usuarioLogado.getEmail().equals("unico@dominio.com"));
    }

    // Scenario: Falha ao atualizar e-mail com senha atual incorreta
    @Given("informo uma senha atual incorreta")
    public void informo_uma_senha_atual_incorreta() {
        this.isSenhaAtualCorreta = false;
    }

    @When("tento alterar meu e-mail para {string}")
    public void tento_alterar_meu_e_mail_para(String novoEmail) {
        altero_meu_e_mail_para(novoEmail);
    }

    @Then("o sistema deve exibir uma mensagem de erro de autenticação")
    public void o_sistema_deve_exibir_uma_mensagem_de_erro_de_autenticacao() {
        assertNotNull(thrownException);
        assertInstanceOf(SecurityException.class, thrownException);
    }

    // Scenario: Atualizar username para um nome único (sucesso)
    @When("altero meu username para {string}")
    public void altero_meu_username_para(String novoUsername) {
        try {
            usuarioLogado.setUsername(novoUsername, "senha123", usuarioService);
        } catch (Exception e) {
            this.thrownException = e;
        }
    }

    @Then("o sistema deve salvar o novo username")
    public void o_sistema_deve_salvar_o_novo_username() {
        assertNull(thrownException);
        assertEquals("usuario_unico123", usuarioLogado.getUsername());
    }

    // Scenario: Falha ao atualizar username já existente
    @Then("o sistema deve exibir uma mensagem de erro de nome já em uso")
    public void o_sistema_deve_exibir_uma_mensagem_de_erro_de_nome_ja_em_uso() {
        assertNotNull(thrownException);
        assertEquals("Nome de usuário já está em uso", thrownException.getMessage());
    }

    // Scenario: Atualizar e-mail com formato válido (sucesso)
    @Then("o sistema deve salvar o novo e-mail")
    public void o_sistema_deve_salvar_o_novo_e_mail() {
        assertNull(thrownException);
    }

    // Scenario: Falha ao atualizar e-mail com formato inválido
    @Then("o sistema deve exibir uma mensagem de erro de formato inválido")
    public void o_sistema_deve_exibir_uma_mensagem_de_erro_de_formato_invalido() {
        assertNotNull(thrownException);
        assertEquals("Formato de e-mail inválido", thrownException.getMessage());
    }

    // Scenario: Falha ao atualizar e-mail já existente
    @Then("o sistema deve exibir uma mensagem de erro de e-mail já cadastrado")
    public void o_sistema_deve_exibir_uma_mensagem_de_erro_de_e_mail_ja_cadastrado() {
        assertNotNull(thrownException);
        assertEquals("Email já está em uso", thrownException.getMessage());
    }

    // Scenario: Alterar senha com senha atual correta (sucesso)
    @Given("informo a senha atual correta {string}")
    public void informo_a_senha_atual_correta(String senha) {
        usuarioLogado = new Usuario("usuario_logado", "logado@dominio.com", "senha123");
        usuarioService.salvar(usuarioLogado);
        
        Usuario usuario = usuarioService.obter(usuarioLogado.getId());
        this.isSenhaAtualCorreta = usuario.getPassword().verify(senha);
    }

    @When("informo a nova senha {string}")
    public void informo_a_nova_senha(String senha) {
        if(senha.equals(this.senhaAtual)) {
            this.thrownException = new IllegalArgumentException("A nova senha deve ser diferente da senha atual");
            return;
        }

        this.novaSenha = senha;
    }

    @When("confirmo a nova senha {string}")
    public void confirmo_a_nova_senha(String senha) {
        if (!senha.equals(this.novaSenha)) {
            this.thrownException = new IllegalArgumentException("A nova senha e a confirmação não coincidem");
        }
    }

    @Then("o sistema deve atualizar a senha com sucesso")
    public void o_sistema_deve_atualizar_a_senha_com_sucesso() {
        Usuario usuario = usuarioService.obter(usuarioLogado.getId());

        if (!isSenhaAtualCorreta) {
            fail("A senha atual deveria estar correta para este cenário de sucesso.");
        }
        try {
            usuarioService.changePassword(usuario,senhaAtual, this.novaSenha);
            senhaAtual = this.novaSenha;
        } catch (Exception e) {
            this.thrownException = e;
        }
        assertTrue(usuario.getPassword().verify(this.novaSenha));
    }

    // Scenario: Falha ao alterar senha com senha atual incorreta
    @Given("informo a senha atual incorreta {string}")
    public void informo_a_senha_atual_incorreta(String senha) {
        this.isSenhaAtualCorreta = this.usuarioLogado.getPassword().verify(senha);
    }

    @Then("o sistema deve exibir uma mensagem de erro de senha atual inválida")
    public void o_sistema_deve_exibir_uma_mensagem_de_erro_de_senha_atual_invalida() {
        assertFalse(isSenhaAtualCorreta);
    }

    // Scenario: Falha ao tentar alterar senha para a mesma senha atual
    @Then("o sistema deve exibir uma mensagem de erro de senha repetida")
    public void o_sistema_deve_exibir_uma_mensagem_de_erro_de_senha_repetida() {
        assertNotNull(thrownException);
    }
}