package server.service.restHelping;

import server.model.Book;

import java.util.List;

public class FinishRentalRequest {
    private List<Book> rentals;
    private Long idRental;
    private Long idLibrarian;

    // Getters and setters

    public FinishRentalRequest(List<Book> rentals, Long idRental, Long idLibrarian) {
        this.rentals = rentals;
        this.idRental = idRental;
        this.idLibrarian = idLibrarian;
    }

    public FinishRentalRequest() {
    }

    public List<Book> getRentals() {
        return rentals;
    }

    public void setRentals(List<Book> rentals) {
        this.rentals = rentals;
    }

    public Long getIdRental() {
        return idRental;
    }

    public void setIdRental(Long idRental) {
        this.idRental = idRental;
    }

    public Long getIdLibrarian() {
        return idLibrarian;
    }

    public void setIdLibrarian(Long idLibrarian) {
        this.idLibrarian = idLibrarian;
    }
}
