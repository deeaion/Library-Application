package server.service;

import server.model.BasketItem;
import server.model.BookInfo;
import server.model.Enums.BookType;
import server.model.Enums.Genre;

import java.util.List;
import java.util.Map;

public interface IServiceClient {
    void register(String username,String password,String conf_password,String email,String FirstName,String LastName,String CPN,String Address,String City,String PostalCode,String Phone);
    Map<Genre, List<BookInfo>> getTopBooksCategories();
    List<BookInfo> filterBooksBYCriteria(List<String> criterias, List<String> values);
    List<BookInfo> searchBooks(String searchContent);
    List<BasketItem> getBasketItems(String username);
    List<BookInfo> getTopBooksForCategory(BookType bookType);
    List<BookInfo> getBooksForGenre(Genre genre);
    List<BookInfo> getBooksForType(BookType bookType);
    void addBookToBasket(BookInfo book,int nrOfCopies,String username);
    void removeBookFromBasket(BookInfo book,String username);
    void updateBookQuantity(BookInfo book,int quantity,String username);
}
