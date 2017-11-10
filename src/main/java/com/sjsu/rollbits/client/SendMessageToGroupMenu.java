/**
 * 
 */
package com.sjsu.rollbits.client;

/**
 * @author nishantrathi
 *
 */
public class SendMessageToGroupMenu implements Menu {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		System.out.println("Enter message:");
		System.out.println("Sending Message");
		System.out.println("Message sent to Group Successfully");
		try {
			Thread.sleep(5 * 1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RollbitsClient.getInstance().setMenu(new MainMenu());
	}

}
