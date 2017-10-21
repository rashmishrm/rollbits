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
    private int userid;
    public GroupUser(int groupid, int userid) {
        this.groupid = groupid;
        this.userid-userid;
    }

    public int getGroupid() {
        return groupid;
    }


    public int getUserid() {
        return userid;
    }





}