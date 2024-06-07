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
import server.model.Librarian;
import server.persistance.interfaces.ILibrarianRepository;
import server.persistance.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
@Repository

public class LibrarianRepository implements ILibrarianRepository {

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

    public List<Librarian> findAllUtil(List<String> attributes, List<Object> values) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        List<Librarian> librarians = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query<Librarian> query=null;
            if(attributes.size()>0)
            {
                StringBuilder sql = new StringBuilder("from Librarian where ");
                //setez values
                for (int i = 0; i < attributes.size(); i++) {
                    sql.append(attributes.get(i)).append(" = :value").append(i);
                    if (i != attributes.size() - 1) {
                        sql.append(" and ");
                    }
                }

                query= session.createQuery(sql.toString(), Librarian.class);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter("value" + i, values.get(i));
                }
            }
            else
            {
                query = session.createQuery("from Librarian", Librarian.class);
            }

            librarians = query.list();
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

        logger.traceExit(librarians);
        return librarians;
    }

    @Override
    public Librarian findByUserId(Long userId) {

        List<String> Attributes = new ArrayList<>();
        List<Object> Values = new ArrayList<>();
        Attributes.add("credentials.id");
        Values.add(userId);
        List<Librarian> librarians = findAllUtil(Attributes, Values);
        if (librarians.size() == 0) {
            return null;
        }
        return librarians.get(0);
    }

    @Override
    public Librarian findByUsername(String username) {
        List<String> Attributes = new ArrayList<>();
        List<Object> Values = new ArrayList<>();
        Attributes.add("credentials.username");
        Values.add(username);
        List<Librarian> librarians = findAllUtil(Attributes, Values);
        if (librarians.size() == 0) {
            return null;
        }
        return librarians.get(0);
    }

    @Override
    public Librarian findByUniqueCode(String uniqueCode) {
        List<String> Attributes = new ArrayList<>();
        List<Object> Values = new ArrayList<>();
        Attributes.add("uniqueCode");
        Values.add(uniqueCode);
        List<Librarian> librarians = findAllUtil(Attributes, Values);
        if (librarians.size() == 0) {
            return null;
        }
        return librarians.get(0);
    }

    @Override
    public Librarian add(Librarian obj) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(obj);
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
        logger.traceExit();
        return obj;
    }

    @Override
    public Librarian remove(Long id) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        Librarian librarian = null;
        try {
            tx = session.beginTransaction();
            librarian = session.get(Librarian.class, id);
            session.remove(librarian);
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
        logger.traceExit(librarian);
        return librarian;

    }

    @Override
    public Librarian update(Librarian obj) {
        logger.traceEntry();
        if(obj==null)
        {
            logger.error("Error: null element");
            throw new IllegalArgumentException("Element is null");

        }
        if(obj.getId()==null)
        {
            logger.error("Error: null id");
            throw new IllegalArgumentException("Id is null");
        }

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
        logger.traceExit();
        return obj;
    }

    @Override
    public Librarian get(Long id) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        Librarian librarian = null;
        try {
            tx = session.beginTransaction();
            librarian = session.get(Librarian.class, id);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error getting element from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(librarian);
        return librarian;
    }

    @Override
    public Iterable<Librarian> getAll() {
        logger.traceEntry();
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        List<Librarian> librarians = findAllUtil(attributes, values);
        logger.traceExit(librarians);
        return librarians;
    }
}
