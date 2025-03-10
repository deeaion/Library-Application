package common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="usercredentials")
public class Credentials extends Identifiable<Long>{
    private String username;
    private String password;
    private String email;
    private String seed;

    public Credentials(String username, String password, String email, String seed) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.seed = seed;
    }

    public Credentials() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }
}
