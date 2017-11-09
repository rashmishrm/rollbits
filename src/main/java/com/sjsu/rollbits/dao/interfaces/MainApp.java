package com.sjsu.rollbits.dao.interfaces;
import com.sjsu.rollbits.dao.interfaces.model.Message;
import com.sjsu.rollbits.dao.interfaces.model.User;
import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.dao.interfaces.service.Service;
import com.sjsu.rollbits.datasync.server.MessageServer;

import java.util.List;


public class MainApp {
    public static void main(String[] args) {
        MessageService service = new MessageService();
        
      
        
        Message m= new Message();
        m.setFromuserid("rashmishrm");
        m.setTogroupid("ddd");
        m.setMessage("this is message");
        service.persist(m);
       
        System.exit(0);

    }

}
