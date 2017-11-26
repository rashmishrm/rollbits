package com.sjsu.rollbits.dao.interfaces;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sjsu.rollbits.dao.interfaces.model.Group;
import com.sjsu.rollbits.dao.interfaces.service.HibernateUtil;



public class GroupDao {
    private Session currentSession;
    private Transaction currentTransaction;
    public GroupDao() {
    }
    public Session openCurrentSession() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
        return currentSession;
    }
    public Session openCurrentSessionwithTransaction() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
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
    	String q="from Group  grp where grp.name =:groupName";
		Query query = getCurrentSession().createQuery(q);
		query.setParameter("groupName",groupName);
    	List<Group> groupList = query.list();
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
