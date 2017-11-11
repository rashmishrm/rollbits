/**
 * 
 */
package com.sjsu.rollbits.client;

/**
 * @author nishantrathi
 *
 */
public class CheckMyMessagesMenu implements Menu {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		System.out.println("Enter emailid:");
		System.out.println("Fetching your messages");
		System.out.println("Fetched Successfully");
		try {
			Thread.sleep(5 * 1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RollbitsClient.getInstance().setMenu(new MainMenu());
	}

}
