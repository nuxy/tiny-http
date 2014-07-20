/**
 *  tiny-http (Logger.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013-2014, Marc S. Brooks (http://mbrooks.info)
 *  Licensed under the MIT license:
 *  http://www.opensource.org/licenses/mit-license.php
 */
package com.tiny.http;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Logger extends Handler {
	FileOutputStream outputStream;
	PrintWriter      printWriter;

	/**
	 * Define custom log hander
	 * @param String file
	 */
	public Logger(String file) {
		super();

		try {
			outputStream = new FileOutputStream(file);
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

		printWriter.println(formatStr(record));

		flush();
	}

	/**
	 * Return a custom formatter string
	 * @param LogRecord record
	 * @return String
	 */
	private String formatStr(LogRecord record) {
		return new java.util.Date() + "\t" + record.getLevel() + "\t" + record.getMessage();
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
