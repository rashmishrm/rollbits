package com.sjsu.rollbits.sharding.hashing;

public class Message {
	private String uniqueKey;

	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	public Message(String key) {
		this.uniqueKey=key;
		
	}
	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
}
