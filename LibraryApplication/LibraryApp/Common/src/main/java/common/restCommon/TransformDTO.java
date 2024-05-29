package common.restCommon;


import common.model.BasketItem;
import common.model.Credentials;
import common.model.CredentialsDTO;
import common.model.Rental;
import common.model.Subscriber;

import java.time.LocalDateTime;
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

        SubscriberDTO subscriberDTO = new SubscriberDTO(
                subscriber.getId(),
                new CredentialsDTO(subscriber.getCredentials(),"subscriber"),
                dateOfSubscriptionString,
                subscriber.getUniqueCode(),
                TransformDTO.transformBasketList(subscriber.getShoppingBasket())

        );
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
}
