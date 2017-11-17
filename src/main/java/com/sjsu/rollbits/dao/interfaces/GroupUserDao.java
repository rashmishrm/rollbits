package com.sjsu.rollbits.dao.interfaces;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.sjsu.rollbits.dao.interfaces.model.Group;
import com.sjsu.rollbits.dao.interfaces.model.GroupUser;

import java.util.List;



public class GroupUserDao {
    private Session currentSession;
    private Transaction currentTransaction;
    public GroupUserDao() {
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

    public void persist(GroupUser entity) {
        getCurrentSession().save(entity);
    }
    public void update(GroupUser entity) {
        getCurrentSession().update(entity);
    }

    public GroupUser findById(int id) {
        GroupUser groupusers = (GroupUser) getCurrentSession().get(GroupUser.class, id);
        return groupusers;
    }

    public void delete(GroupUser entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<GroupUser> findAll() {
        List<GroupUser> groupusers = (List<GroupUser>) getCurrentSession().createQuery("from GroupUser").list();
        return groupusers;
    }

    public void deleteAll() {
        List<GroupUser> entityList = findAll();
        for (GroupUser entity : entityList) {
            delete(entity);
        }

    }
}
