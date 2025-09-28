package usuario;

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

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public boolean changeEmail(String oldEmail, String newEmail, UsuarioService service) {
        if (service.emailExistente(newEmail)) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!this.email.equals(oldEmail)) {
            throw new IllegalArgumentException("Old email does not match current email");
        }
        if (verifyEmail(newEmail)) {
            this.email = newEmail;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!this.password.verify(oldPassword)) {
            throw new IllegalArgumentException("Old password does not match current password");
        }
        this.password = new Password(newPassword);
        return true;
    }
}
