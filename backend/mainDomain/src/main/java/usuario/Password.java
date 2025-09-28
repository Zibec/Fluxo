package usuario;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Password {
    private byte[] passwordHash;
    private byte[] salt;
    private static final SecureRandom random = new SecureRandom();

    public Password(String password) {
        this.salt = new byte[16];
        random.nextBytes(salt);
        this.passwordHash = hash(password, salt);
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    private byte[] hash(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }

    public boolean verify(String inputPassword) {
        byte[] inputHash = hash(inputPassword, this.salt);
        return Arrays.equals(this.passwordHash, inputHash);
    }
}

