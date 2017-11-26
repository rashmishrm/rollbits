package com.sjsu.rollbits.dao.interfaces.service;

import com.sjsu.rollbits.dao.interfaces.model.Group;
import com.sjsu.rollbits.dao.interfaces.GroupDao;

import java.util.List;
public class GroupService {

    private  GroupDao groupDao;
    public GroupService() {
    	groupDao = new GroupDao();
    }
    public  void persist(Group entity) {
    	groupDao.openCurrentSessionwithTransaction();
    	groupDao.persist(entity);
    	groupDao.closeCurrentSessionwithTransaction();
    }
    public  void update(Group entity) {
    	groupDao.openCurrentSessionwithTransaction();
    	groupDao.update(entity);
    	groupDao.closeCurrentSessionwithTransaction();
    }

    public  Group findById(int id) {
    	groupDao.openCurrentSession();
        Group groups = groupDao.findById(id);
        groupDao.closeCurrentSession();
        return groups;
    }
    public  void delete(int id) {
    	groupDao.openCurrentSessionwithTransaction();
        Group groups = groupDao.findById(id);
        groupDao.delete(groups);
        groupDao.closeCurrentSessionwithTransaction();
    }

    public  List<Group> findAll() {
    	groupDao.openCurrentSession();
        List<Group> groups = groupDao.findAll();
        groupDao.closeCurrentSession();
        return groups;
    }
    
    
    public  Boolean findIfAGroupExists(String groupName) {
    	groupDao.openCurrentSession();
        Boolean ifExists = groupDao.checkIfAGroupExists(groupName);
        groupDao.closeCurrentSession();
        return ifExists;
    }
    
    
    public  void deleteAll() {
    	groupDao.openCurrentSessionwithTransaction();
    	groupDao.deleteAll();
    	groupDao.closeCurrentSessionwithTransaction();
    }
    public GroupDao GroupDao() {
        return groupDao;

    }
    
    /*public  void main(String[] args){
    	GroupService gs = new GroupService();
    	Group g = new Group("Hey");
    	gs.persist(g);
    	
    }*/

}
