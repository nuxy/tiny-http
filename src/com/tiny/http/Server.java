/**
 *  tiny-http (Server.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
package com.tiny.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server implements Runnable {
	private int port;

	/**
	 * Define required fields
	 * @param int listenPort
	 */
	public Server(int listenPort) {
		port = listenPort;
	}

	/**
	 * Start the server instance
	 */
	public void run() {
		consoleOut("Starting server on port " + Integer.toString(port));

		int clientNum = 1;

		try {
			ServerSocket listener = new ServerSocket(port);

			try {
				consoleOut("Waiting for client requests...");

				while (true) {
					httpReqHandler(listener.accept(), clientNum++);
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
	private void httpReqHandler(Socket socket, int num) {
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
	private void processReq(BufferedReader input, DataOutputStream output) {
		try {
			String text = input.readLine();

			Pattern p = Pattern.compile("^(GET|POST|PUT|DELETE)\\s([\\/\\w&+%-.]{1,2000})\\s(HTTP\\/1.[0-1])$");
			Matcher m = p.matcher(text);

			String[] fields = new String[2];

			while (m.find()) {
				fields = new String[]{ m.group(1), m.group(2), m.group(3) };
			}

			String method = fields[0];
			String path   = fields[1];
			String proto  = fields[2];

			if (method == null || proto == null || path.contains("..")) {
				output.writeBytes(genHeader(501));
				return;
			}

			StringBuffer file = getFile(path);

			if (file.length() == 0) {
				output.writeBytes(genHeader(404));
			}
			else {
				output.writeBytes(genHeader(200) + file);
			}
		}
		catch (IOException e) {
			consoleOut("error: " + e.getMessage());
		}
	}

	/**
	 * Retrieve contents of a file for a given path
	 * @param  String path
	 * @return StringBuffer
	 */
	private StringBuffer getFile(String path) {
		StringBuffer content = new StringBuffer("");

		int ch;

		try {
			FileInputStream file = new FileInputStream(path);

			while ((ch = file.read()) != -1) {
				content.append((char)ch);
			}

			file.close();
		}
		catch (IOException e) {
			consoleOut("error: " + e.getMessage());
		}

		return content;
	}

	/**
	 * Generate the HTTP header response
	 * @param  int statusCode
	 * @return String
	 */
	private String genHeader(int statusCode) {
		String res = "HTTP/1.0 ";

		switch (statusCode) {
			case 200:
				res += "200 OK";
			break;

			case 201:
				res += "201 Created";
			break;

			case 202:
				res += "202 Accepted";
			break;

			case 400:
				res += "400 Bad Request";
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

			case 501:
				res += "501 Not Implemented";
			break;
		}

		res += "\r\n";
		res += "Content-Type: text/html";
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
