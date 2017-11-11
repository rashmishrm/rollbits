/**
 * 
 */
package com.sjsu.rollbits.client;

/**
 * @author nishantrathi
 *
 */
public class SendMessageToUserMenu implements Menu {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		System.out.println("Enter Message:");
		System.out.println("Sent message to user Successfully");
		try {
			Thread.sleep(5 * 1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RollbitsClient.getInstance().setMenu(new MainMenu());
	}

}
