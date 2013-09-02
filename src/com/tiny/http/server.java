/**
 *  tiny-http (server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

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
	}

	/**
	 * Print message to console
	 * @param String msg
	 */
	private void consoleOut(String msg) {
		System.out.println(msg + "\n");
	}
}