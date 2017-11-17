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
    private String groupid;
    @Column(name = "userid")
    private String userid;


    public GroupUser() {
		// TODO Auto-generated constructor stub
	}
    
    public GroupUser(String string, String string2) {
        this.groupid = string;
        this.userid=string2;
    }

    public String getGroupid() {
        return groupid;
    }


    public String getUserid() {
        return userid;
    }





}