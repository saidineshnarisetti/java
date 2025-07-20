package gradle.junit.selenium;

import java.util.UUID;

public class Customer {

    private String email;
    private String password;
    private String securityAnswer;
    private String token;

    Customer(String email, String password, String securityAnswer) {
        this.email = email;
        this.password = password;
        this.securityAnswer = securityAnswer;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSecurityAnswer() {
        return this.securityAnswer;
    }

    public String getToken() {
        return this.token;
    }

    public void saveToken(String token) {
        this.token = token;
    }

    public static class Builder {
        private String email = String.format("%s@test.com", UUID.randomUUID()
                .toString().replaceAll("-", "").substring(1, 10));
        private String password = "test1234";
        private String securityAnswer = "test1234";

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSecurityAnswer(String securityAnswer) {
            this.securityAnswer = securityAnswer;
            return this;
        }

        public Customer build() {
            return new Customer(this.email, this.password, this.securityAnswer);
        }


    }

}
