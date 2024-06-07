package server.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.config.NotificationService;
import server.model.*;
import server.model.Enums.BookType;
import server.model.Enums.Genre;
import server.model.Enums.StateOfRental;
import server.persistance.implementations.*;
import server.restCommon.NotificationRest;
import server.service.IServiceAdmin;
import server.service.util.PasswordEncryption;
import server.service.util.UniqueCodeGenerator;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private final NotificationService notificationService;

    public ServiceAdmin(SubscriberRepository subscriberRepository, BookInfoRepository bookInfoRepository, BookRepository bookRepository, CredentialsRepository credentialsRepository, LibrarianRepository librarianRepository, PersonRepository personRepository,
                        NotificationService notificationService) {
        this.subscriberRepository = subscriberRepository;
        this.bookInfoRepository = bookInfoRepository;
        this.bookRepository = bookRepository;
        this.credentialsRepository = credentialsRepository;
        this.librarianRepository = librarianRepository;
        this.personRepository = personRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<Subscriber> findAllSubscribers() {

        return (List<Subscriber>) subscriberRepository.getAll();
    }

    @Override
    public void deleteSubscriber(String username) {
        Subscriber subscriber = subscriberRepository.findByUsername(username);

        subscriberRepository.remove(subscriber.getId());



    }

    @Override
    public Librarian addLibrarian(String username, String email, String lastName, String firstName, String CPN, String gender, String address, String phone, String birthDate) throws NoSuchAlgorithmException {
        try {
            String password = "lib" + username + "123";

            String[] hashing = PasswordEncryption.hashPassword(password);
            String salt = hashing[0];
            String hashedPassword = hashing[1];
            Credentials credentials = new Credentials(username, hashedPassword, email, salt);
            credentials = credentialsRepository.add(credentials);
            //setez si persoana
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate Time = LocalDate.parse(birthDate, formatter);
            LocalDateTime dateTime = Time.atStartOfDay();
            Person person = new Person(firstName, lastName, dateTime, gender, address, phone, CPN);
            person = personRepository.add(person);
            String uniqueCode = UniqueCodeGenerator.generateUniqueCode(username);
            Librarian librarian = new Librarian(credentials, LocalDateTime.now(), person, uniqueCode);
            librarian.setId(credentials.getId());
            librarian = librarianRepository.add(librarian);
            notificationService.notifyAdmins(NotificationRest.LIBRARIANADDED.toString());
            return librarian;
        } catch (Exception e) {
            e.printStackTrace();
        }
return null;

    }

    @Override
    public void deleteLibrarian(String username) {
        Librarian librarian = librarianRepository.findByUsername(username);
        librarianRepository.remove(librarian.getId());
        notificationService.notifyAdmins(NotificationRest.LIBRARIANREMOVED.toString());

    }

    @Override
    public void updateLibrarian(String username, String email, String lastName, String firstName, String CPN, String gender, String address, String phone, String birthDate) {

        Librarian librarian = librarianRepository.findByUsername(username);
        Credentials credentialsUpdated=librarian.getCredentials();
        credentialsUpdated.setEmail(email);
        credentialsUpdated.setUsername(username);
        credentialsRepository.update(credentialsUpdated);
        //person update
        Person personUpdated=librarian.getPerson();
        personUpdated.setLastName(lastName);
        personUpdated.setFirstName(firstName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(birthDate, formatter);
        personUpdated.setBirthDay(dateTime);
        personUpdated.setGender(gender);
        personUpdated.setAddress(address);
        personUpdated.setPhone(phone);
        personUpdated.setCpn(CPN);
        personRepository.update(personUpdated);
        Librarian modifiedLibrarian=new Librarian(credentialsUpdated,LocalDateTime.now(),personUpdated,librarian.getUniqueCode());
        librarianRepository.update(modifiedLibrarian);
        notificationService.notifyAdmins(NotificationRest.LIBRARIANUPDATED.toString());

    }

    @Override
    public List<Librarian> findAllLibrarians() {
        return (List<Librarian>) librarianRepository.getAll();
    }

    @Override
    public List<BookInfo> findAllBooks() {

        return (List<BookInfo>) bookInfoRepository.getAll();
    }

    @Override
    public List<Book> findBookUnits(Long id_bookInfo) {
        return (List<Book>) bookRepository.findByBook(id_bookInfo);
    }


    private void  handleModifyingBookUnits(BookInfo bookInfo,int copies) {
        List<Book> books= (List<Book>) bookRepository.findByBook(bookInfo.getId());
        if(books.size()<copies) {
            int difference=copies-books.size();
            for(int i=0;i<difference;i++) {
                Book book=new Book(bookInfo,UniqueCodeGenerator.generateUniqueCode(bookInfo.getIsbn()), StateOfRental.NOT_RENTED);
                book.setId(bookInfo.getId());
                bookRepository.add(book);
            }
            notificationService.notifyAdmins(NotificationRest.BOOKSADDED.toString());
            notificationService.notifySubscribers(NotificationRest.BOOKSADDED.toString());
            notificationService.notifyLibrarians(NotificationRest.BOOKSADDED.toString());
        }
        else if(books.size()>copies) {
            int difference = books.size() - copies;
            int removed = 0;
            //daca am lost le sterg pe alea cat pot! daca nu iau random
            for (Book book : books) {
                if (removed < difference) {
                    bookRepository.remove(book.getId());
                    removed++;
                } else {
                    break;
                }
            }
            //sterg restu
            for (int i = 0; i < difference - removed; i++) {
                int randomIndex = (int) (Math.random() * books.size());
                bookRepository.remove(books.get(randomIndex).getId());
            }

            notificationService.notifyAdmins(NotificationRest.BOOKSREMOVED.toString());
            notificationService.notifySubscribers(NotificationRest.BOOKSREMOVED.toString());
            notificationService.notifyLibrarians(NotificationRest.BOOKSREMOVED.toString());


        }


    }
    @Override
    public void addBookInfo(String title, String isbc, String author, String publisher, String language, String type, String description, String image, int copies, String genre) {
        BookInfo bookInfo=new BookInfo(title,isbc,publisher,author,language,description, Genre.valueOf(genre), BookType.valueOf(type),image,copies);

        bookInfoRepository.add(bookInfo);
        handleModifyingBookUnits(bookInfo,copies);
        notificationService.notifyAdmins(NotificationRest.BOOKSUPDATED.toString());
        notificationService.notifySubscribers(NotificationRest.BOOKSUPDATED.toString());
        notificationService.notifyLibrarians(NotificationRest.BOOKSUPDATED.toString());

    }

    @Override
    public List<Book> showBookUnits(String isbc) {
        BookInfo bookInfo = bookInfoRepository.findBookInfoByIsbn(isbc).iterator().next();
        List<Book> books= (List<Book>) bookRepository.findByBook(bookInfo.getId());



        return books;
    }

    @Override
    public void deleteBookInfo(String isbc) {
        BookInfo bookInfo = bookInfoRepository.findBookInfoByIsbn(isbc).iterator().next();
        bookInfoRepository.remove(bookInfo.getId());
        List<Book> books= (List<Book>) bookRepository.findByBook(bookInfo.getId());
        for(Book book:books) {
            bookRepository.remove(book.getId());
        }
        notificationService.notifyAdmins(NotificationRest.BOOKSREMOVED.toString());
        notificationService.notifySubscribers(NotificationRest.BOOKSREMOVED.toString());
        notificationService.notifyLibrarians(NotificationRest.BOOKSREMOVED.toString());

    }

    @Override
    public void updateBookInfo(String title, String isbc, String author, String publisher, String language, String type, String description, String image, int copies, String genre) {
        BookInfo bookInfo = bookInfoRepository.findBookInfoByIsbn(isbc).iterator().next();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setLanguage(language);
        bookInfo.setDescription(description);
        bookInfo.setGenre(Genre.valueOf(genre));
        bookInfo.setType(BookType.valueOf(type));
        bookInfo.setImg(image);
        bookInfo.setNr_of_copies(copies);

        handleModifyingBookUnits(bookInfo,copies);
        bookInfoRepository.update(bookInfo);
        notificationService.notifyAdmins(NotificationRest.BOOKSUPDATED.toString());
        notificationService.notifySubscribers(NotificationRest.BOOKSUPDATED.toString());
        notificationService.notifyLibrarians(NotificationRest.BOOKSUPDATED.toString());
    }
}
