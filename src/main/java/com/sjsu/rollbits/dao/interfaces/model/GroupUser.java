package com.sjsu.rollbits.dao.interfaces.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GroupUser")
public class GroupUser {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    

    @Column(name = "id")
    private int id;
    @Column(name = "groupid")
    private String groupid;
    @Column(name = "userid")
    private String userid;


    public GroupUser() {
		// TODO Auto-generated constructor stub
	}
    
    public GroupUser(String gname, String uname) {
        this.groupid = gname;
        this.userid= uname;
    }

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGroupid() {
        return groupid;
    }


    public String getUserid() {
        return userid;
    }





}