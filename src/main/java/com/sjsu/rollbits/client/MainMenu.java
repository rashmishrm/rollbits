/**
 * 
 */
package com.sjsu.rollbits.client;

import java.util.Scanner;

/**
 * @author nishantrathi
 *
 */
public class MainMenu implements Menu {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#handleClientSelection()
	 */
	@Override
	public void playMenu() {
		System.out.println("Hi, welcome to Rollbits CLient Main Menu!");
		System.out.println("Please choose one of the options:");
		System.out.println("1. Register a user");
		System.out.println("2. Register a group");
		System.out.println("3. Send a message to a user");
		System.out.println("4. Send a message to a group");
		System.out.println("5. Check my messages");
		System.out.println("6. Change the server connection");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		
		switch(choice){
		case 1 : 
			System.out.println("Loading menu for Registering a user..");
			RollbitsClient.getInstance().setMenu(new RegisterUserMenu());
			break;
		case 2 : 
			System.out.println("Loading menu Registering a group..");
			RollbitsClient.getInstance().setMenu(new RegisterGroupMenu());
			break;
		case 3 : 
			System.out.println("Loading menu for Sending a message to a user..");
			RollbitsClient.getInstance().setMenu(new SendMessageToUserMenu());
			break;
		case 4 : 
			System.out.println("Loading menu for sending a message to a group..");
			RollbitsClient.getInstance().setMenu(new SendMessageToGroupMenu());
			break;
		case 5 : 
			System.out.println("Loading menu for checking my messages..");
			RollbitsClient.getInstance().setMenu(new CheckMyMessagesMenu());
			break;
		case 6 : 
			System.out.println("Loading menu to change cluster selection..");
			RollbitsClient.getInstance().setMenu(new SelectClusterMenu());
			break;
		default : 
			System.out.println("Invalid choice. Please choose again");
			try {
				Thread.sleep(5 * 1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RollbitsClient.getInstance().play();
			break;
		
		}
	}

}
