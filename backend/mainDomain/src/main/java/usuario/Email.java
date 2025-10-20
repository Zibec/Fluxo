package usuario;

public class Email {
    private final String userEmail;

    public Email(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEndereco() {
        return userEmail;
    }

    public boolean verifyEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

}
