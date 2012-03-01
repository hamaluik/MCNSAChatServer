package com.mcnsa.chatserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatListenConnection extends Thread {
	private Socket connection = null;
	private boolean done = false;
	
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
			while(!done) {
				try {
					// prefix info and grab a line from the client
					String line = in.readLine();
					// and broadcast it back out!
					MCNSAChatServer.broadcast(line, this);
				}
				catch(Exception e) {
					// close our buffered read
					in.close();
					// disconnect this client
					MCNSAChatServer.disconnect(this);
					// and finish this thread
					done = true;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
