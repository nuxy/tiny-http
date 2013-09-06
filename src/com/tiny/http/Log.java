/**
 *  tiny-http (Log.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.io.FileOutputStream;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Log extends Handler {
	FileOutputStream outputStream;

	/**
	 * Define required methods
	 */
	public Log() {
		Config conf = new Config();
		conf.loadFile("config.xml");

		try {
			outputStream = new FileOutputStream(conf.getOptionValByName("server","enableLog"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void publish(LogRecord record) { }
	public void flush() { }
	public void close() { }
}