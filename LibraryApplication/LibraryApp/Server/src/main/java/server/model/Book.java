package server.model;

import jakarta.persistence.*;

import server.model.Enums.StateOfRental;

@Entity
@Table(name = "book")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "book_id"))
})
public class Book extends Identifiable<Long> {
    @ManyToOne(cascade = CascadeType.ALL) // Assuming BookInfo is also a JPA entity
    @JoinColumn(name = "information_id", referencedColumnName = "bookinfo_id", nullable = true)
    private BookInfo bookInfo;

    @Column(name = "unique_code", length = 60, nullable = false, unique = true)
    private String uniqueCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 25, nullable = false)
    private StateOfRental state;

    // Constructors, getters, and setters
    public Book() {
    }

    public Book(BookInfo bookInfo, String uniqueCode, StateOfRental state) {
        this.bookInfo = bookInfo;
        this.uniqueCode = uniqueCode;
        this.state = state;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public StateOfRental getState() {
        return state;
    }

    public void setState(StateOfRental state) {
        this.state = state;
    }
}
