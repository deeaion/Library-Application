package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import server.model.Credentials;
import server.model.Person;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity

@Table(name = "librarian")
//@PrimaryKeyJoinColumn(name = "id")
public class Librarian extends Person {
    @OneToOne
    @JoinColumn(name = "credentials_id", referencedColumnName = "user_id")
    private Credentials credentials;

    @Column(name = "date_of_employment")
    private LocalDateTime hireDate;

    @Column(name = "unique_code", unique = true, length = 50)
    private String uniqueCode;

    // Getters and setters

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
