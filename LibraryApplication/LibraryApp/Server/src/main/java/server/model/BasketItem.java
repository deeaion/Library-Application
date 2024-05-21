package server.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "basketitem")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "basket_id"))
})
public class BasketItem extends Identifiable<Long> {
    @ManyToOne
    @JoinColumn(name = "book_id") // Foreign key to BookInfo
    private BookInfo book;

    @Column(name = "number_of_items")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "subscriber_of_basket_id", referencedColumnName = "id")
    private Subscriber subscriberOfBasket;

    public BasketItem(BookInfo book, int quantity, Subscriber subscriberOfBasket) {
        this.book = book;
        this.quantity = quantity;
        this.subscriberOfBasket = subscriberOfBasket;
    }

    public BasketItem() {}

    public BookInfo getBook() {
        return book;
    }

    public void setBook(BookInfo book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Subscriber getSubscriberOfBasket() {
        return subscriberOfBasket;
    }

    public void setSubscriberOfBasket(Subscriber subscriberOfBasket) {
        this.subscriberOfBasket = subscriberOfBasket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketItem that)) return false;
        return getQuantity() == that.getQuantity() && Objects.equals(getBook(), that.getBook()) && Objects.equals(getSubscriberOfBasket(), that.getSubscriberOfBasket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBook(), getQuantity(), getSubscriberOfBasket());
    }

    @Override
    public String toString() {
        return "BasketItem{" +
                "book=" + book +
                ", quantity=" + quantity +
                ", subscriberOfBasket=" + subscriberOfBasket +
                "} " + super.toString();
    }
}
