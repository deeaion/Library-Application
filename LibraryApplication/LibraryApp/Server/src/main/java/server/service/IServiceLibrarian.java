package server.service;

import server.model.*;

import java.util.List;

public interface IServiceLibrarian {
    Subscriber findSubscriber(String context);
    List<Rental> getSubscriberCurrentRentals(String username);
    List<Rental> getSubscriberRentalsHistory(String username);
    List<BasketItem> getSubscriberBasket(String username);
    public void finishRetrievingRental(List<Book> rentals, Long id_rental, Long id_librarian);
    public List<Librarian> findAllLibrarians();
}
