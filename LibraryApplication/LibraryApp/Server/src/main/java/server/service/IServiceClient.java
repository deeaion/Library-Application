package server.service;

import server.model.BasketItem;
import server.model.Book;
import server.model.BookInfo;
import server.model.Enums.BookType;
import server.model.Enums.Genre;
import server.model.Rental;
import server.service.restHelping.BasketItemDTO;

import java.util.List;
import java.util.Map;

public interface IServiceClient {
    public int numberOfItemsInBasket(String username);
    void register(String username,String password,String conf_password,String email,String FirstName,String LastName,String CPN,String Address,String City,String PostalCode,String Phone);
    Map<BookType, List<BookInfo>> getTopBooksCategories();
    public int getNrOfItemsInStock(Long id);
    public int getQuantityOfBookInBasket(BookInfo bookInfo,String username);
    public int getBooksInBasket(String username);
    List<BookInfo> filterBooksBYCriteria(List<String> criterias, List<String> values);
    List<BookInfo> searchBooks(String searchContent);
    List<BasketItemDTO> getBasketItems(String username);
    List<BookInfo> getTopBooksForCategory(BookType bookType);
    List<BookInfo> getBooksForGenre(Genre genre);
    List<BookInfo> getBooksForType(BookType bookType);
    void addBookToBasket(BookInfo book,int nrOfCopies,String username);
    void removeBookFromBasket(BasketItemDTO book,String username);
    void updateBookQuantity(BasketItemDTO book,int quantity,String username);

    void finishOrder(String username);

}
