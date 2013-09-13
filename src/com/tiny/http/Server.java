/**
 *  tiny-http (Server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 *  Licensed under the MIT license:
 *  http://www.opensource.org/licenses/mit-license.php
 */
package com.tiny.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.MimetypesFileTypeMap;

public class Server implements Runnable {
	private Config config;
	private Logger logger;
	private String docRoot;

	/**
	 * Define required fields
	 * @param Config c
	 */
	public Server(Config c) {
		config = c;
		logger = new Logger(config.getOptionValByName("Server","LogFile"));
	}

	/**
	 * Start the server instance
	 */
	public void run() {
		String port = config.getOptionValByName("Server","ListenPort");

		logger.publish(new LogRecord(Level.INFO, "Starting server on port " + port));

		try {
			ServerSocket listener = new ServerSocket(Integer.valueOf(port));

			try {
				logger.publish(new LogRecord(Level.INFO, "Waiting for client requests..."));

				while (true) {
					reqHandler(listener.accept());
				}
			}
			finally {
				listener.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle the HTTP client request
	 * @param Socket socket
	 */
	private void reqHandler(Socket socket) {
		SocketAddress clientIp = socket.getRemoteSocketAddress();

		try {
			InputStreamReader input  = new InputStreamReader(socket.getInputStream());
			BufferedReader    buffer = new BufferedReader(input);
			DataOutputStream  output = new DataOutputStream(socket.getOutputStream());

			processReq(buffer, output, clientIp);

			output.close();
			socket.close();
		}
		catch (IOException e) {
			logger.publish(new LogRecord(Level.WARNING, e.getMessage()));
		}
	}

	/**
	 * Process the HTTP request header; send client a valid response
	 * @param BufferedReader   input
	 * @param DataOutputStream output
	 * @param SocketAddress    clientIp
	 */
	private void processReq(BufferedReader input, DataOutputStream output, SocketAddress clientIp) throws IOException {
		String text = input.readLine();

		// parse request string
		Pattern p = Pattern.compile("^(GET)\\s([\\/\\w&+%-.]{1,2000})\\s(HTTP\\/1.[0-1])$");
		Matcher m = p.matcher(text);

		String[] fields = new String[2];

		while (m.find()) {
			fields = new String[]{ m.group(1), m.group(2), m.group(3) };
		}

		String method = fields[0];
		String path   = fields[1];
		String proto  = fields[2];

		// validate string arguments
		if (method == null || path.contains("..") || proto == null) {
			output.writeBytes(genError(403));
			return;
		}

		// send requested file, if exists
		try {
			docRoot = config.getOptionValByName("Website","DocumentRoot");

			String f = docRoot + path;

			if (path.endsWith("/")) {
				f += config.getOptionValByName("Website","DirectoryIndex");
			}

			StringBuffer file = getFile(f);

			if (file.length() == 0) {
				output.writeBytes(genError(404));
				return;
			}

			MimetypesFileTypeMap map = new MimetypesFileTypeMap("mime.types");

			String name = new File(f).getName();
			String type = map.getContentType(name);

			output.writeBytes(genHeader(200, type) + "\r\n" + file.toString());

			logger.publish(new LogRecord(Level.INFO, clientIp.toString().split("/")[1] + "\t" + method + " - " + f));
		}
		catch (IOException e) {
			output.writeBytes(genError(500));
		}
	}

	/**
	 * Retrieve contents of a file for a given path
	 * @param  String path
	 * @return StringBuffer
	 */
	private StringBuffer getFile(String path) {
		StringBuffer content = new StringBuffer("");

		try {
			int ch = 0;

			FileInputStream file = new FileInputStream(path);

			while ((ch = file.read()) != -1) {
				content.append((char)ch);
			}

			file.close();
		}
		catch (IOException e) {
			logger.publish(new LogRecord(Level.WARNING, e.getMessage()));
		}

		return content;
	}

	/**
	 * Generate the HTTP header response
	 * @param  int    statusCode
	 * @param  String mimeType
	 * @return String
	 */
	private String genHeader(int statusCode, String mimeType) {
		String res = "HTTP/1.0 ";

		switch (statusCode) {
			case 200:
				res += "200 OK";
			break;

			case 403:
				res += "403 Forbidden";
			break;

			case 404:
				res += "404 Not Found";
			break;

			case 500:
				res += "500 Internal Server Error";
			break;
		}

		res += "\r\n";
		res += "Content-Type: " + ((mimeType != null) ? mimeType : "text/html");
		res += "\r\n";

		return res;
	}

	/**
	 * Generate error response, includes HTTP header
	 * @param  int statusCode
	 * @return String
	 */
	private String genError(int statusCode) throws IOException {
		String res = new String(genHeader(statusCode, null));

		res += "\r\n";

		StringBuffer file = getFile(docRoot + "/error-" + statusCode + ".html");

		if (file.length() != 0) {
			res += file;
		}

		return res;
	}
}
