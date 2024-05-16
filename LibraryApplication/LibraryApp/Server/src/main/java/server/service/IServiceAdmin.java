package server.service;

import server.model.BookInfo;
import server.model.Librarian;
import server.model.Subscriber;

import java.time.LocalDateTime;
import java.util.List;

public interface IServiceAdmin {
    List<Subscriber> findAllSubscribers();
    void deleteSubscriber(String username);
    Librarian addLibrarian(String username, String email, String lastName, String firstName, String CPN, String gender, String address, String phone, String birthDate);
    void deleteLibrarian(String username);
    void updateLibrarian(String username, String email, String lastName, String firstName, String CPN, String gender, String address, String phone, String birthDate);
    List<Librarian> findAllLibrarians();
    List<BookInfo> findAllBooks();
    void addBookInfo(String title,String isbc,String author,String publisher,String language,String type,String description,String image,int copies,String genre);
    void showBookUnits(String isbc);
    void deleteBookInfo(String isbc);
    void updateBookInfo(String title,String isbc,String author,String publisher,String language,String type,String description,String image,int copies,String genre);

}
