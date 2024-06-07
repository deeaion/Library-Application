package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.*;
import server.service.IServiceAdmin;
import server.service.restHelping.*;

import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IServiceAdmin serviceAdmin;

    @GetMapping("/subscribers")
    public ResponseEntity<?> getAllSubscribers() {
        try {
            List<Subscriber> subscribers = serviceAdmin.findAllSubscribers();
            List<SubscriberDTO> dtos = subscribers.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/subscribers/{username}")
    public ResponseEntity<?> deleteSubscriber(@PathVariable String username) {
        try {
            serviceAdmin.deleteSubscriber(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/librarians")
    public ResponseEntity<?> addLibrarian(@RequestBody LibrarianDTO librarianDTO) {
        try {
            Librarian librarian = serviceAdmin.addLibrarian(
                    librarianDTO.getCredentials().getUsername(),
                    librarianDTO.getCredentials().getEmail(),
                    librarianDTO.getPerson().getLastName(),
                    librarianDTO.getPerson().getFirstName(),
                    librarianDTO.getPerson().getCPN(),
                    librarianDTO.getPerson().getGender(),
                    librarianDTO.getPerson().getAddress(),
                    librarianDTO.getPerson().getPhone(),
                    librarianDTO.getPerson().getBirthDate()
            );
            return new ResponseEntity<>(convertToDTO(librarian), HttpStatus.CREATED);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/librarians/{username}")
    public ResponseEntity<?> deleteLibrarian(@PathVariable String username) {
        try {
            serviceAdmin.deleteLibrarian(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/librarians/{username}")
    public ResponseEntity<?> updateLibrarian(@PathVariable String username, @RequestBody LibrarianDTO librarianDTO) {
        try {
            serviceAdmin.updateLibrarian(
                    username,
                    librarianDTO.getCredentials().getEmail(),
                    librarianDTO.getPerson().getLastName(),
                    librarianDTO.getPerson().getFirstName(),
                    librarianDTO.getPerson().getCPN(),
                    librarianDTO.getPerson().getGender(),
                    librarianDTO.getPerson().getAddress(),
                    librarianDTO.getPerson().getPhone(),
                    librarianDTO.getPerson().getBirthDate()
            );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/librarians")
    public ResponseEntity<?> getAllLibrarians() {
        try {
            List<Librarian> librarians = serviceAdmin.findAllLibrarians();
            List<LibrarianDTO> dtos = librarians.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books")
    public ResponseEntity<?> getAllBooks() {
        try {
            List<BookInfo> books = serviceAdmin.findAllBooks();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    public ResponseEntity<?> addBookInfo(@RequestBody BookInfoDTO bookInfoDTO) {
        try {
            serviceAdmin.addBookInfo(
                    bookInfoDTO.getTitle(),
                    bookInfoDTO.getIsbc(),
                    bookInfoDTO.getAuthor(),
                    bookInfoDTO.getPublisher(),
                    bookInfoDTO.getLanguage(),
                    bookInfoDTO.getType(),
                    bookInfoDTO.getDescription(),
                    bookInfoDTO.getImage(),
                    bookInfoDTO.getCopies(),
                    bookInfoDTO.getGenre()
            );
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/books/{isbc}")
    public ResponseEntity<?> deleteBookInfo(@PathVariable String isbc) {
        try {
            serviceAdmin.deleteBookInfo(isbc);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books/{isbc}")
    public ResponseEntity<?> updateBookInfo(@PathVariable String isbc, @RequestBody BookInfoDTO bookInfoDTO) {
        try {
            serviceAdmin.updateBookInfo(
                    bookInfoDTO.getTitle(),
                    isbc,
                    bookInfoDTO.getAuthor(),
                    bookInfoDTO.getPublisher(),
                    bookInfoDTO.getLanguage(),
                    bookInfoDTO.getType(),
                    bookInfoDTO.getDescription(),
                    bookInfoDTO.getImage(),
                    bookInfoDTO.getCopies(),
                    bookInfoDTO.getGenre()
            );
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/{id_bookInfo}")
    public ResponseEntity<?> getBookUnits(@PathVariable Long id_bookInfo) {
        try {
            List<Book> books = serviceAdmin.findBookUnits(id_bookInfo);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DTO conversion methods

    private RentalDTO convertToDTO(Rental rental) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startedAtString = rental.getStarted_at().format(formatter);
        String endedAtString = rental.getEnded_at() != null ? rental.getEnded_at().format(formatter) : null;

        return new RentalDTO(
                startedAtString,
                endedAtString,
                new CredentialsDTO(rental.getRented_by(), "rental"),
                rental.getRetrieved_by() != null ? new CredentialsDTO(rental.getRetrieved_by(), "rental") : null,
                rental.getBooks()
        );
    }

    private SubscriberDTO convertToDTO(Subscriber subscriber) {
        CredentialsDTO credentialsDTO = new CredentialsDTO(subscriber.getCredentials(), "subscriber");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateOfSubscription = subscriber.getDateOfSubscription().format(formatter);

        SubscriberDTO subscriberDTO = new SubscriberDTO(
                credentialsDTO,
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
        );
        subscriberDTO.setGender(subscriber.getGender());
        return subscriberDTO;
    }

    private LibrarianDTO convertToDTO(Librarian librarian) {
        CredentialsDTO credentialsDTO = new CredentialsDTO(librarian.getCredentials(), "librarian");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateOfEmployment = librarian.getHireDate().format(formatter);

        PersonDTO personDTO = new PersonDTO(
                librarian.getPerson().getFirstName(),
                librarian.getPerson().getLastName(),
                librarian.getPerson().getBirthDay().format(formatter),
                librarian.getPerson().getGender(),
                librarian.getPerson().getAddress(),
                librarian.getPerson().getPhone(),
                librarian.getPerson().getCpn()
        );

        return new LibrarianDTO(credentialsDTO, dateOfEmployment, personDTO, librarian.getUniqueCode());
    }

    private BookInfoDTO convertToDTO(BookInfo bookInfo) {
        BookInfoDTO bookInfoDTO = new BookInfoDTO(
                bookInfo.getTitle(),
                bookInfo.getIsbn(),
                bookInfo.getAuthor(),
                bookInfo.getPublisher(),
                bookInfo.getLanguage(),
                bookInfo.getType().toString(),
                bookInfo.getDescription(),
                bookInfo.getImg(),
                bookInfo.getNr_of_copies(),
                bookInfo.getGenre().toString()
        );
        bookInfoDTO.setId(bookInfo.getId());

        return bookInfoDTO;
    }

    private BasketItemDTO convertToDTO(BasketItem basketItem) {
        Subscriber subscriber = basketItem.getSubscriberOfBasket();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateOfSubscription = subscriber.getDateOfSubscription().format(formatter);

        return new BasketItemDTO(
                basketItem.getId(),
                basketItem.getBook(),
                basketItem.getQuantity(),
                new SubscriberDTO(
                        new CredentialsDTO(basketItem.getSubscriberOfBasket().getCredentials(), "subscriber"),
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

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                convertToDTO(book.getBookInfo()),
                book.getUniqueCode(),
                book.getState()
        );
    }

    private CredentialsDTO convertToDTO(Credentials credentials) {
        return new CredentialsDTO(credentials, "idk");
    }
}
