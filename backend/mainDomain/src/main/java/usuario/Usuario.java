package usuario;

import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.isTrue;

public class Usuario {
    private final String id;
    private String username;
    private Email userEmail;
    private Password password;

    private DataFormato formatoDataPreferido;
    private Moeda moedaPreferida;

    public Usuario(String username, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.userEmail = new Email(email);
        isTrue(userEmail.verifyEmail(email), "Email inválido");
        this.password = new Password(password);
        this.formatoDataPreferido = DataFormato.DDMMYYYY;
        this.moedaPreferida = Moeda.valueOf("BRL");
    }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public Email getEmail() {
        return userEmail;
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
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }
    public Moeda getMoedaPreferida() {
        return moedaPreferida;
    }
    public void setMoedaPreferida(String moedaPreferida) {
        this.moedaPreferida = Moeda.valueOf(moedaPreferida);
    }
    public void setEmail(Email newEmail) {
        this.userEmail = newEmail;
    }
    public void setPassword(Password newPassword) {
        this.password = newPassword;
    }
}
