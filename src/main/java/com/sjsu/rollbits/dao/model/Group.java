package com.sjsu.rollbits.dao.interfaces;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Group")
public class Group {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    public Group(int id, String name) {
        this.id = id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }



}