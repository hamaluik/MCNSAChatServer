package com.mcnsa.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ChatListenConnection extends Thread {
	private Socket connection = null;
	
	public ChatListenConnection(Socket _connection) {
		// store our connection!
		connection = _connection;
	}
	
	public Socket getSocket() {
		return connection;
	}

	// this gets run when the thread runs
	public void run() {
		try {
			// get a buffered reader from the client connection
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			// grab a line
			while(true) {
				try {
					String line = "[" + connection.getInetAddress() + ":" + connection.getPort() + "] " + in.readLine();
					// and broadcast it back out!
					MCNSAChatServer.broadcast(line);
				}
				catch(SocketException e) {
					in.close();
					MCNSAChatServer.disconnect(this);
				}
				catch(IOException e) {
					in.close();
					MCNSAChatServer.disconnect(this);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
