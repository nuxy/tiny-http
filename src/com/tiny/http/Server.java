/**
 *  tiny-http (Server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.MimetypesFileTypeMap;

public class Server implements Runnable {
	private Config config;
	private Log    logger;

	/**
	 * Define required fields
	 * @param Config c
	 */
	public Server(Config c) {
		config = c;
		logger = new Log(config.getOptionValByName("Server","LogFile"));
	}

	/**
	 * Start the server instance
	 */
	public void run() {
		String port = config.getOptionValByName("Server","ListenPort");

		consoleOut("Starting server on port " + port);

		int clientNum = 1;

		try {
			ServerSocket listener = new ServerSocket(new Integer(port));

			try {
				consoleOut("Waiting for client requests...");

				while (true) {
					reqHandler(listener.accept(), clientNum++);
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
	 * @param int    num
	 */
	private void reqHandler(Socket socket, int num) {
		consoleOut("Client " + Integer.toString(num) + " connected");

		try {
			InputStreamReader input  = new InputStreamReader(socket.getInputStream());
			BufferedReader    buffer = new BufferedReader(input);
			DataOutputStream  output = new DataOutputStream(socket.getOutputStream());

			processReq(buffer, output);

			output.close();
			socket.close();
		}
		catch (IOException e) {
			consoleOut("error: " + e.getMessage());
		}
	}

	/**
	 * Process the HTTP request header; send client a valid response
	 * @param  BufferedReader   input
	 * @param  DataOutputStream output
	 */
	private void processReq(BufferedReader input, DataOutputStream output) throws IOException {
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
			output.writeBytes(genHeader(500, null));
			return;
		}

		// send requested file, if exists
		try {
			String f = config.getOptionValByName("Website","DocumentRoot") + path;

			if (path.endsWith("/")) {
				f += config.getOptionValByName("Website","DirectoryIndex");
			}

			StringBuffer file = getFile(f);

			if (file.length() == 0) {
				output.writeBytes(genHeader(404, null));
				return;
			}

			MimetypesFileTypeMap map = new MimetypesFileTypeMap("mime.types");

			String name = new File(f).getName();
			String type = map.getContentType(name);

			output.writeBytes(genHeader(200, type) + "\r\n" + file.toString());
		}
		catch (IOException e) {
			output.writeBytes(genHeader(403, null));
		}
	}

	/**
	 * Retrieve contents of a file for a given path
	 * @param  String path
	 * @return StringBuffer
	 */
	private StringBuffer getFile(String path) throws IOException {
		StringBuffer content = new StringBuffer("");

		int ch = 0;

		FileInputStream file = new FileInputStream(path);

		while ((ch = file.read()) != -1) {
			content.append((char)ch);
		}

		file.close();

		return content;
	}

	/**
	 * Generate the HTTP header response
	 * @param  int    statusCode
	 * @param  String contentType
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
		res += "Content-Type: " + ((mimeType != null) ? mimeType : "text/plain");
		res += "\r\n";

		return res;
	}

	/**
	 * Print message to console
	 * @param String str
	 */
	private void consoleOut(String msg) {
		System.out.println(msg + "\n");
	}
}