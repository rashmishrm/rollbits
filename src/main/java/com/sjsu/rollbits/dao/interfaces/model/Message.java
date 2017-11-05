package com.sjsu.rollbits.dao.interfaces.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "type_id")
    private int typeid;
    @Column(name= "timestamp")
    private Date timestamp;
    @Column(name="fromuserid" )
    private String fromuserid;
    @Column(name= "touserid")
    private String touserid;
    @Column(name= "togroupid")
    private String togroupid;

    @Column(name= "message")
    private String message;
    String email;

    public Message() {
		// TODO Auto-generated constructor stub
	}


    public Message(int id, int typeid,Date timestamp, String fromuserid,String touserid,String togroupid,String message) {
        this.id = id;
        this.typeid=typeid;
        this.timestamp=timestamp;
        this.fromuserid=fromuserid;
        this.touserid=touserid;
        this.togroupid=togroupid;
        this.message=message;
    }
    
    public Message(int typeid,Date timestamp, String fromuserid,String touserid,String togroupid,String message) {
        this.typeid=typeid;
        this.timestamp=timestamp;
        this.fromuserid=fromuserid;
        this.touserid=touserid;
        this.togroupid=togroupid;
        this.message=message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(String fromuserid) {
        this.fromuserid = fromuserid;
    }

    public String getTouserid() {
        return touserid;
    }

    public void setTouserid(String touserid) {
        this.touserid = touserid;
    }

    public String getTogroupid() {
        return togroupid;
    }

    public void setTogroupid(String togroupid) {
        this.togroupid = togroupid;
    }
    
    public String getEmail() {
		return email;
	}
    
    public String getMessage() {
		return message;
	}
    
     public void setMessage(String message) {
		this.message = message;
	}


    public String toString()
    {
        return "message " + getId() + " is for: " + getTouserid() + "from: " + getFromuserid() + "from the group: " +getTogroupid();
    }
}