package com.sjsu.rollbits.dao.interfaces;
import com.sjsu.rollbits.dao.interfaces.model.Message;
import com.sjsu.rollbits.dao.interfaces.model.User;
import com.sjsu.rollbits.dao.interfaces.service.MessageService;
import com.sjsu.rollbits.dao.interfaces.service.Service;
import com.sjsu.rollbits.datasync.server.MessageServer;

import java.util.ArrayList;
import java.util.List;


public class MainApp {
    public static void main(String[] args) {
        MessageService service = new MessageService();
       Message m= new Message();
        //m.setFromuserid("dhrumil");
        //m.setTogroupid("ddd");
        ///m.setMessage("this should work");
        //m.setTouserid("nov12user");
        //service.persist(m);
       
       
       ArrayList<Message> msg=(ArrayList<Message>) MessageService.findAllforuname("nov12user");
       ArrayList<Message> msg_from=(ArrayList<Message>) MessageService.findAllfromuname("dhrumil");
        
        
        for(int i=0;i<msg.size();i++)
        {
        	System.out.println(msg.get(i).getMessage());
        }
        
        System.exit(0);

    }

}
