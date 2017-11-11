package yml;
import java.sql.SQLException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Settings;
import org.hibernate.internal.SessionFactoryImpl;

//import src.main.resources.hibernate.cfg.xml;

public final class Connection {
 //   private String url= (((SessionFactoryImpl) sessionFactory).getConnectionProvider().getConnection().getMetaData().getURL());
    //private int poolSize;
  
	static String url;
    private static final SessionFactory sessionFactory = buildSessionFactory();
	//SessionFactory f = new Configuration().configure().buildSessionFactory();
	
	 //url=((SessionFactoryImpl) f).getConnectionProvider().getConnection().getMetaData().getURL();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	SessionFactory f = new Configuration().configure().buildSessionFactory();
        	
       	 url=((SessionFactoryImpl) f).getConnectionProvider().getConnection().getMetaData().getURL();

            
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
		return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    	public String getURL()
    	{
    		return url;
    	}
    
   
 
    public void setUrl(String url) {
        this.url = url;
    }
    
    
    public static void main(String[] args) throws SQLException
    {
    	SessionFactory f = new Configuration().configure().buildSessionFactory();
    	url=((SessionFactoryImpl) f).getConnectionProvider().getConnection().getMetaData().getURL();
       
    	System.out.println(url);
    	//System.out.println(sessionFactory.getCurrentSession().getSessionFactory().);
    }

   
 
}
