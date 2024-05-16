package server.service;

import server.model.Rental;
import server.model.Subscriber;

import java.util.List;

public interface IServiceLibrarian {
    Subscriber findSubscriber(String context);
    void finishRetrievingRental(List<Rental> rentals);
}
