package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriber")
@Inheritance(strategy = InheritanceType.JOINED)
public class Subscriber extends Person {

    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "credentials_id", referencedColumnName = "user_id")
    private Credentials credentials;

    @Column(name = "date_of_subscription")
    private LocalDateTime dateOfSubscription;

    @Column(name = "unique_code")
    private String uniqueCode;

    @OneToMany(mappedBy = "subscriberOfBasket", fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
    private List<BasketItem> shoppingBasket = new ArrayList<>();

    @ManyToMany(cascade=CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
            name = "CurrentRentals",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id")
    )
    private List<Rental> currentRentals = new ArrayList<>();

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name = "PreviousRentals",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id")
    )
    private List<Rental> previousRentals = new ArrayList<>();

    public Subscriber() {}

    public Subscriber(String uniqueCode, Person person, Credentials credentials, LocalDateTime dateOfSubscription) {
        super(person.getFirstName(), person.getLastName(), person.getBirthDay(), person.getGender(), person.getAddress(), person.getPhone(), person.getCpn());
        this.setId(person.getId()); // Ensure the ID is set
        this.credentials = credentials;
        this.dateOfSubscription = dateOfSubscription;
        this.uniqueCode = uniqueCode;
        this.previousRentals = new ArrayList<>();
        this.currentRentals = new ArrayList<>();
        this.shoppingBasket = new ArrayList<>();
    }
    public void addCurrentRental(Rental rental) {
        if (currentRentals == null) {
            currentRentals = new ArrayList<>();
        }
        currentRentals.add(rental);
        rental.setRented_by(this.credentials);
    }
    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public LocalDateTime getDateOfSubscription() {
        return dateOfSubscription;
    }

    public void setDateOfSubscription(LocalDateTime dateOfSubscription) {
        this.dateOfSubscription = dateOfSubscription;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public List<BasketItem> getShoppingBasket() {
        return shoppingBasket;
    }

    public void setShoppingBasket(List<BasketItem> shoppingBasket) {
        this.shoppingBasket = shoppingBasket;
    }

    public List<Rental> getCurrentRentals() {
        return currentRentals;
    }

    public void setCurrentRentals(List<Rental> currentRentals) {
        this.currentRentals = currentRentals;
    }

    public List<Rental> getPreviousRentals() {
        return previousRentals;
    }

    public void setPreviousRentals(List<Rental> previousRentals) {
        this.previousRentals = previousRentals;
    }
}
