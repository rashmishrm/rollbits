package com.sjsu.rollbits.dao.interfaces;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Message")
public class Message {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "type_id")
    private int typeid;
    @Column(name= "timestamp")
    private Date timestamp;
    @Column(name="fromuserid" )
    private int fromuserid;
    @Column(name= "touserid")
    private int touserid;
    @Column(name= "togroupid")
    private int togroupid;


    String email;



    public Message(int id, int typeid,Date timestamp, int fromuserid,int touserid,int togroupid) {
        this.id = id;
        this.typeid=typeid;
        this.timestamp=timestamp;
        this.fromuserid=fromuserid;
        this.touserid=touserid;
        this.togroupid=togroupid;
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

    public int getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(int fromuserid) {
        this.fromuserid = fromuserid;
    }

    public int getTouserid() {
        return touserid;
    }

    public void setTouserid(int touserid) {
        this.touserid = touserid;
    }

    public int getTogroupid() {
        return togroupid;
    }

    public void setTogroupid(int togroupid) {
        this.togroupid = togroupid;
    }


}