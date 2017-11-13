package com.sjsu.rollbits.dao.interfaces;
import com.sjsu.rollbits.dao.interfaces.model.Message;

import com.sjsu.rollbits.dao.interfaces.model.User;
import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.dao.interfaces.service.Service;
import com.sjsu.rollbits.datasync.server.MessageServer;
import com.sjsu.rollbits.dao.interfaces.service.GroupService;
import java.util.ArrayList;
import java.util.List;
import com.sjsu.rollbits.dao.interfaces.model.Group;


public class MainApp {
    public static void main(String[] args) {
        MessageService service = new MessageService();
       Message m= new Message();
        m.setFromuserid("dhrumil");
        m.setTogroupid("ddd");
        m.setMessage("this should work");
        m.setTouserid("nov12user");
        service.persist(m);
       
       GroupService gs = new GroupService();
       Group g = new Group("Hey");
       gs.persist(g);
       
    }

}
