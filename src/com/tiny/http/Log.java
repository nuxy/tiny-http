/**
 *  tiny-http (Log.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Log extends Handler {
	FileOutputStream outputStream;
	PrintWriter      printWriter;

	/**
	 * Define custom log hander
	 */
	public Log() {
		Config conf = new Config();
		conf.loadFile("config.xml");

		try {
			outputStream = new FileOutputStream(conf.getOptionValByName("server","enableLog"));
			printWriter  = new PrintWriter(outputStream);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Output the formatted data to the file
	 * @param LogRecord record
	 */
	public void publish(LogRecord record) {
		if (!isLoggable(record)) return;

		printWriter.println(getFormatter().format(record));
	}

	/**
	 * Flush any buffered output
	 */
	public void flush() {
		printWriter.flush();
	}

	/**
	 * Close handler to free associated resources
	 */
	public void close() throws SecurityException {
		printWriter.close();
	}
}