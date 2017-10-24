package com.sjsu.rollbits.dao.interfaces;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "msgtype")
public class Messagetype {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "type")
    private int type;
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
