package server.persistance.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Repository;
import server.model.BasketItem;
import server.model.Rental;
import server.persistance.interfaces.IBasketItemRepository;

@Repository
public class BasketItemRepository implements IBasketItemRepository {
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
    @Override
    public BasketItem add(BasketItem obj) {
        return null;
    }

    @Override
    public BasketItem remove(Long aLong) {
        if(aLong==null)
            throw new IllegalArgumentException("Id doesn t exist");
        logger.traceEntry("removing basket item with {}",aLong);
        Session session = getSession().openSession();
        Transaction tx = null;
        BasketItem rental = null;
        try {
            tx = session.beginTransaction();
            rental = session.get(BasketItem.class, aLong);
            session.remove(rental);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error removing rental", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(rental);
        return rental;
    }

    @Override
    public BasketItem update(BasketItem obj) {
        return null;
    }

    @Override
    public BasketItem get(Long aLong) {
        return null;
    }

    @Override
    public Iterable<BasketItem> getAll() {
        return null;
    }
}
