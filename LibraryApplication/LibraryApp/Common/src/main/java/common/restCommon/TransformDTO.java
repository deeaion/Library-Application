package common.restCommon;

import common.model.*;
import common.restCommon.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TransformDTO {

    public static BasketItemDTO transformBasket(BasketItem basketItem) {
        return new BasketItemDTO(
                basketItem.getId(),
                basketItem.getBook(),
                basketItem.getQuantity(),
                transformSubscriber(basketItem.getSubscriberOfBasket())
        );
    }

    public static List<BasketItemDTO> transformBasketList(List<BasketItem> basketItems) {
        return basketItems.stream()
                .map(TransformDTO::transformBasket)
                .collect(Collectors.toList());
    }

    private static SubscriberDTO transformSubscriber(Subscriber subscriber) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String birthDayString = subscriber.getBirthDay().format(formatter);
        String dateOfSubscriptionString = subscriber.getDateOfSubscription().format(formatter);

        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setCredentials(new CredentialsDTO(subscriber.getCredentials(),"subscriber"));
        subscriberDTO.setDateOfSubscription(dateOfSubscriptionString);
        subscriberDTO.setUniqueCode(subscriber.getUniqueCode());
        subscriberDTO.setId(subscriber.getId());
        subscriberDTO.setCpn(subscriber.getCpn());
        subscriberDTO.setGender(subscriber.getGender());
        subscriberDTO.setFirstName(subscriber.getFirstName());
        subscriberDTO.setLastName(subscriber.getLastName());
        subscriberDTO.setBirthDate(birthDayString);
        subscriberDTO.setAddress(subscriber.getAddress());
        subscriberDTO.setPhoneNumber(subscriber.getPhone());

        //set the current rentals
//        subscriberDTO.setCurrentRentals(subscriber.getCurrentRentals().stream().map(TransformDTO::transformRental).collect(Collectors.toList()));
        return subscriberDTO;
    }
    public TransformDTO() {
    }
    private static RentalDTO transformRental(Rental rental) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startedAtString = rental.getStarted_at().format(formatter);
        String endedAtString = rental.getEnded_at().format(formatter);

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setStartedAt(startedAtString);
        rentalDTO.setEndedAt(endedAtString);
        rentalDTO.setRentedBy(new CredentialsDTO(rental.getRented_by(),"rental"));
        rentalDTO.setRetrievedBy(new CredentialsDTO(rental.getRetrieved_by(),"rental"));
        rentalDTO.setBooks(rental.getBooks());
        return rentalDTO;
    }

    private SubscriberDTO convertToDTO(Subscriber subscriber) {
        CredentialsDTO credentialsDTO = new CredentialsDTO(
                subscriber.getCredentials(),"subscriber"
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateOfSubscription = subscriber.getDateOfSubscription().format(formatter);

        List<BasketItemDTO> basketItemDTOs = subscriber.getShoppingBasket().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<RentalDTO> currentRentalDTOs = subscriber.getCurrentRentals().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<RentalDTO> previousRentalDTOs = subscriber.getPreviousRentals().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        SubscriberDTO subscriberDTO = new SubscriberDTO(

                credentialsDTO,
                dateOfSubscription,
                subscriber.getUniqueCode(),
                subscriber.getId(),
                subscriber.getFirstName(),
                subscriber.getLastName(),
                subscriber.getBirthDay().format(formatter),
                subscriber.getAddress(),
                subscriber.getPhone(),
                subscriber.getCpn(),
                subscriber.getGender()
        );


        return subscriberDTO;
    }

    private LibrarianDTO convertToDTO(Librarian librarian) {
        CredentialsDTO credentialsDTO = new CredentialsDTO(
                librarian.getCredentials(),"librarian"
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateOfEmployment = librarian.getHireDate().format(formatter);

        PersonDTO personDTO = new PersonDTO(
                librarian.getFirstName(),
                librarian.getLastName(),
                librarian.getBirthDay().format(formatter),
                librarian.getGender(),
                librarian.getAddress(),
                librarian.getPhone(),
                librarian.getCpn()
        );

        return new LibrarianDTO(credentialsDTO, dateOfEmployment, personDTO, librarian.getUniqueCode());
    }

    private BookInfoDTO convertToDTO(BookInfo bookInfo) {
        return new BookInfoDTO(
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
    }

    private BasketItemDTO convertToDTO(BasketItem basketItem) {
        return new BasketItemDTO(
                basketItem.getId(),
                basketItem.getBook(),
                basketItem.getQuantity(),
                convertToDTO(basketItem.getSubscriberOfBasket())
        );
    }

    private RentalDTO convertToDTO(Rental rental) {
        List<BookDTO> bookDTOs = rental.getBooks().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        CredentialsDTO rentedByDTO = convertToDTO(rental.getRented_by());
        CredentialsDTO retrievedByDTO = rental.getRetrieved_by() != null ? convertToDTO(rental.getRetrieved_by()) : null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startedAt = rental.getStarted_at().format(formatter);
        String endedAt = rental.getEnded_at() != null ? rental.getEnded_at().format(formatter) : null;

        return new RentalDTO(
                startedAt,
                endedAt,
                rentedByDTO,
                retrievedByDTO,
                rental.getBooks()
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
        return new CredentialsDTO(
                credentials,"idk"
        );
    }
}
