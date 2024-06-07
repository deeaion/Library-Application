package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.*;
import server.service.IServiceLibrarian;
import server.service.restHelping.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/librarian")
public class LibrarianController {

    @Autowired
    private IServiceLibrarian serviceLibrarian;

    @GetMapping("/subscribers/{username}")
    public ResponseEntity<SubscriberDTO> getSubscriber(@PathVariable String username) {
        Subscriber subscriber = serviceLibrarian.findSubscriber(username);
        return new ResponseEntity<>(convertSubscriberToDTO(subscriber), HttpStatus.OK);
    }

    @PostMapping("/rentals-finish")
    public ResponseEntity<Void> finishRetrievingRental(@RequestBody FinishRentalRequest request) {
        serviceLibrarian.finishRetrievingRental(request.getRentals(), request.getIdRental(), request.getIdLibrarian());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/rentals-current/{username}")
    public ResponseEntity<?> getSubscriberCurrentRentals(@PathVariable String username) {
        List<Rental> rentals = serviceLibrarian.getSubscriberCurrentRentals(username);
        List<RentalDTO> dtos = rentals.stream().map(this::convertRentalToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/rentals-history")
    public ResponseEntity<List<RentalDTO>> getSubscriberRentalsHistory(@RequestParam(value = "username") String username) {
        List<Rental> rentals = serviceLibrarian.getSubscriberRentalsHistory(username);
        List<RentalDTO> dtos = rentals.stream().map(this::convertRentalToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/basket/{username}")
    public ResponseEntity<List<BasketItemDTO>> getSubscriberBasket(@PathVariable String username) {
        List<BasketItem> basketItems = serviceLibrarian.getSubscriberBasket(username);
        List<BasketItemDTO> dtos = basketItems.stream().map(this::convertBasketItemToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // DTO conversion methods

    private RentalDTO convertRentalToDTO(Rental rental) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startedAtString = rental.getStarted_at().format(formatter);
        String endedAtString = rental.getEnded_at() != null ? rental.getEnded_at().format(formatter) : null;
        RentalDTO rentalDTO= new RentalDTO(
                startedAtString,
                endedAtString,
                new CredentialsDTO(rental.getRented_by(), "rental"),
                rental.getRetrieved_by() != null ? new CredentialsDTO(rental.getRetrieved_by(), "rental") : null,
                rental.getBooks()
        );
        rentalDTO.setId(rental.getId());
        return rentalDTO;

    }

    private SubscriberDTO convertSubscriberToDTO(Subscriber subscriber) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateOfSubscription = subscriber.getDateOfSubscription().format(formatter);
        String birthDay = subscriber.getBirthDay().format(formatter);

        return new SubscriberDTO(
                new CredentialsDTO(subscriber.getCredentials(), "subscriber"),
                dateOfSubscription,
                subscriber.getUniqueCode(),
                subscriber.getId(),
                subscriber.getCpn(),
                subscriber.getFirstName(),
                subscriber.getLastName(),
                birthDay,
                subscriber.getAddress(),
                subscriber.getPhone(),
                subscriber.getGender()
        );
    }

    private BasketItemDTO convertBasketItemToDTO(BasketItem basketItem) {
        Subscriber subscriber = basketItem.getSubscriberOfBasket();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateOfSubscription = subscriber.getDateOfSubscription().format(formatter);

        return new BasketItemDTO(
                basketItem.getId(),
                basketItem.getBook(),
                basketItem.getQuantity(),
                new SubscriberDTO(
                        new CredentialsDTO(subscriber.getCredentials(), "subscriber"),
                        dateOfSubscription,
                        subscriber.getUniqueCode(),
                        subscriber.getId(),
                        subscriber.getCpn(),
                        subscriber.getFirstName(),
                        subscriber.getLastName(),
                        subscriber.getBirthDay().format(formatter),
                        subscriber.getAddress(),
                        subscriber.getPhone(),
                        subscriber.getGender()
                )
        );
    }

    private BookDTO convertBookToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                convertBookInfoToDTO(book.getBookInfo()),
                book.getUniqueCode(),
                book.getState()
        );
    }

    private BookInfoDTO convertBookInfoToDTO(BookInfo bookInfo) {
        BookInfoDTO bookInfoDTO=
                new BookInfoDTO(
                        bookInfo.getTitle(),
                        bookInfo.getIsbn(),
                        bookInfo.getAuthor(),
                        bookInfo.getPublisher(),
                        bookInfo.getLanguage(),
                        bookInfo.getType().toString(),
                        bookInfo.getDescription(),
                        bookInfo.getImg(),
                        bookInfo.getNr_of_copies(),
                        bookInfo.getGenre().toString());
        bookInfoDTO.setId(bookInfo.getId());
        return bookInfoDTO;
    }

    private CredentialsDTO convertCredentialsToDTO(Credentials credentials) {
        return new CredentialsDTO(
                credentials, "idk"
        );
    }
}
