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
public class SendMessageToUserMenu implements Menu, CommListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter From Username:");
		String fromUnm = sc.next();
		System.out.println("Enter To User:");
		String toUnm = sc.next();
		System.out.println("Enter Message:");
		String msg = sc.next();
		System.out.println("Sending Message...");
		MessageClient mc = ExternalClientClusterDirectory.getMessageClient(this);
		mc.sendMessage(fromUnm, toUnm, msg, "CLIENT", false,RollbitsConstants.SINGLE);
	}

	@Override
	public String getListenerID() {
		return "SendMessageToUserMenu";
	}

	@Override
	public void onMessage(Route msg) {
		System.out.println("Sent message to user Successfully\n");
		RollbitsClient.getInstance().setMenu(new MainMenu());
		RollbitsClient.getInstance().play();
	}

}
