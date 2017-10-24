package com.sjsu.rollbits.dao.interfaces.service;

import com.sjsu.rollbits.dao.interfaces.model.User;
import com.sjsu.rollbits.dao.interfaces.UserDao;

import java.util.List;
public class Service {

    private static UserDao userDao;
    public Service() {
        userDao = new UserDao();
    }
    public static void persist(User entity) {
        userDao.openCurrentSessionwithTransaction();
        userDao.persist(entity);
        userDao.closeCurrentSessionwithTransaction();
    }
    public static void update(User entity) {
        userDao.openCurrentSessionwithTransaction();
        userDao.update(entity);
        userDao.closeCurrentSessionwithTransaction();
    }

    public static User findById(int id) {
        userDao.openCurrentSession();
        User user = userDao.findById(id);
        userDao.closeCurrentSession();
        return user;
    }
    public static void delete(int id) {
        userDao.openCurrentSessionwithTransaction();
        User User = userDao.findById(id);
        userDao.delete(User);
        userDao.closeCurrentSessionwithTransaction();
    }

    public static List<User> findAll() {
        userDao.openCurrentSession();
        List<User> Users = userDao.findAll();
        userDao.closeCurrentSession();
        return Users;
    }
    public static void deleteAll() {
        userDao.openCurrentSessionwithTransaction();
        userDao.deleteAll();
        userDao.closeCurrentSessionwithTransaction();
    }
    public UserDao UserDao() {
        return userDao;

    }

}
