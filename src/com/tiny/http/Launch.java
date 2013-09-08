/**
 *  tiny-http (Launch.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Launch {

	/**
	 * @param String[] args
	 */
	public static void main(String[] args) {
		Config config = new Config();
		config.loadFile("config.xml");

		int threads = new Integer(config.getOptionValByName("Server","ThreadTotal"));

		ExecutorService threadExec = Executors.newFixedThreadPool(threads);

		try {
			Server startServer = new Server(config);
			threadExec.execute(startServer);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
