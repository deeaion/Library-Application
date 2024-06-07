package common.restCommon;

import common.model.Book;
import common.model.BookInfo;
import jakarta.persistence.Entity;


public class BasketItemDTO {
    private Long id;
    private BookInfo book;
    private int quantity;
    private SubscriberDTO subscriberOfBasket;
    public BasketItemDTO() {
    }

    @Override
    public String toString() {
        return "BasketItemDTO{" +
                "id=" + id +
                ", book=" + book +
                ", quantity=" + quantity +
                ", subscriberOfBasket=" + subscriberOfBasket +
                '}';
    }

    public BasketItemDTO(Long id, BookInfo book, int quantity, SubscriberDTO subscriberOfBasket) {
        this.id = id;
        this.book = book;
        this.quantity = quantity;
        this.subscriberOfBasket = subscriberOfBasket;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public SubscriberDTO getSubscriberOfBasket() {
        return subscriberOfBasket;
    }

    public void setSubscriberOfBasket(SubscriberDTO subscriberOfBasket) {
        this.subscriberOfBasket = subscriberOfBasket;
    }
}
