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
		// create the server socket
		servSock = new ServerSocket(2288);
		System.out.println("Server started on port 2288");
		
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
	
	public static void broadcast(String message) throws IOException {
		// loop through all connections
		for(int i = 0; i < connections.size(); i++) {
			// get the socket
			Socket connection = connections.get(i).getSocket();
			
			// construct an output stream
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			
			// and write to it!
			out.writeBytes(message + "\n");
			out.flush();
		}
		
		// locally record the message
		System.out.println(message);
	}
	
	public static void disconnect(ChatListenConnection connection) throws IOException {
		// create a disconnect message
		String message = new String("Client on: " + connection.getSocket().getInetAddress() + ":" + connection.getSocket().getPort() + " disconnected!");
		// close the socket
		connection.getSocket().close();
		// remove the connection from our list
		connections.remove(connection);
		// and broadcast the result
		broadcast(message);
	}
}
