/**
 * 
 */
package com.sjsu.rollbits.client;

import java.util.Scanner;

import com.sjsu.rollbits.client.serverdiscovery.ClusterDirectory;
import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;

import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class RegisterGroupMenu implements Menu, CommListener {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		System.out.println("Enter group name:");
		Scanner sc = new Scanner(System.in);
		String gnm = sc.next();
		System.out.println("Requesting server to creating group");
		MessageClient mc = ClusterDirectory.getMessageClient(this);
		mc.addGroup(gnm, 1, false, false);
	}

	@Override
	public String getListenerID() {
		return "RegisterGroupMenu";
	}

	@Override
	public void onMessage(Route msg) {
		System.out.println("Group created Successfully");
		RollbitsClient.getInstance().setMenu(new MainMenu());
	}

}
