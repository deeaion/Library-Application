package server.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "subscriber")
public class Subscriber extends Person{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private Credentials credentials;

    @Column(name = "date_of_subscription")
    private LocalDateTime dateOfSubscription;

    @Column(name = "unique_code")
    private String uniqueCode;

    @OneToMany(mappedBy = "subscriberOfBasket")

    private List<BasketItem> shoppingBasket;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "CurrentRentals",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id")
    )
    private List<Rental> currentRentals;

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setShoppingBasket(List<BasketItem> shoppingBasket) {
        this.shoppingBasket = shoppingBasket;
    }

    public void setCurrentRentals(List<Rental> currentRentals) {
        this.currentRentals = currentRentals;
    }

    public void setPreviousRentals(List<Rental> previousRentals) {
        this.previousRentals = previousRentals;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "PreviousRentals",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id")
    )
    private List<Rental> previousRentals;

    public Subscriber(String uniqueCode,String firstName, String lastName, LocalDateTime birthDay, String gender, String address, String phone, String cpn, Credentials credentials, LocalDateTime dateOfSubscription) {
        super(firstName, lastName, birthDay, gender, address, phone, cpn);
        this.credentials = credentials;
        this.dateOfSubscription = dateOfSubscription;
        this.uniqueCode=uniqueCode;
    }

    public Subscriber() {
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setDateOfSubscription(LocalDateTime dateOfSubscription) {
        this.dateOfSubscription = dateOfSubscription;
    }

}
