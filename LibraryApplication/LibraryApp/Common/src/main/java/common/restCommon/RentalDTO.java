package common.restCommon;

import common.model.Book;
import common.model.CredentialsDTO;

import java.util.List;

public class RentalDTO {
    private String startedAt;  // LocalDateTime as String
    private String endedAt;    // LocalDateTime as String
    private long id;

    @Override
    public String toString() {
        return "RentalDTO{" +
                "rentedBy=" + rentedBy.getUsername() +
                ", data=" + startedAt +
                '}';
    }

    private CredentialsDTO rentedBy;
    private CredentialsDTO retrievedBy;
    private List<Book> books;

    public RentalDTO(String startedAt, String endedAt, CredentialsDTO rentedBy, CredentialsDTO retrievedBy, List<Book> books) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.rentedBy = rentedBy;
        this.retrievedBy = retrievedBy;
        this.books = books;
    }

    // Getters and Setters
    public RentalDTO() {
    }
    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public CredentialsDTO getRentedBy() {
        return rentedBy;
    }

    public void setRentedBy(CredentialsDTO rentedBy) {
        this.rentedBy = rentedBy;
    }

    public CredentialsDTO getRetrievedBy() {
        return retrievedBy;
    }

    public void setRetrievedBy(CredentialsDTO retrievedBy) {
        this.retrievedBy = retrievedBy;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
