package server.persistance.interfaces;

import server.model.Book;
import server.model.Enums.StateOfRental;

public interface IBookRepository extends IRepository<Long, Book>{
    public Book findByUniqueCode(String uniqueCode);
    public Iterable<Book> findByBook(Long id_bookInfo);
    public Iterable<Book> findByBookandState(Long id_bookInfo, StateOfRental state);
}
