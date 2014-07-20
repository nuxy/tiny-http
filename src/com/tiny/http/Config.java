/**
 *  tiny-http (Config.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013-2014, Marc S. Brooks (http://mbrooks.info)
 *  Licensed under the MIT license:
 *  http://www.opensource.org/licenses/mit-license.php
 */
package com.tiny.http;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class Config {
	private Document doc = null;

	/**
	 * Load the XML configuration file
	 * @param String name
	 */
	public void loadFile(String name) {
		try {
			File confFile = new File(name);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        dBuilder  = dbFactory.newDocumentBuilder();

			doc = dBuilder.parse(confFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all options for a given group (XML parent element)
	 * @param  String groupName
	 * @return Map<String, String>
	 */
	public Map<String, String> getOptionsByGroup(String groupName) {
		Map<String, String> elmMap = new HashMap<String, String>();

		try {
			NodeList nodeList  = doc.getElementsByTagName(groupName);
			Node     rootElm   = nodeList.item(0);
			NodeList childList = rootElm.getChildNodes();

			for (int i = 0; i < childList.getLength(); i++) {
				Node childElm = childList.item(i);

				if (childElm.getNodeType() == Node.ELEMENT_NODE) {
					elmMap.put(childElm.getNodeName(), childElm.getTextContent());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return elmMap;
	}

	/**
	 * Get option value, by name, for a given group
	 * @param  String groupName
	 * @param  String optName
	 * @return String value
	 */
	public String getOptionValByName(String groupName, String optName) {
		Map<String, String> options = getOptionsByGroup(groupName);

		String value = new String();

		for (Map.Entry<String, String> entry : options.entrySet()) {
			if (entry.getKey() == optName) {
				value = entry.getValue();
				break;
			}
		}

		return value;
	}
}
