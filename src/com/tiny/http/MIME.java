/**
 *  tiny-http (MIME.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2014, Marc S. Brooks (http://mbrooks.info)
 *  Licensed under the MIT license:
 *  http://www.opensource.org/licenses/mit-license.php
 */
package com.tiny.http;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

public class MIME {

	/**
	 * Return the content-type of a file
	 * @param  String file
	 * @return String type
	 */
	public static String getContentType(String file) throws IOException {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type             = fileNameMap.getContentTypeFor(file);

		return type;
	}
}
