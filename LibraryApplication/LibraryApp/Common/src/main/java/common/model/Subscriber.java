package common.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriber")
public class Subscriber extends Person {
    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private Credentials credentials;

    @Column(name = "date_of_subscription")
    private LocalDateTime dateOfSubscription;

    @Column(name = "unique_code")
    private String uniqueCode;






    @OneToMany(mappedBy = "subscriberOfBasket", fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)


    private List<BasketItem> shoppingBasket;


    @ManyToMany(cascade=CascadeType.MERGE)
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

    public Credentials getCredentials() {
        return credentials;
    }

    public LocalDateTime getDateOfSubscription() {
        return dateOfSubscription;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public List<BasketItem> getShoppingBasket() {
        return shoppingBasket;
    }

    public List<Rental> getCurrentRentals() {
        return currentRentals;
    }

    public List<Rental> getPreviousRentals() {
        return previousRentals;
    }

    @ManyToMany(cascade=CascadeType.MERGE)
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
        this.currentRentals = new ArrayList<>();
        this.previousRentals = new ArrayList<>();
    }

    public Subscriber() {
    }
    public void addCurrentRental(Rental rental) {
        if (currentRentals == null) {
            currentRentals = new ArrayList<>();
        }
        currentRentals.add(rental);
        rental.setRented_by(this.credentials);
    }


    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setDateOfSubscription(LocalDateTime dateOfSubscription) {
        this.dateOfSubscription = dateOfSubscription;
    }

}
