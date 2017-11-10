/**
 * 
 */
package com.sjsu.rollbits.client;

import java.util.Scanner;

/**
 * @author nishantrathi
 *
 */
public class SelectClusterMenu implements Menu {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.ClientMenu#handleClientSelection()
	 */
	@Override
	public void playMenu() {
		System.out.println("Hi, welcome to Rollbits CLient Select CLuster Menu!");
		System.out.println("Please choose one of the options:");
		System.out.println("0. Group1");
		System.out.println("1. Group12");
		System.out.println("2. Group13");
		System.out.println("3. Group74");
		System.out.println("4. Group99");
		System.out.println("\nPlease enter your choice number:");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		System.out.println("Selecting cluster number " + choice);
		try {
			Thread.sleep(5 * 1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RollbitsClient.getInstance().setMenu(new MainMenu());

	}

}
