/**
 * 
 */
package com.sjsu.rollbits.client;

import java.util.List;
import java.util.Scanner;

import routing.Pipe.Message;
import routing.Pipe.Route;

import com.sjsu.rollbits.client.serverdiscovery.ClusterDirectory;
import com.sjsu.rollbits.datasync.client.CommListener;
import com.sjsu.rollbits.datasync.client.MessageClient;

/**
 * @author nishantrathi
 *
 */
public class CheckMyMessagesMenu implements Menu, CommListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {

		System.out.println("Enter name:");
		Scanner sc = new Scanner(System.in);
		String name = sc.next();

		System.out.println("Fetching your messages");

		try {
			Thread.sleep(5 * 1000L);
			MessageClient mc = ClusterDirectory.getMessageClient(this);

			List<Message> messages = mc.fetchMessages(name,true);
			if (messages != null) {

				for (Message message : messages) {
					System.out.println("Message from: " + message.getReceiverId() + " : " + message.getPayload());
				}
				if (messages.size() == 0) {
					System.out.println("No Messages for you");
				}

			} else {
				System.out.println("No Messages for you");
			}
			System.out.println("Fetched Successfully");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RollbitsClient.getInstance().setMenu(new MainMenu());
	}

	@Override
	public String getListenerID() {
		// TODO Auto-generated method stub
		return "checkMyMessageListener";
	}

	@Override
	public void onMessage(Route msg) {

	}

}
