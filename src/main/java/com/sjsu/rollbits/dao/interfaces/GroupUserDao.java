package com.sjsu.rollbits.dao.interfaces;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sjsu.rollbits.dao.interfaces.model.GroupUser;
import com.sjsu.rollbits.dao.interfaces.service.HibernateUtil;

public class GroupUserDao {
	private Session currentSession;
	private Transaction currentTransaction;

	public GroupUserDao() {
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

	@SuppressWarnings("unchecked")
	public List<GroupUser> findGroupsforUser(String uname) {
		String q = "from GroupUser  grpu where grpu.userid =:uname";
		Query query = getCurrentSession().createQuery(q);
		query.setParameter("uname", uname);
		List<GroupUser> groupusers = (List<GroupUser>) query.list();
		return groupusers;
	}

	public void deleteAll() {
		List<GroupUser> entityList = findAll();
		for (GroupUser entity : entityList) {
			delete(entity);
		}

	}
}
