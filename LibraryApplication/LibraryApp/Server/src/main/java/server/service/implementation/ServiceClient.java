package server.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import server.config.NotificationService;
import server.model.*;

import server.model.Enums.BookType;
import server.model.Enums.Genre;
import server.model.Enums.StateOfRental;
import server.persistance.implementations.*;
import server.restCommon.NotificationRest;
import server.service.IServiceClient;
import server.service.restHelping.BasketItemDTO;
import server.service.restHelping.TransformDTO;
import server.service.util.PasswordEncryption;
import server.service.util.UniqueCodeGenerator;

import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ServiceClient implements IServiceClient {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private  SubscriberRepository subscriberRepository;
    @Autowired
    private  BookInfoRepository bookInfoRepository;
    @Autowired
    private  BookRepository bookRepository;
    @Autowired
    private  CredentialsRepository credentialsRepository;
    @Autowired
    private  PersonRepository personRepository;
    @Autowired
    private  BasketItemRepository basketItemRepository;
    @Autowired
    private RentalRepository rentalRepository;
    private final Logger logger = LogManager.getLogger(ServiceClient.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public int getNrOfItemsInStock(Long id)
    {
        BookInfo bookInfo=bookInfoRepository.get(id);
        List<Book> books= (List<Book>) bookRepository.getAll();
        int count=0;
        for(Book book:books)
        {
            if(book.getBookInfo().equals(bookInfo) && book.getState()== StateOfRental.NOT_RENTED)
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getBooksInBasket(String username)
    {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        return subscriber.getShoppingBasket().size();
    }

    @Override
    public int getQuantityOfBookInBasket(BookInfo bookInfo,String username)
    {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        List<BasketItem> basketItems=subscriber.getShoppingBasket();
        for(BasketItem basketItem:basketItems)
        {
            if(basketItem.getBook().equals(bookInfo))
            {
                return basketItem.getQuantity();
            }
        }
        return 0;
    }

    @Override
    public int numberOfItemsInBasket(String username)
    {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        int count=0;
        for(BasketItem basketItem:subscriber.getShoppingBasket())
        {
            count+=basketItem.getQuantity();
        }
        return count;
    }
    public void register(String username, String password, String conf_password, String email, String firstName, String lastName, String cpn, String address, String phone, String birthday, String gender) {
        if (!Objects.equals(password, conf_password)) {
            try {
                notificationService.notifySubscribers("Passwords do not match");
            } catch (Exception e) {
                logger.error(e);
            }
            return;
        }

        String[] resultOfHashing;
        try {
            resultOfHashing = PasswordEncryption.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            return;
        }

        String hashedPassword = resultOfHashing[1];
        String salt = resultOfHashing[0];
        Credentials credentials = new Credentials(username, hashedPassword, email, salt);
        try {
            credentials = credentialsRepository.add(credentials);
        } catch (Exception e) {
            logger.error(e);
            return;
        }

        try {
            String uniqueCode = UniqueCodeGenerator.generateUniqueCode(username);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(birthday, formatter);
            Person person = new Person(firstName, lastName, dateTime, gender, address, phone, cpn);

            // Save person to the repository
            person = personRepository.add(person);
            logger.info("Saved Person ID: " + person.getId());

            // Create subscriber with the person ID
            Subscriber subscriber = new Subscriber(uniqueCode, person, credentials, LocalDateTime.now());
            subscriber.setId(person.getId()); // Manually set the ID to ensure it matches the person ID
            logger.info("Subscriber ID before saving: " + subscriber.getId());

            // Save subscriber to the repository
            subscriberRepository.add(subscriber);
            logger.info("Subscriber ID after saving: " + subscriber.getId());
        } catch (Exception e) {
            logger.error(e);
            return;
        }

        try {
            notificationService.notifyAdmins(NotificationRest.SUBSCRIBERREGISTERED.name()+":"+username);
        } catch (Exception e) {
            logger.error(e);
        }
    }


    private Map<BookInfo,Integer> countRentalTimes()
    {
        List<BookInfo> bookInfos= (List<BookInfo>) bookInfoRepository.getAll();
        Map<BookInfo,Integer> bookInfoIntegerMap=new HashMap<>();
        for(BookInfo bookInfo:bookInfos)
        {
            bookInfoIntegerMap.put(bookInfo,findNumberOfRentedUnits(bookInfo));
        }
        return bookInfoIntegerMap;
    }
    private int findNumberOfRentedUnits(BookInfo bookInfo)
    {
       List<Rental> rentals= (List<Rental>) rentalRepository.getAll();
         int count=0;
        for(Rental rental:rentals)
        {
            List<Book> books=rental.getBooks();
            for(Book book:books)
            {
                if(book.getBookInfo().equals(bookInfo))
                {
                    count++;
                }
            }

        }
        return count;
    }
    @Override
    public Map<BookType, List<BookInfo>> getTopBooksCategories() {
        Map<BookType, List<BookInfo>> books = new HashMap<>();
        for (BookType bookType : BookType.values()) {
            books.put(bookType, new ArrayList<>()); // Initialize list for each BookType
            try {
                List<BookInfo> bookInfoForGenre = (List<BookInfo>) bookInfoRepository.findBookInfoByType(bookType.toString());
                if (!bookInfoForGenre.isEmpty()) {
                    Map<BookInfo, Integer> bookInfosInteger = new HashMap<>();
                    for (BookInfo bookInfo : bookInfoForGenre) {
                        bookInfosInteger.put(bookInfo, findNumberOfRentedUnits(bookInfo));
                    }
                    bookInfosInteger.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Sort by value in descending order
                            .limit(4)
                            .forEach(entry -> books.get(bookType).add(entry.getKey()));
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return books;
    }


    @Override
    public List<BookInfo> filterBooksBYCriteria(List<String>criterias, List<String> values) {

        List<BookInfo> bookInfos= (List<BookInfo>) bookInfoRepository.getAll();
        List<BookInfo> result=new ArrayList<>();
        for(BookInfo bookInfo:bookInfos)
        {
            boolean ok=true;
            for(int i=0;i<criterias.size();i++)
            {
                if(criterias.get(i).equals("author"))
                {
                    if(!bookInfo.getAuthor().contains(values.get(i)))
                    {
                        ok=false;
                        break;
                    }
                }
                if(criterias.get(i).equals("title"))
                {
                    if(!bookInfo.getTitle().contains(values.get(i)))
                    {
                        ok=false;
                        break;
                    }
                }
                if(criterias.get(i).equals("genre"))
                {
                    if(!bookInfo.getGenre().toString().contains(values.get(i)))
                    {
                        ok=false;
                        break;
                    }
                }
                if(criterias.get(i).equals("publisher"))
                {
                    if(!bookInfo.getPublisher().contains(values.get(i)))
                    {
                        ok=false;
                        break;
                    }
                }
                if(criterias.get(i).equals("language"))
                {
                    if(!bookInfo.getLanguage().contains(values.get(i)))
                    {
                        ok=false;
                        break;
                    }
                }
            }
            if(ok)
            {
                result.add(bookInfo);
            }
        }
        return result;
    }

    @Override
    public List<BookInfo> searchBooks(String searchContent) {
        List<BookInfo> bookInfos= (List<BookInfo>) bookInfoRepository.getAll();
        List<BookInfo> result=new ArrayList<>();
        for(BookInfo bookInfo:bookInfos)
        {
            if(bookInfo.getAuthor().contains(searchContent) || bookInfo.getTitle().contains(searchContent) || bookInfo.getGenre().toString().contains(searchContent)
            ||bookInfo.getGenre().toString().contains(searchContent) || bookInfo.getPublisher().contains(searchContent) || bookInfo.getLanguage().contains(searchContent))
            {
                result.add(bookInfo);
            }
        }
        return result;
    }

    @Override
    public List<BasketItemDTO> getBasketItems(String username) {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        return TransformDTO.transformBasketList(subscriber.getShoppingBasket());
    }

    @Override
    public List<BookInfo> getTopBooksForCategory(BookType bookType) {
        Map<BookType,List<BookInfo>> books = new HashMap<>();
        Map<BookInfo,Integer> bookInfosInteger=new HashMap<>();
        for(BookType type:BookType.values())
        {
            List<BookInfo> bookInfoForType=(List<BookInfo>)bookInfoRepository.findBookInfoByType(type.toString());
            for(BookInfo bookInfo:bookInfoForType)
            {
                bookInfosInteger.put(bookInfo,findNumberOfRentedUnits(bookInfo));
            }
            bookInfosInteger.entrySet().stream().sorted().limit(3).forEach(entry->{
                books.get(type).add(entry.getKey());
            });
        }
        return books.get(bookType);
    }

    @Override
    public List<BookInfo> getBooksForGenre(Genre genre) {
        return (List<BookInfo>) bookInfoRepository.findBookInfoByGenre(genre.toString());
    }

    @Override
    public List<BookInfo> getBooksForType(BookType bookType) {
        return (List<BookInfo>) bookInfoRepository.findBookInfoByType(bookType.toString());
    }


    public void addBookToBasket(BookInfo book, int nrOfCopies, String username) {
        Subscriber subscriber = subscriberRepository.findByUsername(username);
        if (subscriber == null) {
            throw new RuntimeException("Subscriber not found: " + username);
        }

        // Ensure shopping basket is initialized
        if (subscriber.getShoppingBasket() == null) {
            subscriber.setShoppingBasket(new ArrayList<>());
        }

        BasketItem basketItem = new BasketItem(book, nrOfCopies, subscriber);
        subscriber.getShoppingBasket().add(basketItem);

        try {
            basketItemRepository.add(basketItem);
            subscriberRepository.update(subscriber);
        } catch (Exception e) {
            logger.error("Error saving basket item or subscriber", e);
            throw e;
        }

        // Notify
        try {
            System.out.println("Sending notification");
            String notification=MessageFormat.format("Type:{0},Subscriber:{1}\r\n",NotificationRest.BASKETUPDATED.toString(), username);
            System.out.println("Notification: " + notification);
            notificationService.notifySubscribers(notification);
            notificationService.notifyAdmins(notification);
            notificationService.notifyLibrarians(notification);
        } catch (Exception e) {
            logger.error("Error sending notifications", e);
        }
    }


    @Override
    public void removeBookFromBasket(BasketItemDTO book, String username) {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        List<BasketItem> basketItems=subscriber.getShoppingBasket();
        for(BasketItem basketItem:basketItems)
        {
            if(basketItem.getId()==book.getId())
            {
                basketItems.remove(basketItem);
                try{
                    subscriberRepository.update(subscriber);

//                    basketItemRepository.remove(basketItem.getId());
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
                break;
            }
        }
        //notify
        try {
            String notification=MessageFormat.format("Type:{0},Subscriber:{1}",NotificationRest.BASKETUPDATED.toString(), username);
            notificationService.notifyAdmins(notification);
            notificationService.notifyLibrarians(notification);
        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Override
    public void updateBookQuantity(BasketItemDTO book, int quantity, String username) {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        List<BasketItem> basketItems=subscriber.getShoppingBasket();
        for(BasketItem basketItem:basketItems)
        {
            if(basketItem.getId()==book.getId())
            {
                basketItem.setQuantity(quantity);
                try{
                    basketItemRepository.update(basketItem);
                    subscriberRepository.update(subscriber);
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
                break;
            }
        }


        //notify
        try {
            String notification=MessageFormat.format("Type:{0},Subscriber:{1}",NotificationRest.BASKETUPDATED.toString(), username);
            notificationService.notifyAdmins(notification);
            notificationService.notifyLibrarians(notification);
        } catch (Exception e) {
            logger.error(e);
        }
    }


    public void finishOrder(String username) {
        Subscriber subscriber = subscriberRepository.findByUsername(username);
        List<BasketItem> basketItems = subscriber.getShoppingBasket();
        List<Book> booksToRent = new ArrayList<>();

        for (BasketItem basketItem : basketItems) {
            BookInfo bookInfo = basketItem.getBook();
            int quantity = basketItem.getQuantity();

            List<Book> booksOfInfo = (List<Book>) bookRepository.findByBookandState(bookInfo.getId(), StateOfRental.NOT_RENTED);
            if (booksOfInfo.size() < quantity) {
                throw new RuntimeException("Not enough books in stock");
            }

            Random random = new Random();
            for (int i = 0; i < quantity; i++) {
                int index = random.nextInt(booksOfInfo.size());
                booksToRent.add(booksOfInfo.get(index));
                booksOfInfo.remove(index);
            }
        }

        // Clear the shopping basket
        subscriber.getShoppingBasket().clear();

        Rental rental = new Rental(LocalDateTime.now(), null, subscriber.getCredentials(), null);
        rental.setBooks(booksToRent);

        rentalRepository.add(rental); // Persist the rental

        subscriber.addCurrentRental(rental); // Manage bidirectional relationship

        subscriberRepository.update(subscriber); // Update the subscriber

        for (Book book : booksToRent) {
            book.setState(StateOfRental.RENTED);
            bookRepository.update(book); // Update the books
        }

        // Notify admins and librarians
        String notification = MessageFormat.format("Type:{0},Subscriber:{1}", NotificationRest.RENTMADE.toString(), username);
        notificationService.notifyAdmins(notification);
        notificationService.notifyLibrarians(notification);
        notificationService.notifySubscribers(notification);
    }

    private void notifyAdminsAndLibrarians(String username, NotificationRest notificationType) {
        try {
            String notification = MessageFormat.format("Type:{0},Subscriber:{1}", notificationType, username);
            notificationService.notifyAdmins(notification);
            notificationService.notifyLibrarians(notification);
        } catch (Exception e) {
            logger.error("Error sending notifications", e);
        }
    }


}
