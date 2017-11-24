/**
 * 
 */
package com.sjsu.rollbits.client;

import java.util.Scanner;

import com.sjsu.rollbits.client.serverdiscovery.ExternalClientClusterDirectory;
import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;
import com.sjsu.rollbits.datasync.server.resources.RollbitsConstants;

import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class RegisterUserMenu implements Menu, CommListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		System.out.println("Enter username:");
		Scanner sc = new Scanner(System.in);
		String unm = sc.next();
		System.out.println("Requesting server to creating user...");
		MessageClient mc = ExternalClientClusterDirectory.getMessageClient(this);
		mc.addUser(unm, unm, RollbitsConstants.CLIENT, false);
	}

	@Override
	public String getListenerID() {
		return "RegisterUserMenu";
	}

	@Override
	public void onMessage(Route msg) {
		System.out.println("User created Successfully\n");
		RollbitsClient.getInstance().setMenu(new MainMenu());
		RollbitsClient.getInstance().play();

	}

}
