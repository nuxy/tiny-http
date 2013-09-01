/**
 *  tiny-http (server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.util.*;

public class server implements Runnable {

	/**
	 * Start the server
	 */
	public void run() {
		Map<String, String> confMap = (new config()).parseFile("config.xml");

		for (Map.Entry<String, String> entry : confMap.entrySet()) {
			consoleOut(entry.getValue());
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
