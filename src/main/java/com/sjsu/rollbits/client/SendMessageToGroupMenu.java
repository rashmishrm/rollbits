/**
 * 
 */
package com.sjsu.rollbits.client;

import java.util.Scanner;

import com.sjsu.rollbits.client.serverdiscovery.ExternalClientClusterDirectory;
import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;

import routing.Pipe.Route;

/**
 * @author nishantrathi
 *
 */
public class SendMessageToGroupMenu implements Menu, CommListener {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter From Username:");
		String fromUnm = sc.next();
		System.out.println("Enter To Group name:");
		String toGnm = sc.next();
		System.out.println("Enter Message:");
		String msg = sc.next();
		System.out.println("Sending Message...");
		MessageClient mc = ExternalClientClusterDirectory.getMessageClient(this);
		mc.sendMessage(fromUnm, toGnm, msg, "CLIENT", false);//Need to change for group when message client is updated
	}

	@Override
	public String getListenerID() {
		return "SendMessageToGroupMenu";
	}

	@Override
	public void onMessage(Route msg) {
		System.out.println("Sent message to group Successfully\n"+msg);
		RollbitsClient.getInstance().setMenu(new MainMenu());
		
	}

}
