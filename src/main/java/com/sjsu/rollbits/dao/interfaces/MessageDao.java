package com.sjsu.rollbits.dao.interfaces;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sjsu.rollbits.dao.interfaces.model.Message;
import com.sjsu.rollbits.dao.interfaces.service.HibernateUtil;

public class MessageDao {
	private Session currentSession;
	private Transaction currentTransaction;

	public MessageDao() {
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
		String messages = "FROM Message msg WHERE msg.touserid = :uname";
		Query query = getCurrentSession().createQuery(messages);
		query.setParameter("uname", uname);
		// System.out.println(hql);
		// List<Message> message = (List<Message>)
		// getCurrentSession().createQuery(hql).list();
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Message> findAllFromUname(String uname) {
		String messages = "FROM Message msg WHERE msg.fromuserid = :uname";
		Query query = getCurrentSession().createQuery(messages);
		query.setParameter("uname", uname);
		// System.out.println(hql);
		// List<Message> message = (List<Message>)
		// getCurrentSession().createQuery(hql).list();
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Message> findAllMessages(String uname, List<String> groups) {

		String messages = "FROM Message msg WHERE msg.fromuserid = :uname or msg.togroupid in :glist";
		Query query = getCurrentSession().createQuery(messages);
		query.setParameter("uname", uname);
		query.setParameterList("glist", groups);
		// System.out.println(hql);
		// List<Message> message = (List<Message>)
		// getCurrentSession().createQuery(hql).list();
		return query.list();
	}

	public void deleteAll() {
		List<Message> entityList = findAll();
		for (Message entity : entityList) {
			delete(entity);
		}

	}
}
