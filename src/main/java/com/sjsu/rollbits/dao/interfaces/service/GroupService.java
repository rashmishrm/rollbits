package com.sjsu.rollbits.dao.interfaces.service;

import com.sjsu.rollbits.dao.interfaces.model.Group;
import com.sjsu.rollbits.dao.interfaces.GroupDao;

import java.util.List;
public class GroupService {

    private static GroupDao groupDao;
    public GroupService() {
    	groupDao = new GroupDao();
    }
    public static void persist(Group entity) {
    	groupDao.openCurrentSessionwithTransaction();
    	groupDao.persist(entity);
    	groupDao.closeCurrentSessionwithTransaction();
    }
    public static void update(Group entity) {
    	groupDao.openCurrentSessionwithTransaction();
    	groupDao.update(entity);
    	groupDao.closeCurrentSessionwithTransaction();
    }

    public static Group findById(int id) {
    	groupDao.openCurrentSession();
        Group user = groupDao.findById(id);
        groupDao.closeCurrentSession();
        return user;
    }
    public static void delete(int id) {
    	groupDao.openCurrentSessionwithTransaction();
        Group User = groupDao.findById(id);
        groupDao.delete(User);
        groupDao.closeCurrentSessionwithTransaction();
    }

    public static List<Group> findAll() {
    	groupDao.openCurrentSession();
        List<Group> Users = groupDao.findAll();
        groupDao.closeCurrentSession();
        return Users;
    }
    public static void deleteAll() {
    	groupDao.openCurrentSessionwithTransaction();
    	groupDao.deleteAll();
    	groupDao.closeCurrentSessionwithTransaction();
    }
    public GroupDao UserDao() {
        return groupDao;

    }

}
