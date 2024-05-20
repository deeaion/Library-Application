package server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subscriber")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "subscriber_id"))
})
public class Subscriber extends Person {
    @OneToOne
    @JoinColumn(name = "credentials_id", referencedColumnName = "user_id")
    private Credentials credentials;

    @Column(name = "date_of_subscription")
    private LocalDateTime dateOfSubscription;

    @Column(name = "unique_code", nullable = false)
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "PreviousRentals",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "rental_id")
    )
    private List<Rental> previousRentals;

    public Subscriber() {}

    public Subscriber(String uniqueCode, String firstName, String lastName, LocalDateTime birthDay, String gender, String address, String phone, String cpn, Credentials credentials, LocalDateTime dateOfSubscription) {
        super(firstName, lastName, birthDay, gender, address, phone, cpn);
        this.credentials = credentials;
        this.dateOfSubscription = dateOfSubscription;
        this.uniqueCode = uniqueCode;
    }
}
