/**
 * 
 */
package com.sjsu.rollbits.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.sjsu.rollbits.client.serverdiscovery.ClusterDirectory;
import com.sjsu.rollbits.client.serverdiscovery.UdpClient;
import com.sjsu.rollbits.client.serverdiscovery.UdpServer;

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
		System.out.println("Loading Available Clusters...");
		Thread udpServer = new Thread(new UdpServer());
		udpServer.start();
		
		try {
			UdpClient.broadcast();
			Thread.sleep(10 * 1000L);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		udpServer.stop();
		Map<Integer,String> menuMap=new HashMap<>();
		Integer i = 1;
		for(String key:ClusterDirectory.getGroupMap().keySet()){
			System.out.println(i + "  =>  " + key);
			menuMap.put(i, key);
			i++;
		}
		
		System.out.println("\nPlease enter your choice number:");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		System.out.println("Selecting cluster number " + choice + "  =>  " + menuMap.get(choice));
		ClusterDirectory.selectClusterGroup(menuMap.get(choice));
		
		System.out.println("Your selection has been saved");
		RollbitsClient.getInstance().setMenu(new MainMenu());
		RollbitsClient.getInstance().play();

	}

}
