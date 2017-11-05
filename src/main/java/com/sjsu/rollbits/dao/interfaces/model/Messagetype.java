package com.sjsu.rollbits.dao.interfaces.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "msgtype")
public class Messagetype {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "type")
    private int type;

    
    public Messagetype() {
		// TODO Auto-generated constructor stub
	}
    public Messagetype(int id, int type) {
        this.id=id;
        this.type=type;
    }

    public int getId() {
        return id;
    }


    public int getType() {
        return type;
    }
    





}
