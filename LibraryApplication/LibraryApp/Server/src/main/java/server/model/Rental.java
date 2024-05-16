package server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rental")
public class Rental extends Identifiable<Long>  {
    public Rental() {
    }

    @Column(name="started_at")
    private LocalDateTime started_at;
    @Column(name="ended_at")
    private LocalDateTime ended_at;
    @ManyToOne
    @JoinColumn(name="rented_by", referencedColumnName = "id")
    private Credentials rented_by;
    @ManyToOne
    @JoinColumn(name="retrieved_by", referencedColumnName = "id")
    private Credentials retrieved_by;
    @ManyToMany
    @JoinTable(
            name = "bookrented",
            joinColumns = @JoinColumn(name = "rental_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public LocalDateTime getStarted_at() {
        return started_at;
    }

    public void setStarted_at(LocalDateTime started_at) {
        this.started_at = started_at;
    }

    public LocalDateTime getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(LocalDateTime ended_at) {
        this.ended_at = ended_at;
    }

    public Credentials getRented_by() {
        return rented_by;
    }

    public void setRented_by(Credentials rented_by) {
        this.rented_by = rented_by;
    }

    public Credentials getRetrieved_by() {
        return retrieved_by;
    }

    public void setRetrieved_by(Credentials retrieved_by) {
        this.retrieved_by = retrieved_by;
    }

    public Rental(LocalDateTime started_at, LocalDateTime ended_at, Credentials rented_by, Credentials retrieved_by) {
        this.started_at = started_at;
        this.ended_at = ended_at;
        this.rented_by = rented_by;
        this.retrieved_by = retrieved_by;
    }
}
