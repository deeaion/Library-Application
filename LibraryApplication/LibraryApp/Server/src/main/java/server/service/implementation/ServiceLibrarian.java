package server.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.config.NotificationService;
import server.model.*;
import server.persistance.implementations.*;
import server.restCommon.NotificationRest;
import server.service.IServiceLibrarian;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceLibrarian implements IServiceLibrarian {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private final SubscriberRepository subscriberRepository;
    @Autowired
    private final BookInfoRepository bookInfoRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final RentalRepository rentalRepository;
    @Autowired
    private final LibrarianRepository librarianRepository;

    @Autowired
    public ServiceLibrarian(SubscriberRepository subscriberRepository, NotificationService notificationService, BookInfoRepository bookInfoRepository, BookRepository bookRepository, RentalRepository rentalRepository, LibrarianRepository librarianRepository) {
        this.subscriberRepository = subscriberRepository;
        this.notificationService = notificationService;
        this.bookInfoRepository = bookInfoRepository;
        this.bookRepository = bookRepository;
        this.rentalRepository = rentalRepository;
        this.librarianRepository = librarianRepository;
    }
    @Override
    public Subscriber findSubscriber(String context) {
        //incerc dupa username
        Subscriber subscriber = subscriberRepository.findByUsername(context);
        if(subscriber==null)
        {
            //incerc dupa email
            subscriber = subscriberRepository.findByEmail(context);
            if (subscriber==null)
            {
                //incerc dupa CNP
                subscriber = subscriberRepository.findByCPN(context);
                if (subscriber==null)
                {
                    //incerc dupa unique code
                    subscriber = subscriberRepository.findByUniqueCode(context);
                }
            }

        }
        return subscriber;
    }

    @Override
    public void finishRetrievingRental(List<Book> rentals,Long id_rental,Long id_librarian) {
        //ok deci trebuie fiecare book sa schimb starea in available sau lost
        Rental rental = rentalRepository.get(id_rental);
        List<Book> notUpdatedBooks = rental.getBooks();
        int i=0;

        for (i=0;i<notUpdatedBooks.size();i++)
        {
            Book book = rentals.get(i);
            Book notUpdatedBook = notUpdatedBooks.get(i);
            if(notUpdatedBook.getState()!=book.getState())
            {
                notUpdatedBook.setState(book.getState());
                bookRepository.update(notUpdatedBook);
            }

        }
        //acum tre sa sterg rental din CurrentRentals -> sa il pun PreviousRentals.
        //fac update la subscriber
        List<Rental> currentRentals=subscriberRepository.getCurrentRentalsForUser(rental.getRented_by().getUsername());
        currentRentals.removeIf(rental1 -> rental1.getId().equals(id_rental));
        List<Rental> previousRentals=subscriberRepository.getPreviousRentalsForUser(rental.getRented_by().getUsername());
        previousRentals.add(rental);
        Subscriber subscriber = subscriberRepository.findByUsername(rental.getRented_by().getUsername());
        subscriber.setPreviousRentals(previousRentals);
        subscriber.setCurrentRentals(currentRentals);
        Librarian librarian= librarianRepository.findByUserId(id_librarian);
        rental.setRetrieved_by(librarian.getCredentials());
        rental.setEnded_at(LocalDateTime.now());
        rentalRepository.update(rental);
       subscriber= subscriberRepository.update(subscriber);

        notificationService.notifyLibrarians(NotificationRest.RENTRETURNED.toString()+":"+rental.getRented_by().getUsername());
        notificationService.notifyAdmins(NotificationRest.RENTRETURNED.toString()+":"+rental.getRented_by().getUsername());

    }

    @Override
    public List<Librarian> findAllLibrarians() {
        return (List<Librarian>) librarianRepository.getAll();
    }

    @Override
    public List<Rental> getSubscriberCurrentRentals(String username) {
        List<Rental> rentals = subscriberRepository.getCurrentRentalsForUser(username);
        return rentals;
    }

    @Override
    public List<Rental> getSubscriberRentalsHistory(String username) {
        List<Rental> rentals = subscriberRepository.getPreviousRentalsForUser(username);
        return rentals;
    }

    @Override
    public List<BasketItem> getSubscriberBasket(String username) {
        Subscriber subscriber = subscriberRepository.findByUsername(username);
        return subscriber.getShoppingBasket();
    }
}
