/**
 *  tiny-http (Server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private int port;

	/**
	 * Define required fields
	 * @param int listenPort
	 */
	public Server(int listenPort) {
		port = listenPort;
	}

	/**
	 * Start the server instance
	 */
	public void run() {
		consoleOut("Starting server on port " + Integer.toString(port));

		int clientNum = 1;

		try {
			ServerSocket listener = new ServerSocket(port);

			try {
				consoleOut("Waiting for client requests...");

				while (true) {
					httpReqHandler(listener.accept(), clientNum++);
				}
			}
			finally {
				listener.close();
			}
		}
		catch (Exception e) {
			consoleOut("Internal server error: " + e.getMessage());
		}
	}

	/**
	 * Handle the HTTP client request
	 * @param Socket socket
	 * @param int    num
	 */
	private void httpReqHandler(Socket socket, int num) {
		consoleOut("Client " + Integer.toString(num) + " connected");

		try {
			InputStreamReader input  = new InputStreamReader(socket.getInputStream());
			BufferedReader    buffer = new BufferedReader(input);
			DataOutputStream  output = new DataOutputStream(socket.getOutputStream());

			processRequest(buffer, output);

			socket.close();
		}
		catch (IOException e) {
			consoleOut("Internal server error: " + e.getMessage());
		}
	}

	/**
	 * Process the HTTP request header; send client a valid response
	 * @param BufferedReader   input
	 * @param DataOutputStream output
	 */
	private void processRequest(BufferedReader input, DataOutputStream output) {
		try {
			String text = input.readLine();

			consoleOut("Client says: " + text);
		}
		catch (IOException e) {
			consoleOut("Internal server error: " + e.getMessage());
		}

		return;
	}

	/**
	 * Print message to console
	 * @param String msg
	 */
	private void consoleOut(String msg) {
		System.out.println(msg + "\n");
	}
}