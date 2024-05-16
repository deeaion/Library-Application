package server.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.config.NotificationService;
import server.model.Rental;
import server.model.Subscriber;
import server.service.IServiceLibrarian;

import java.util.List;

@Service
public class ServiceLibrarian implements IServiceLibrarian {
    @Autowired
    private NotificationService notificationService;

    @Override
    public Subscriber findSubscriber(String context) {
        return null;
    }

    @Override
    public void finishRetrievingRental(List<Rental> rentals) {

    }
}
