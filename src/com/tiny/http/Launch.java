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
		Config conf = new Config();
		conf.loadFile("config.xml");

		int threadTotal = new Integer(conf.getOptionValByName("server","threadTotal"));
		int listenPort  = new Integer(conf.getOptionValByName("server","listenPort"));

		ExecutorService threadExec = Executors.newFixedThreadPool(threadTotal);

		try {
			Server startServer = new Server(listenPort);
			threadExec.execute(startServer);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}