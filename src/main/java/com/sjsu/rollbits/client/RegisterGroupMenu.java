/**
 * 
 */
package com.sjsu.rollbits.client;

/**
 * @author nishantrathi
 *
 */
public class RegisterGroupMenu implements Menu {

	/* (non-Javadoc)
	 * @see com.sjsu.rollbits.client.Menu#playMenu()
	 */
	@Override
	public void playMenu() {
		System.out.println("Enter group name:");
		System.out.println("Requesting server to creating group");
		System.out.println("Group created Successfully");
		try {
			Thread.sleep(5 * 1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RollbitsClient.getInstance().setMenu(new MainMenu());
	}

}
