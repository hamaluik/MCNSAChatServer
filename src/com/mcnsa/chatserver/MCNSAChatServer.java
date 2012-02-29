package com.mcnsa.chatserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MCNSAChatServer {
	private static ServerSocket servSock = null;
	private static boolean quit = false;
	private static ArrayList<ChatConnection> connections = new ArrayList<ChatConnection>();
	
	public static void main(String argv[]) throws Exception {
		// create the server socket
		servSock = new ServerSocket(2626);
		
		// and begin listening!
		while(!quit) {
			// accept a new connection
			Socket connection = servSock.accept();
			if(connection != null) {
				ChatConnection client = new ChatConnection(connection);
				connections.add(client);
				client.start();
			}
		}
	}
}
