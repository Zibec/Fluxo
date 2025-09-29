package usuario;

import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.isTrue;

public class Usuario {
    private final String id;
    private String username;
    private String email;
    private Password password;

    private DataFormato formatoDataPreferido;
    private Moeda moedaPreferida;

    public Usuario(String username, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        isTrue(verifyEmail(email), "Email inválido");
        this.email = email;
        this.password = new Password(password);
        this.formatoDataPreferido = DataFormato.DDMMYYYY;
        this.moedaPreferida = Moeda.valueOf("BRL");
    }

    public boolean verifyEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean verifyPassword(String password) {
        return this.password.verify(password);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public DataFormato getFormatoDataPreferido() {
        return formatoDataPreferido;
    }

    public void setFormatoDataPreferido(String formatoDataPreferido) {
        this.formatoDataPreferido = DataFormato.valueOf(formatoDataPreferido);
    }

    public Moeda getMoedaPreferida() {
        return moedaPreferida;
    }

    public void setMoedaPreferida(String moedaPreferida) {
        this.moedaPreferida = Moeda.valueOf(moedaPreferida);
    }

    public void setUsername(String newUsername, String password, UsuarioService service) {
        if (!this.verifyPassword(password)) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (service.usernameExistente(newUsername)) {
            throw new IllegalArgumentException("Nome de usuário já está em uso");
        }
        this.username = newUsername;
    }

    public void changeEmail(String oldEmail, String newEmail, String password, UsuarioService service) {
        if (!this.verifyPassword(password)) {
            throw new IllegalArgumentException("Senha incorreta");
        }
        if (service.emailExistente(newEmail)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        if (!this.email.equals(oldEmail)) {
            throw new IllegalArgumentException("O e-mail antual não corresponde ao e-mail atual");
        }
        if (verifyEmail(newEmail)) {
            this.email = newEmail;
        } else {
            throw new IllegalArgumentException("Formato de e-mail inválido");
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!this.password.verify(oldPassword)) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        if(Objects.equals(oldPassword, newPassword)) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual");
        }
        this.password = new Password(newPassword);
    }
}
