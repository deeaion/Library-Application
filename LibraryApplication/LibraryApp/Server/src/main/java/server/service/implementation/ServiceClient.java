package server.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.config.NotificationService;
import server.model.*;
import server.model.Enums.BookType;
import server.model.Enums.Genre;
import server.persistance.implementations.*;
import server.service.IServiceClient;
import server.service.util.PasswordEncryption;
import server.service.util.UniqueCodeGenerator;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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
    @Override
    public void register(String username, String password, String conf_password, String email, String FirstName, String LastName, String CPN, String Address, String Phone,String birthday,String gender) {
        if(!Objects.equals(password, conf_password))
        {
            try {
                notificationService.notifySubscribers("Passwords do not match");
            } catch (Exception e) {
                logger.error(e);
            }
        }
        String[] resultOfHashing= new String[0];
        try {
            resultOfHashing = PasswordEncryption.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
        String hashedPassword=resultOfHashing[1];
        String salt=resultOfHashing[0];
        Credentials credentials=new Credentials(username,hashedPassword,email,salt);
        try{
            credentialsRepository.add(credentials);
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        try{
            String uniqueCode= UniqueCodeGenerator.generateUniqueCode(username);
            Subscriber subscriber=new Subscriber(uniqueCode,FirstName,LastName,LocalDateTime.parse(birthday),gender,Address,Phone,CPN,credentials, LocalDateTime.now());
            subscriberRepository.add(subscriber);
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        try {
            notificationService.notifyAdmins("New subscriber registered: "+username);
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
    public Map<Genre, List<BookInfo>> getTopBooksCategories() {
        Map<Genre,List<BookInfo>> books = new HashMap<>();
        Map<BookInfo,Integer> bookInfosInteger=new HashMap<>();

        for (Genre genre:Genre.values())
        {
            List<BookInfo> bookInfoForGenre=(List<BookInfo>)bookInfoRepository.findBookInfoByGenre(genre.toString());
            for(BookInfo bookInfo:bookInfoForGenre)
            {
                bookInfosInteger.put(bookInfo,findNumberOfRentedUnits(bookInfo));
            }
           bookInfosInteger.entrySet().stream().sorted().limit(3).forEach(entry->{
               books.get(genre).add(entry.getKey());
           });
        }
        return books;

    }

    @Override
    public List<BookInfo> filterBooksBYCriteria(String criteria, String value) {
        switch (criteria) {
            case "author":
                return (List<BookInfo>) bookInfoRepository.findBookInfoByAuthor(value);
            case "title":
                return (List<BookInfo>) bookInfoRepository.findBookInfoByTitle(value);
            case "genre":
                return (List<BookInfo>) bookInfoRepository.findBookInfoByGenre(value);
            default:
                return new ArrayList<>();
        }
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
    public List<BasketItem> getBasketItems(String username) {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        return subscriber.getShoppingBasket();
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

    @Override
    public void addBookToBasket(BookInfo book, int nrOfCopies, String username) {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        BasketItem basketItem=new BasketItem(book,nrOfCopies,subscriber);
        subscriber.getShoppingBasket().add(basketItem);
        try{
            basketItemRepository.add(basketItem);
            subscriberRepository.update(subscriber);
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        //notify
        try {
            notificationService.notifyAdmins("Subscriber:"+username+",book:"+book.getTitle()+",copies:"+nrOfCopies);
            notificationService.notifyLibrarians("Subscriber:"+username+",book:"+book.getTitle()+",copies:"+nrOfCopies);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void removeBookFromBasket(BookInfo book, String username) {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        List<BasketItem> basketItems=subscriber.getShoppingBasket();
        for(BasketItem basketItem:basketItems)
        {
            if(basketItem.getBook().equals(book))
            {
                basketItems.remove(basketItem);
                try{
                    basketItemRepository.remove(basketItem.getId());
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
            notificationService.notifyAdmins("Subscriber:"+username+",book:"+book.getIsbn());
            notificationService.notifyLibrarians("Subscriber:"+username+",book:"+book.getIsbn());
        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Override
    public void updateBookQuantity(BookInfo book, int quantity, String username) {
        Subscriber subscriber= (Subscriber) subscriberRepository.findByUsername(username);
        List<BasketItem> basketItems=subscriber.getShoppingBasket();
        for(BasketItem basketItem:basketItems)
        {
            if(basketItem.getBook().equals(book))
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
            notificationService.notifyAdmins("Subscriber:"+username+",book:"+book.getIsbn()+",quantity:"+quantity);
            notificationService.notifyLibrarians("Subscriber:"+username+",book:"+book.getIsbn()+",quantity:"+quantity);
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
