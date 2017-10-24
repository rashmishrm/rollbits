package com.sjsu.rollbits.dao.interfaces.service;

import com.sjsu.rollbits.dao.interfaces.model.User;
import com.sjsu.rollbits.dao.interfaces.UserDao;

import java.util.List;
public class Service {

    private static UserDao userDao;
    public Service() {
        userDao = new UserDao();
    }
    public void persist(User entity) {
        userDao.openCurrentSessionwithTransaction();
        userDao.persist(entity);
        userDao.closeCurrentSessionwithTransaction();
    }
    public void update(User entity) {
        userDao.openCurrentSessionwithTransaction();
        userDao.update(entity);
        userDao.closeCurrentSessionwithTransaction();
    }

    public User findById(int id) {
        userDao.openCurrentSession();
        User user = userDao.findById(id);
        userDao.closeCurrentSession();
        return user;
    }
    public void delete(int id) {
        userDao.openCurrentSessionwithTransaction();
        User User = userDao.findById(id);
        userDao.delete(User);
        userDao.closeCurrentSessionwithTransaction();
    }

    public List<User> findAll() {
        userDao.openCurrentSession();
        List<User> Users = userDao.findAll();
        userDao.closeCurrentSession();
        return Users;
    }
    public void deleteAll() {
        userDao.openCurrentSessionwithTransaction();
        userDao.deleteAll();
        userDao.closeCurrentSessionwithTransaction();
    }
    public userDao UserDao() {
        return userDao;

    }

}
