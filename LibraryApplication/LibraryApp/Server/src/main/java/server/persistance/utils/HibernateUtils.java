package server.persistance.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
    private static final Logger logger=LogManager.getLogger();

    public static SessionFactory sessionFactory;
    public static SessionFactory getSession() {
        logger.traceEntry() ;
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
               sessionFactory=getNewSession();
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
            }
        }
        logger.traceExit();
        return sessionFactory;
    }
    public static SessionFactory getNewSession() {
        SessionFactory ses = null;
        logger.traceEntry();
        try{
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            ses= new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e)
        {
            logger.error(e);
            System.out.println("Error getting connection!"+ e);
        }
        return ses;
    }

}
