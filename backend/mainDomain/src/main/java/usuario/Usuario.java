package usuario;

import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

import static org.apache.commons.lang3.Validate.isTrue;

public class Usuario {
    private String id;
    private String username;
    private Email userEmail;
    private String password;
    private DataFormato formatoDataPreferido;
    private Moeda moedaPreferida;

    public Usuario() {
        this.id = UUID.randomUUID().toString();
    }

    public Usuario(String username, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.userEmail = new Email(email);
        isTrue(userEmail.verifyEmail(email), "Email inválido");
        this.password = password;
        this.formatoDataPreferido = DataFormato.DDMMYYYY;
        this.moedaPreferida = Moeda.valueOf("BRL");
    }

    public Usuario(String id, String username, String email, String password, DataFormato formatoDataPreferido, Moeda moedaPreferida) {
        this.id = id;
        this.username = username;
        this.userEmail = new Email(email);
        this.password = password;
        this.formatoDataPreferido = formatoDataPreferido;
        this.moedaPreferida = moedaPreferida;
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
    public String getPassword() {
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
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    public void setId(String id) {
        this.id = id;
    }
}
