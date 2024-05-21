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
import server.model.Credentials;
import server.model.Person;
import server.model.Subscriber;
import server.persistance.interfaces.ISubscriberRepository;
import server.persistance.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
@Repository
public class SubscriberRepository implements ISubscriberRepository {

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

    private List<Subscriber> findAllUtil(List<String> attributes, List<Object> values) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        List<Subscriber> subscribers = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query<Subscriber> query = null;
            if (attributes.size() > 0) {
                StringBuilder sql = new StringBuilder("from Subscriber s ");
                sql.append("left join fetch s.shoppingBasket ");
                sql.append("where ");
                for (int i = 0; i < attributes.size(); i++) {
                    sql.append("s.").append(attributes.get(i)).append(" = :value").append(i);
                    if (i != attributes.size() - 1) {
                        sql.append(" and ");
                    }
                }

                query = session.createQuery(sql.toString(), Subscriber.class);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter("value" + i, values.get(i));
                }
            } else {
                query = session.createQuery("from Subscriber s left join fetch s.shoppingBasket", Subscriber.class);
            }

            subscribers = query.list();
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

        logger.traceExit(subscribers);
        return subscribers;
    }
    @Override
    public Subscriber findByUsername(String username) {
        if (username == null) {
            logger.error("Error: Username is null");
            return null;
        }
        logger.traceEntry();
        List<Subscriber> subscribers = findAllUtil(List.of("credentials.username"), List.of(username));
        if (subscribers.size() == 0) {
            logger.error("Error: Subscriber not found");
            return null;
        }
        logger.traceExit(subscribers.get(0));
        return subscribers.get(0);
    }

    @Override
    public Subscriber findByEmail(String email) {
        if (email == null) {
            logger.error("Error: Email is null");
            return null;
        }
        logger.traceEntry();
        List<Subscriber> Subscribers = findAllUtil(List.of("credentials.email"), List.of(email));
        if (Subscribers.size() == 0) {
            logger.error("Error: Subscriber not found");
            return null;
        }
        logger.traceExit(Subscribers.get(0));
        return Subscribers.get(0);
    }
    @Override
    public Subscriber findByCPN(String CPN) {
        if(CPN==null)
            return null;
        logger.traceEntry();
        List<Subscriber> Subscribers = findAllUtil(List.of("cnp"), List.of(CPN));
        if(Subscribers.size() == 0)
            return null;
        logger.traceExit(Subscribers.get(0));
        return Subscribers.get(0);
    }
    @Override
    public Subscriber findByUniqueCode(String uniqueCode) {
        if(uniqueCode==null)
            return null;
        logger.traceEntry();
        List<Subscriber> Subscribers = findAllUtil(List.of("uniqueCode"), List.of(uniqueCode));
        if(Subscribers.size() == 0)
            return null;
        logger.traceExit(Subscribers.get(0));
        return Subscribers.get(0);
    }
    @Override
    public Subscriber add(Subscriber obj) {
        if (obj == null) return null;
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (obj.getId() != null) {
               session.merge(obj);
            } else {
                session.persist(obj);
            }
            session.flush();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error adding element in DB", e);
        } finally {
            session.close();
        }
        logger.traceExit(obj);
        return obj;
    }
    @Override
    public Subscriber remove(Long aLong) {
        if(aLong==null)
            return null;
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        Subscriber subscriber = null;
        try {
            tx = session.beginTransaction();
            subscriber = session.get(Subscriber.class, aLong);
            session.delete(subscriber);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error removing subscriber", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(subscriber);
        return subscriber;
    }
    @Override
    public Subscriber update(Subscriber obj) {
        if(obj==null)
            return null;
        logger.traceEntry();
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
    public Subscriber get(Long aLong) {
        if(aLong==null)
            return null;
        logger.traceEntry();
        List<Subscriber> Subscribers = findAllUtil(List.of("id"), List.of(aLong));
        if(Subscribers.size() == 0)
            return null;
        logger.traceExit(Subscribers.get(0));
        return Subscribers.get(0);
    }
    @Override
    public Iterable<Subscriber> getAll() {
        logger.traceEntry();
        List<Subscriber> Subscribers = findAllUtil(new ArrayList<>(), new ArrayList<>());
        logger.traceExit(Subscribers);
        return Subscribers;
    }
}
