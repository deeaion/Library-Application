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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.model.Credentials;
import server.model.Credentials;
import server.model.Validators.CredentialsValidator;
import server.persistance.interfaces.ICredentialsRepository;
import server.persistance.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
@Repository

public class CredentialsRepository implements ICredentialsRepository {
    private DBUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private static SessionFactory sessionFactory;
    @Autowired
    private CredentialsValidator credentialsValidator;

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

    public List<Credentials> findAllUtil(List<String> attributes, List<Object> values) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        List<Credentials> Credentialss = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query<Credentials> query=null;
            if(attributes.size()>0)
            {
                StringBuilder sql = new StringBuilder("from Credentials where ");
                //setez values
                for (int i = 0; i < attributes.size(); i++) {
                    sql.append(attributes.get(i)).append(" = :value").append(i);
                    if (i != attributes.size() - 1) {
                        sql.append(" and ");
                    }
                }

                query= session.createQuery(sql.toString(), Credentials.class);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter("value" + i, values.get(i));
                }
            }
            else
            {
                query = session.createQuery("from Credentials", Credentials.class);
            }

            Credentialss = query.list();
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

        logger.traceExit(Credentialss);
        return Credentialss;
    }
    @Override
    public Credentials findByUsername(String username) {
        logger.traceEntry();
        List<Credentials> Credentialss = findAllUtil(List.of("username"), List.of(username));
        if(Credentialss.size() == 0)
            return null;
        logger.traceExit(Credentialss.get(0));
        return Credentialss.get(0);
    }

    @Override
    public Credentials findByEmail(String email) {
        logger.traceEntry();
        List<Credentials> Credentialss = findAllUtil(List.of("email"), List.of(email));
        if(Credentialss.size() == 0)
            return null;
        logger.traceExit(Credentialss.get(0));
        return Credentialss.get(0);
    }

    @Override
    public Credentials add(Credentials obj) {
        logger.traceEntry("saving object {} ",obj);
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
            logger.error("Error saving object in DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public Credentials remove(Long aLong) {
        if(aLong==null)
            throw new IllegalArgumentException("Id doesn t exist");
        logger.traceEntry("Removing object with {}",aLong);
        Session session = getSession().openSession();
        Transaction tx = null;
        Credentials obj = null;
        try {
            tx = session.beginTransaction();
            obj = session.get(Credentials.class, aLong);
            session.remove(obj);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error removing object from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;

    }

    @Override
    public Credentials update(Credentials obj) {
        if(obj==null)
            throw new IllegalArgumentException("Entity doesn t exist");
        logger.traceEntry("Updating object {}",obj);
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
            logger.error("Error updating object in DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public Credentials get(Long aLong) {
        if(aLong==null)
            throw new IllegalArgumentException("Id doesn t exist");
        logger.traceEntry("Getting object with id {}",aLong);
        List<String> attributes = new ArrayList<>();
        attributes.add("id");
        List<Credentials> Credentialss = findAllUtil(attributes, List.of(aLong));
        if(Credentialss.size() == 0)
            return null;
        logger.traceExit(Credentialss.get(0));
        return Credentialss.get(0);


    }

    @Override
    public Iterable<Credentials> getAll() {
        logger.traceEntry();
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        List<Credentials> Credentialss = findAllUtil(attributes, values);
        logger.traceExit(Credentialss);
        return Credentialss;
    }
}
