package com.sjsu.rollbits.dao.interfaces.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Group")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;

	public Group() {
		// TODO Auto-generated constructor stub
	}

	public Group(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "group id; " + getId() + " group name: " + getName();
	}

}
