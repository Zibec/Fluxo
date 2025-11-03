package persistencia.jpa.usuario;

import jakarta.persistence.*;
import persistencia.jpa.Mapper;
import usuario.DataFormato;
import usuario.Email;
import usuario.Moeda;

import java.security.Provider;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.isTrue;

@Entity
@Table(name = "USUARIO")
public class UsuarioJpa {
    @Id
    public String id;
    public String username;
    public String password;

    public String userEmail;

    public String formatoDataPreferido;

    public String moedaPreferida;
}
