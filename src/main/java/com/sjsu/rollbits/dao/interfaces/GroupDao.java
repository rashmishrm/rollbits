package com.sjsu.rollbits.dao.interfaces;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import com.sjsu.rollbits.dao.interfaces.model.Group;

import java.util.List;



public class GroupDao {
    private Session currentSession;
    private Transaction currentTransaction;
    public GroupDao() {
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

    public void persist(Group entity) {
        getCurrentSession().save(entity);
    }
    public void update(Group entity) {
        getCurrentSession().update(entity);
    }

    public Group findById(int id) {
        Group groups = (Group) getCurrentSession().get(Group.class, id);
        return groups;
    }

    public void delete(Group entity) {
        getCurrentSession().delete(entity);
    }
    
    public Boolean checkIfAGroupExists(String groupName){
    	List<Group> groupList = (List<Group>) getCurrentSession().createQuery("from Group  grp where grp.name =:groupName").list();
    	return groupList.size() == 1 ? true:false;
    }

    @SuppressWarnings("unchecked")
    public List<Group> findAll() {
        List<Group> groups = (List<Group>) getCurrentSession().createQuery("from Group").list();
        return groups;
    }

    public void deleteAll() {
        List<Group> entityList = findAll();
        for (Group entity : entityList) {
            delete(entity);
        }

    }
}
