/**
 * 
 */
package com.sjsu.rollbits.discovery;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author akansha
 *
 */
public class MyConstants {
	
	public static final String GROUP_NAME="Group1";
	public static final String NODE_NAME="nisahnt";
	public static final String NODE_IP="169.254.227.79";
	public static final String NODE_PORT="4567";
	public static final String SECRET="secret";
	public static final String UDP_IP_BROADCAST = "255.255.255.255";
	public static final Integer UDP_PORT = 8888;
	
	public static void main(String args[]){
		String ip;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                System.out.println(iface.getDisplayName() + " " + ip);
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
	}
}
