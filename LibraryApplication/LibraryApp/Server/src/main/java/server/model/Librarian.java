package server.model;

import jakarta.persistence.*;
import server.model.Credentials;
import server.model.Person;

import java.time.LocalDateTime;

@Entity
@Table(name = "librarian")
public class Librarian extends Person {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private Credentials credentials;

    @Column(name = "date_of_employment")
    private LocalDateTime hireDate;

    @Column(name = "unique_code", unique = true, length = 50)
    private String uniqueCode;

    // Getters and setters

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
