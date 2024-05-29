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
import org.springframework.transaction.annotation.Transactional;
import server.model.Person;
import server.model.Person;
import server.model.Validators.PersonValidator;
import server.model.Validators.ValidatorException;
import server.persistance.interfaces.IPersonRepository;
import server.persistance.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
@Repository

public class PersonRepository implements IPersonRepository {
    private DBUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private static SessionFactory sessionFactory;
    @Autowired
    private PersonValidator personValidator;

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

    public List<Person> findAllUtil(List<String> attributes, List<Object> values) {
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        List<Person> Persons = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query<Person> query=null;
            if(attributes.size()>0)
            {
                StringBuilder sql = new StringBuilder("from Person where ");
                //setez values
                for (int i = 0; i < attributes.size(); i++) {
                    sql.append(attributes.get(i)).append(" = :value").append(i);
                    if (i != attributes.size() - 1) {
                        sql.append(" and ");
                    }
                }

                query= session.createQuery(sql.toString(), Person.class);
                for (int i = 0; i < values.size(); i++) {
                    query.setParameter("value" + i, values.get(i));
                }
            }
            else
            {
                query = session.createQuery("from Person", Person.class);
            }

            Persons = query.list();
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

        logger.traceExit(Persons);
        return Persons;
    }
    @Override
    public Person findByCPN(String cnp) {
        if(cnp==null)
            return null;
        logger.traceEntry();
        List<Person> Persons = findAllUtil(List.of("cnp"), List.of(cnp));
        if(Persons.size() == 0)
            return null;
        logger.traceExit(Persons.get(0));
        return Persons.get(0);
    }
    @Override
    public Iterable<Person> findByName(String name) {
        if(name==null)
            return null;
        logger.traceEntry();
        List<Person> Persons = findAllUtil(List.of("name"), List.of(name));
        logger.traceExit(Persons);
        return Persons;
    }
    @Override
    public Person add(Person obj) {
        if (obj == null)
            return null;
        try {
            personValidator.validate(obj);
        } catch (ValidatorException e) {
            logger.error(e);
        }
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(obj);
            session.flush();
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
    public void flush() {
        Session session = getSession().openSession();
        session.flush();
        session.close();
    }
    @Override
    public Person remove(Long aLong) {
        if(aLong==null)
            return null;
        logger.traceEntry();
        Session session = getSession().openSession();
        Transaction tx = null;
        Person person = null;
        try {
            tx = session.beginTransaction();
            person = session.get(Person.class, aLong);
            session.remove(person);
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
        logger.traceExit(person);
        return person;
    }
    @Override
    public Person update(Person obj) {
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
    public Person get(Long aLong) {
        if(aLong==null)
            return null;
        logger.traceEntry();
        List<String> attributes = new ArrayList<>();
        attributes.add("id");
        List<Object> values = new ArrayList<>();
        values.add(aLong);
        List<Person> Persons = findAllUtil(attributes, values);
        logger.traceExit(Persons);
        if(Persons.size()==0)
            return null;
        return Persons.get(0);

    }
    @Override
    public Iterable<Person> getAll() {
        logger.traceEntry();
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        List<Person> Persons = findAllUtil(attributes, values);
        logger.traceExit(Persons);
        return Persons;
    }
}
