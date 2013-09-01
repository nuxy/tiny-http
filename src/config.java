/**
 *  tiny-http (config.java)
 *  A compact, multi-threaded, HTTP server written in Java
 *
 *  Copyright 2013, Marc S. Brooks (http://mbrooks.info)
 */
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class config {

	/**
	 * Parse the XML configuration file
	 * @param  String name
	 * @return ArrayList
	 */
	public Map<String, String> parseFile(String name) {
		Map<String, String> elmMap = new HashMap<String, String>();

		try {
			File confFile = new File(name);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        dBuilder  = dbFactory.newDocumentBuilder();
			Document               doc       = dBuilder.parse(confFile);

			// convert child elements to hashmap
			NodeList nodeList  = doc.getElementsByTagName("server");
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
}
