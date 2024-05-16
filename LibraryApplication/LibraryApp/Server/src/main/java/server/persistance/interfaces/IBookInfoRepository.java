package server.persistance.interfaces;

import server.model.BookInfo;

public interface IBookInfoRepository extends IRepository<Long, BookInfo> {
    public Iterable<BookInfo> findBookInfoByTitle(String title);
    public Iterable<BookInfo> findBookInfoByAuthor(String author);
    public Iterable<BookInfo> findBookInfoByGenre(String genre);
    public Iterable<BookInfo> findBookInfoByYear(int year);
    public Iterable<BookInfo> findBookInfoByPublisher(String publisher);
    public Iterable<BookInfo> findBookInfoByIsbn(String isbn);
    public Iterable<BookInfo> findBookInfoByLanguage(String language);

}
