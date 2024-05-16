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
import server.model.Rental;
import server.model.Rental;
import server.persistance.interfaces.IRentalRepository;
import server.persistance.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
@Repository

public class RentalRepository implements IRentalRepository {
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

    public List<Rental> findAllUtil(List<String> attributes, List<Object> values) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        List<Rental> Rentals = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query<Rental> query=null;
            if(attributes.size()>0)
            {
                StringBuilder sql = new StringBuilder("from Rental where ");
                //setez values
                for (int i = 0; i < attributes.size(); i++) {
                    sql.append(attributes.get(i)).append(" = :value").append(i);
                    if (i != attributes.size() - 1) {
                        sql.append(" and ");
                    }
                }

                query= session.createQuery(sql.toString(), Rental.class);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter("value" + i, values.get(i));
                }
            }
            else
            {
                query = session.createQuery("from Rental", Rental.class);
            }

            Rentals = query.list();
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

        logger.traceExit(Rentals);
        return Rentals;
    }
    @Override
    public Iterable<Rental> findRentalsByUser(Long userId) {
        logger.traceEntry();
        List<Rental> rentals = findAllUtil(List.of("rented_by.id"), List.of(userId));
        logger.traceExit(rentals);
        return rentals;
    }

    @Override
    public Rental add(Rental obj) {
        if(obj==null)
            throw new IllegalArgumentException("Entity doesn t exist");
        logger.traceEntry("saving rental {}",obj);
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
            logger.error("Error saving rental", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public Rental remove(Long aLong) {
       if(aLong==null)
              throw new IllegalArgumentException("Id doesn t exist");
          logger.traceEntry("removing rental with {}",aLong);
          Session session = getSession().openSession();
          Transaction tx = null;
          Rental rental = null;
          try {
                tx = session.beginTransaction();
                rental = session.get(Rental.class, aLong);
                session.delete(rental);
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
    public Rental update(Rental obj) {
        if (obj == null) {
            logger.error("Error: null element");
            throw new IllegalArgumentException("Element is null");
        }
        if (obj.getId() == null) {
            logger.error("Error: null id");
            throw new IllegalArgumentException("Id is null");
        }
        logger.traceEntry("Updating rental {}", obj);
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
            logger.error("Error updating rental", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logger.traceExit(obj);
        return obj;
    }

    @Override
    public Rental get(Long aLong) {
       if(aLong==null)
                throw new IllegalArgumentException("Id doesn t exist");
     logger.traceEntry("Getting rental with id {}",aLong);
     List<String> attributes=new ArrayList<>();
        attributes.add("id");
        List<Object> values=new ArrayList<>();
        values.add(aLong);
        List<Rental> rentals=findAllUtil(attributes,values);
        if(rentals.size() == 0)
            return null;
        return rentals.get(0);
    }

    @Override
    public Iterable<Rental> getAll() {
        logger.traceEntry();
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        List<Rental> rentals = findAllUtil(attributes, values);
        logger.traceExit(rentals);
        return rentals;
    }
}
