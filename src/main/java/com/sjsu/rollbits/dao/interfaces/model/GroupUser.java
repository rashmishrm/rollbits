package com.sjsu.rollbits.dao.interfaces.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Group")
public class GroupUser {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    @Column(name = "groupid")
    private int groupid;
    @Column(name = "userid")
    private int userid;


    public GroupUser() {
		// TODO Auto-generated constructor stub
	}
    
    public GroupUser(int groupid, int userid) {
        this.groupid = groupid;
        this.userid=userid;
    }

    public int getGroupid() {
        return groupid;
    }


    public int getUserid() {
        return userid;
    }





}