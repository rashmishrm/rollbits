package com.sjsu.rollbits.dao.interfaces;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
     @Column(name= "email")
    private String email;


    public User(int id, String name, String email) {
        this.id = id;
        this.title = title;
        this.email = email;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}