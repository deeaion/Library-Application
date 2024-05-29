package server.persistance.interfaces;

import server.model.BasketItem;
import server.model.Rental;
import server.model.Subscriber;

import java.util.List;

public interface ISubscriberRepository extends IRepository<Long, Subscriber>{
    Subscriber findByUsername(String username);
    Subscriber findByEmail(String email);
    Subscriber findByCPN(String address);
    Subscriber findByUniqueCode(String uniqueCode);
    List<Rental> getCurrentRentalsForUser(String username);
    List<Rental> getPreviousRentalsForUser(String username);

}
