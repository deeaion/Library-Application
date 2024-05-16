package common.model;

import jakarta.persistence.*;
import common.model.Enums.StateOfRental;

@Entity
@Table(name = "book")
public class Book extends Identifiable<Long> {
    @ManyToOne(cascade = CascadeType.ALL) // Assuming BookInfo is also a JPA entity
    @JoinColumn(name = "information_id", referencedColumnName = "id", nullable = true)
    private BookInfo bookInfo;

    @Column(name = "unique_code", length = 60, nullable = false, unique = true)
    private String uniqueCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 25, nullable = false)
    private StateOfRental state;

    // Constructors, getters, and setters
    public Book() {
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
