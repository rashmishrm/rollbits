/**
 * 
 */
package com.sjsu.rollbits.client;

/**
 * @author nishantrathi
 *
 */
public class RollbitsClient {

	private Menu menu = new SelectClusterMenu();
	private static RollbitsClient rollbitsClient;
	
	public static RollbitsClient getInstance(){
		if(rollbitsClient == null){
			rollbitsClient = new RollbitsClient();
		}
		return rollbitsClient;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RollbitsClient.getInstance().play();
	}

	public void play() {
		//while(true){
			menu.playMenu();
		//}
	}

	/**
	 * @return the menu
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * @param menu
	 *            the menu to set
	 */
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	/**
	 * Making this class a singleton
	 */
	private RollbitsClient() {
	}

	
}
