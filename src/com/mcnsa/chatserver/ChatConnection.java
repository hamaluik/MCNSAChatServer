package com.mcnsa.chatserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatConnection extends Thread {
	private Socket connection = null;
	
	public ChatConnection(Socket _connection) {
		// store our connection!
		connection = _connection;
	}

	// this gets run when the thread runs
	public void run() {
		try {
			// get a buffered reader from the client connection
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			// and an output stream to write to
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			
			// grab a line
			String line = in.readLine();
			// and return it in upper case
			out.writeBytes(line.toUpperCase());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
