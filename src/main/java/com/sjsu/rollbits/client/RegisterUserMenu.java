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
public class RegisterUserMenu implements Menu, CommListener {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		System.out.println("Enter username:");
		Scanner sc = new Scanner(System.in);
		String unm = sc.next();
		System.out.println("Requesting server to creating user...");
		MessageClient mc = ClusterDirectory.getMessageClient(this);
		mc.addUser(unm, unm, false, false);
	}

	@Override
	public String getListenerID() {
		return "RegisterUserMenu";
	}

	@Override
	public void onMessage(Route msg) {
		System.out.println("User created Successfully\n"+msg);
		RollbitsClient.getInstance().setMenu(new MainMenu());
		
	}

}
