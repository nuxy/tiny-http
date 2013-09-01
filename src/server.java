/**
 *  tiny-http (server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
public class server implements Runnable {

	/**
	 * Start the server
	 */
	public void run() {
		consoleOut("Let's get this party started, right?");
	}
	
	/**
	 * Print message to console
	 * @param String msg
	 */
	private void consoleOut(String msg) {
		System.out.println(msg + "\n");
	}
}
