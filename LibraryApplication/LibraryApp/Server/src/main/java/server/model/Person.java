package server.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)


public class Person extends Identifiable<Long> {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birthday")
    private LocalDateTime birthDay;
    @Column(name = "gender")
    private String gender;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "cpn")
    private String cpn;

    public Person(String firstName, String lastName, LocalDateTime birthDay, String gender, String address, String phone, String cpn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.cpn = cpn;
    }

    public Person() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDay(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCpn(String cpn) {
        this.cpn = cpn;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDay=" + birthDay +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", cpn='" + cpn + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return Objects.equals(getFirstName(), person.getFirstName()) && Objects.equals(getLastName(), person.getLastName()) && Objects.equals(getBirthDay(), person.getBirthDay()) && Objects.equals(getGender(), person.getGender()) && Objects.equals(getAddress(), person.getAddress()) && Objects.equals(getPhone(), person.getPhone()) && Objects.equals(getCpn(), person.getCpn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getBirthDay(), getGender(), getAddress(), getPhone(), getCpn());
    }
}
