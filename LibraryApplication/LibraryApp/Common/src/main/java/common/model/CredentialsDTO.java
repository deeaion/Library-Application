package common.model;

import java.util.Objects;

public class CredentialsDTO extends Credentials {
    private String type="";
    public CredentialsDTO(Credentials credentials, String type) {
        super(credentials.getUsername(), credentials.getPassword(), credentials.getEmail(), credentials.getSeed());
        this.type = type;
        this.setId(credentials.getId());
    }

    public CredentialsDTO(String username, String password, String email, String seed, String type) {
        super(username, password, email, seed);
        this.type = type;
    }

    public CredentialsDTO(Credentials credentials) {
        super(credentials.getUsername(), credentials.getPassword(), credentials.getEmail(), credentials.getSeed());

    }

    public String getType() {
        return type;
    }

    public CredentialsDTO() {
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CredentialsDTO that)) return false;
        return Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    @Override
    public String toString() {
        return "CredentialsDTO{" +
                "type='" + type + '\'' +
                "} " + super.toString();
    }
}
