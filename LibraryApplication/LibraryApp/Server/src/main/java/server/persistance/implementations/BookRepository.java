package server.persistance.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import server.model.Book;
import server.model.Enums.StateOfRental;
import server.model.Book;
import server.persistance.interfaces.IBookRepository;
import server.persistance.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
@Repository

public class BookRepository implements IBookRepository {
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
    private List<Book> findAllUtil(List<String> attributes, List<Object> values) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        List<Book> books = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query<Book> query=null;
            if(attributes.size()>0)
            {
                StringBuilder sql = new StringBuilder("from Book where ");
                //setez values
                for (int i = 0; i < attributes.size(); i++) {
                    sql.append(attributes.get(i)).append(" = :value").append(i);
                    if (i != attributes.size() - 1) {
                        sql.append(" and ");
                    }
                }

                query= session.createQuery(sql.toString(), Book.class);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter("value" + i, values.get(i));
                }
            }
            else
            {
                query = session.createQuery("from Book", Book.class);
            }

            books = query.list();
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

        logger.traceExit(books);
        return books;
    }
    @Override
    public Book findByUniqueCode(String uniqueCode) {
        List<String> attributes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        attributes.add("uniqueCode");
        values.add(uniqueCode);
        List<Book> books = findAllUtil(attributes, values);
        if (books.size() == 0)
            return null;
        return books.get(0);
    }

    @Override
    public Iterable<Book> findByBook(Long id_bookInfo) {
        List<String> attributes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        attributes.add("bookInfo.id");
        values.add(id_bookInfo);
        return findAllUtil(attributes, values);
    }

    @Override
    public Iterable<Book> findByBookandState(Long id_bookInfo, StateOfRental state) {
        List<String> attributes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        attributes.add("bookInfo.id");
        attributes.add("state");
        values.add(id_bookInfo);
        values.add(state);
        return findAllUtil(attributes, values);
    }

    @Override
    public Book add(Book obj) {
        if(obj==null)
            throw new IllegalArgumentException("Entity doesn t exist");
        logger.traceEntry("Adding book {}",obj);
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
            logger.error("Error adding book", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public Book remove(Long aLong) {
        if(aLong==null)
            throw new IllegalArgumentException("Id doesn t exist");
        logger.traceEntry("Removing book with {}",aLong);
        Session session = getSession().openSession();
        Transaction tx = null;
        Book book = null;
        try {
            tx = session.beginTransaction();
            book = session.get(Book.class, aLong);
            session.remove(book);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error removing book", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(book);
        return book;
    }

    @Override
    public Book update(Book obj) {
        if(obj==null)
            throw new IllegalArgumentException("Entity doesn t exist");
        logger.traceEntry("Updating book {}",obj);
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
            logger.error("Error updating book", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public Book get(Long aLong) {
        List<String> attributes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        attributes.add("id");
        values.add(aLong);
        List<Book> books = findAllUtil(attributes, values);
        if (books.size() == 0)
            return null;
        return books.get(0);
    }

    @Override
    public Iterable<Book> getAll() {
        logger.traceEntry();
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        List<Book> books = findAllUtil(attributes, values);
        logger.traceExit(books);
        return books;
    }
}
