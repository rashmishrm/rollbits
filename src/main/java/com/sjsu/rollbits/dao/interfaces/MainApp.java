package com.sjsu.rollbits.dao.interfaces;
import com.sjsu.rollbits.dao.interfaces.model.User;
import com.sjsu.rollbits.dao.interfaces.service.Service;

import java.util.List;


public class MainApp {
    public static void main(String[] args) {
        Service service = new Service();
        User user1 = new User(1, "abc", "abc@gmail.com");
        User user2 = new User(2, "xyz", "xyz@yahoo.com");
        User user3 = new User(3, "pqr", "pqr@hotmail.com");
        Service.persist(user1);
        Service.persist(user2);
        Service.persist(user3);

        //persist
        List<User> users = Service.findAll();
        for (User user : users) {
            System.out.println("-" + user.toString());
        }

        //update
        user1.setName("ABC");
        Service.update(user1);
        System.out.println("User Updated is =>" +Service.findById(user1.getId()).toString());
        int id1 = user1.getId();

        User another = Service.findById(id1);
        System.out.println("User found with id " + id1 + " is =>" + another.toString());


        int id3 = user3.getId();
        Service.delete(id3);
        System.out.println("Deleted user with id " + id3 + ".");


        List<User> users2 = Service.findAll();
        System.out.println("Users list :");
        for (User user : users2) {
            System.out.println("-" + user.toString());
        }

        Service.deleteAll();
        System.out.println("Users found are: " + Service.findAll().size());
        System.out.println("*** DeleteAll - end ***");
        System.exit(0);

    }

}
