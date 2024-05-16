package server.persistance.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Remove;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import server.model.BookInfo;
import server.model.BookInfo;
import server.model.Enums.Genre;
import server.persistance.interfaces.IBookInfoRepository;
import server.persistance.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
@Repository
public class BookInfoRepository implements IBookInfoRepository {
    private DBUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private static SessionFactory sessionFactory;

    public static SessionFactory getSession() {
        logger.traceEntry();
        try {
            if (sessionFactory == null || sessionFactory.isClosed())
                sessionFactory = getNewSession();
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(sessionFactory);
        return sessionFactory;
    }

    public static SessionFactory getNewSession() {
        SessionFactory ses = null;
        try {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            ses = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }
        return ses;
    }

    public List<BookInfo> findAllUtil(List<String> attributes, List<Object> values) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        List<BookInfo> BookInfos = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query<BookInfo> query=null;
            if(attributes.size()>0)
            {
                StringBuilder sql = new StringBuilder("from BookInfo where ");
                //setez values
                for (int i = 0; i < attributes.size(); i++) {
                    sql.append(attributes.get(i)).append(" = :value").append(i);
                    if (i != attributes.size() - 1) {
                        sql.append(" and ");
                    }
                }

                query= session.createQuery(sql.toString(), BookInfo.class);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter("value" + i, values.get(i));
                }
            }
            else
            {
                query = session.createQuery("from BookInfo", BookInfo.class);
            }

            BookInfos = query.list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error finding all elements in DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        logger.traceExit(BookInfos);
        return BookInfos;
    }
    @Override
    public Iterable<BookInfo> findBookInfoByTitle(String title) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("title");
        values.add(title);
        return findAllUtil(attributes,values);
    }

    @Override
    public Iterable<BookInfo> findBookInfoByAuthor(String author) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("author");
        values.add(author);
        return findAllUtil(attributes,values);
    }

    @Override
    public Iterable<BookInfo> findBookInfoByGenre(String genre) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("genre");
        values.add(Genre.valueOf(genre));
        return findAllUtil(attributes,values);
    }

    @Override
    public Iterable<BookInfo> findBookInfoByYear(int year) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("year");
        values.add(year);
        return findAllUtil(attributes,values);
    }

    @Override
    public Iterable<BookInfo> findBookInfoByPublisher(String publisher) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("publisher");
        values.add(publisher);
        return findAllUtil(attributes,values);
    }

    @Override
    public Iterable<BookInfo> findBookInfoByIsbn(String isbn) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("isbn");
        values.add(isbn);
        return findAllUtil(attributes,values);
    }

    @Override
    public Iterable<BookInfo> findBookInfoByLanguage(String language) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("language");
        values.add(language);
        return findAllUtil(attributes,values);
    }

    @Override
    public BookInfo add(BookInfo obj) {
        if(obj==null)
        {
            logger.error("Error: null BookInfo");
            throw new IllegalArgumentException("BookInfo is null");
        }
        logger.traceEntry("Adding BookInfo {}",obj);
        Session session = getSession().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(obj);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error adding element in DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public BookInfo remove(Long aLong) {
        if(aLong==null)
        {
            logger.error("Error: null id");
            throw new IllegalArgumentException("Id is null");
        }
        logger.traceEntry("Removing BookInfo with {}",aLong);
        Session session = getSession().openSession();
        Transaction tx = null;
        BookInfo bookInfo = null;
        try {
            tx = session.beginTransaction();
            bookInfo = session.get(BookInfo.class, aLong);
            session.remove(bookInfo);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error removing element from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(bookInfo);
        return bookInfo;
    }

    @Override
    public BookInfo update(BookInfo obj) {
        if (obj == null) {
            logger.error("Error: null element");
            throw new IllegalArgumentException("Element is null");
        }
        if (obj.getId() == null) {
            logger.error("Error: null id");
            throw new IllegalArgumentException("Id is null");
        }
        logger.traceEntry("Updating BookInfo {}", obj);
        Session session = getSession().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error updating element in DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public BookInfo get(Long aLong) {
        if(aLong==null)
            throw new IllegalArgumentException("Id doesn t exist");
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("id");
        values.add(aLong);
        List<BookInfo> bookInfos=findAllUtil(attributes,values);
        if(bookInfos.size()==0)
            return null;
        return bookInfos.get(0);
    }

    @Override
    public Iterable<BookInfo> getAll() {
        logger.traceEntry();
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        List<BookInfo> bookInfos = findAllUtil(attributes, values);
        logger.traceExit(bookInfos);
        return bookInfos;
    }

    public Iterable<BookInfo> findBookInfoByType(String string) {
        List<String> attributes=new ArrayList<>();
        List<Object> values=new ArrayList<>();
        attributes.add("type");
        values.add(string);
        return findAllUtil(attributes,values);
    }
}
