package server.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.model.BookInfo;
import server.model.Credentials;
import server.model.Librarian;
import server.model.Subscriber;
import server.persistance.implementations.*;
import server.service.IServiceAdmin;

import java.util.List;
@Service
public class ServiceAdmin implements IServiceAdmin {
    @Autowired

    private final SubscriberRepository subscriberRepository;

    @Autowired
    private final BookInfoRepository bookInfoRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final CredentialsRepository credentialsRepository;
    @Autowired
    private final LibrarianRepository librarianRepository;
    @Autowired
    private final PersonRepository personRepository;

    public ServiceAdmin(SubscriberRepository subscriberRepository, BookInfoRepository bookInfoRepository, BookRepository bookRepository, CredentialsRepository credentialsRepository, LibrarianRepository librarianRepository, PersonRepository personRepository) {
        this.subscriberRepository = subscriberRepository;
        this.bookInfoRepository = bookInfoRepository;
        this.bookRepository = bookRepository;
        this.credentialsRepository = credentialsRepository;
        this.librarianRepository = librarianRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<Subscriber> findAllSubscribers() {
        return null;
    }

    @Override
    public void deleteSubscriber(String username) {

    }

    @Override
    public Librarian addLibrarian(String username, String email, String lastName, String firstName, String CPN, String gender, String address, String phone, String birthDate) {
        return null;
    }

    @Override
    public void deleteLibrarian(String username) {

    }

    @Override
    public void updateLibrarian(String username, String email, String lastName, String firstName, String CPN, String gender, String address, String phone, String birthDate) {

    }

    @Override
    public List<Librarian> findAllLibrarians() {
        return null;
    }

    @Override
    public List<BookInfo> findAllBooks() {
        return null;
    }

    @Override
    public void addBookInfo(String title, String isbc, String author, String publisher, String language, String type, String description, String image, int copies, String genre) {

    }

    @Override
    public void showBookUnits(String isbc) {

    }

    @Override
    public void deleteBookInfo(String isbc) {

    }

    @Override
    public void updateBookInfo(String title, String isbc, String author, String publisher, String language, String type, String description, String image, int copies, String genre) {

    }
}
