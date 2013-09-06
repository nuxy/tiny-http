/**
 *  tiny-http (Log.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.io.FileOutputStream;
import java.util.logging.Handler;

public class Log extends Handler {
	FileOutputStream outputStream;

	/**
	 * Define required fields
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
}
