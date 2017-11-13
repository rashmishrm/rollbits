package com.sjsu.rollbits.dao.interfaces;

import com.sjsu.rollbits.dao.interfaces.model.Message;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.List;



public class MessageDao {
    private Session currentSession;
    private Transaction currentTransaction;
    public MessageDao() {
    }
    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }
    public Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }
    public void closeCurrentSession() {
        currentSession.close();
    }
    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();

    }
    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        return sessionFactory;

    }
    public Session getCurrentSession() {
        return currentSession;
    }
    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void persist(Message entity) {
        getCurrentSession().save(entity);
    }
    public void update(Message entity) {
        getCurrentSession().update(entity);
    }



    public Message findById(Integer id) {
        Message message = (Message) getCurrentSession().get(Message.class, id);
        return message;
    }

    public void delete(Message entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Message> findAll() {
        List<Message> message = (List<Message>) getCurrentSession().createQuery("from Message").list();
        return message;
    }
    
	@SuppressWarnings("unchecked")
	public List<Message> findAllForUname(String uname) {
		String messages="FROM Message msg WHERE msg.touserid = :uname";
		Query query = getCurrentSession().createQuery(messages);
		query.setParameter("uname",uname);
		//System.out.println(hql);
        //List<Message> message = (List<Message>) getCurrentSession().createQuery(hql).list();
        return query.list();
    }
	
	@SuppressWarnings("unchecked")
	public List<Message> findAllFromUname(String uname) {
		String messages="FROM Message msg WHERE msg.fromuserid = :uname";
		Query query = getCurrentSession().createQuery(messages);
		query.setParameter("uname",uname);
		//System.out.println(hql);
        //List<Message> message = (List<Message>) getCurrentSession().createQuery(hql).list();
        return query.list();
    }

    public void deleteAll() {
        List<Message> entityList = findAll();
        for (Message entity : entityList) {
            delete(entity);
        }

    }
}
