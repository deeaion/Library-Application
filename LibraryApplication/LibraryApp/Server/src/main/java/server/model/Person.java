package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "person")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "person_id"))
})
//@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends Identifiable<Long>{


    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birthday", nullable = false)
    private LocalDateTime birthDay;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "cpn", nullable = false, unique = true)
    private String cpn;

    public Person() {}

    public Person(String firstName, String lastName, LocalDateTime birthDay, String gender, String address, String phone, String cpn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.cpn = cpn;
    }
}
