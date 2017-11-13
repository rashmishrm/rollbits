package com.sjsu.rollbits.raft;

import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.discovery.Node;

import routing.Pipe.Route;

public class RaftListener implements CommListener {
	private Node node;
	private MessageClient mc;
	private Route.Builder msgToSend;
	
	
	
	/**
	 * 
	 */
//	public RaftListener() {
//	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * @return the mc
	 */
	public MessageClient getMc() {
		return mc;
	}

	/**
	 * @param mc the mc to set
	 */
	public void setMc(MessageClient mc) {
		this.mc = mc;
	}

	/**
	 * @return the msgToSend
	 */
	public Route.Builder getMsgToSend() {
		return msgToSend;
	}

	/**
	 * @param msgToSend the msgToSend to set
	 */
	public void setMsgToSend(Route.Builder msgToSend) {
		this.msgToSend = msgToSend;
	}

	/**
	 * @param node
	 * @param mc
	 * @param msgToSend
	 */
	public RaftListener(Node node, MessageClient mc, Route.Builder msgToSend) {
		this.node = node;
		this.mc = mc;
		this.msgToSend = msgToSend;
	}

	@Override
	public String getListenerID() {
		// TODO Auto-generated method stub
		return "raftHelper";
	}

	@Override
	public void onMessage(Route msg) {
		// TODO Auto-generated method stub
		System.out.println("RaftListener On Message printing:\n"+msg);
		//Retry if it is not success
		if(!"success".equals(msg.getPayload())){
			mc.sendProto(msgToSend);
		}
	}
}
