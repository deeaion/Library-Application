package server.service.restHelping;


import server.model.Book;
import server.model.CredentialsDTO;

import java.util.List;

public class RentalDTO {
    private String startedAt;  // LocalDateTime as String
    private String endedAt;    // LocalDateTime as String
    private CredentialsDTO rentedBy;
    private CredentialsDTO retrievedBy;
    private List<Book> books;

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
}
