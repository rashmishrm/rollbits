package com.sjsu.rollbits.dao.interfaces;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;



public class MessageDao implements DaoInterface<Message, int> {
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

    public Message findById(int id) {
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

    public void deleteAll() {
        List<Message> entityList = findAll();
        for (Meesage entity : entityList) {
            delete(entity);
        }

    }
}
