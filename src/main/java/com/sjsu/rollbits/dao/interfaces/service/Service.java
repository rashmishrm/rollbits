package com.sjsu.rollbits.dao.interfaces;
import java.util.List;
public class Service {

    private static UserDAO UserDao;
    public Service() {
        UserDao = new UserDAO();
    }
    public void persist(User entity) {
        UserDao.openCurrentSessionwithTransaction();
        UserDao.persist(entity);
        UserDao.closeCurrentSessionwithTransaction();
    }
    public void update(User entity) {
        UserDao.openCurrentSessionwithTransaction();
        UserDao.update(entity);
        UserDao.closeCurrentSessionwithTransaction();
    }

    public User findById(int id) {
        UserDao.openCurrentSession();
        User user = UserDao.findById(id);
        UserDao.closeCurrentSession();
        return user;
    }
    public void delete(String id) {
        UserDao.openCurrentSessionwithTransaction();
        User User = UserDao.findById(id);
        UserDao.delete(User);
        UserDao.closeCurrentSessionwithTransaction();
    }

    public List<User> findAll() {
        UserDao.openCurrentSession();
        List<User> Users = UserDao.findAll();
        UserDao.closeCurrentSession();
        return Users;
    }
    public void deleteAll() {
        UserDao.openCurrentSessionwithTransaction();
        UserDao.deleteAll();
        UserDao.closeCurrentSessionwithTransaction();
    }
    public UserDao UserDao() {
        return UserDao;

    }

}
