/**
 *  tiny-http (thread.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.util.concurrent.*;

public class thread {
	static int threads = 1;

	/**
	 * @param String[] args
	 */
	public static void main(String[] args) {
		ExecutorService threadExec = Executors.newFixedThreadPool(threads);
		
		try {
			server serverStart = new server();

			threadExec.execute(serverStart);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
