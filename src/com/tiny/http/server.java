/**
 *  tiny-http (server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.net.ServerSocket;
import java.net.Socket;

public class server implements Runnable {
	private int port;

	/**
	 * Define required fields
	 * @param int listenPort
	 */
	public server(int listenPort) {
		port = listenPort;
	}

	/**
	 * Start the server instance
	 */
	public void run() {
		consoleOut("Starting server on port " + Integer.toString(port));

		int clientNum = 0;

		try {
			ServerSocket listener = new ServerSocket(port);

			try {
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
	 * @param int num
	 */
	private void httpReqHandler(Socket socket, int num) {
		consoleOut("Client connection " + Integer.toString(num) + " available");

		try {
			socket.close();
		}
		catch (Exception e) {
			consoleOut("Internal server error: " + e.getMessage());
		}
	}

	/**
	 * Print message to console
	 * @param String msg
	 */
	private void consoleOut(String msg) {
		System.out.println(msg + "\n");
	}
}
