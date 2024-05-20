package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="usercredentials")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_id"))
})
public class Credentials extends Identifiable<Long>{
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="email")
    private String email;
    @Column(name="seed")
    private String seed;

    public Credentials(String username, String password, String email, String seed) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.seed = seed;
    }

    public Credentials() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }
}
