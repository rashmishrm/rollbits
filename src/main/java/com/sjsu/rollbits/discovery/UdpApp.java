package com.sjsu.rollbits.discovery;



import java.io.File;

public class UdpApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
//		if (args.length == 0) {
//			System.out.println("usage: server <config file>");
//			System.exit(1);
//		}

        File cf = new File("/Users/rashmisharma/Documents/GitHub/rollbits/src/main/resources/routing.conf");
        try {
          //  UdpServer svr = new UdpServer(cf);
         //   svr.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("server closing");
        }
    }
}
