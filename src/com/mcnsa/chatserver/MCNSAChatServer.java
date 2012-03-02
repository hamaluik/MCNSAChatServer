package com.mcnsa.chatserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MCNSAChatServer {
	private static ServerSocket servSock = null;
	private static boolean quit = false;
	private static ArrayList<ChatListenConnection> connections = new ArrayList<ChatListenConnection>();
	
	public static void main(String argv[]) throws Exception {
		// get the desired port
		Integer port = 9345;
		if(argv.length == 1) {
			try {
				port = Integer.parseInt(argv[0]);
			}
			catch(Exception e) {}
		}
		
		// create the server socket
		servSock = new ServerSocket(port);
		System.out.println("Server started on port " + port);
		
		// and begin listening!
		while(!quit) {
			// accept a new connection
			Socket connection = servSock.accept();
			if(connection != null) {
				System.out.println("Accepted a new connection on: " + connection.getInetAddress() + ":" + connection.getPort());
				ChatListenConnection client = new ChatListenConnection(connection);
				connections.add(client);
				client.start();
			}
		}
	}
	
	public static void broadcast(String message, ChatListenConnection omitConnection) throws IOException {
		// loop through all connections
		for(int i = 0; i < connections.size(); i++) {
			// make sure we're not broadcasting back to the sender
			if(omitConnection.equals(connections.get(i))) {
				continue;
			}
			
			// get the socket
			Socket connection = connections.get(i).getSocket();
			
			// construct an output stream
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			
			// and write to it!
			out.writeBytes(message + "\n");
			out.flush();
		}
		
		// locally record the message
		System.out.println("{" + omitConnection.getSocket().getInetAddress() + ":" + omitConnection.getSocket().getPort() + "} " + message);
	}
	
	public static void disconnect(ChatListenConnection connection) throws IOException {
		// write a disconnect message
		System.out.println("Client on: " + connection.getSocket().getInetAddress() + ":" + connection.getSocket().getPort() + " disconnected!");
		// close the socket
		connection.getSocket().close();
		// remove the connection from our list
		connections.remove(connection);
	}
}
