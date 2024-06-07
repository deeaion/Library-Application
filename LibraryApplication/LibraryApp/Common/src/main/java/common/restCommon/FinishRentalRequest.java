package common.restCommon;

import java.util.List;

public class FinishRentalRequest {
    private List<BookDTO> rentals;
    private Long idRental;
    private Long idLibrarian;

    // Getters and setters

    public FinishRentalRequest(List<BookDTO> rentals, Long idRental, Long idLibrarian) {
        this.rentals = rentals;
        this.idRental = idRental;
        this.idLibrarian = idLibrarian;
    }

    public FinishRentalRequest() {
    }

    public List<BookDTO> getRentals() {
        return rentals;
    }

    public void setRentals(List<BookDTO> rentals) {
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
