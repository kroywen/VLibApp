package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class ChangePasswordParser extends PatronAccountParser {
	
	public void parse(InputStream is) 
		throws XmlPullParserException, IOException 
	{
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(is, "utf-8");
			parser.next();
			
			parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPOND);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String tagName = parser.getName();
					if (tagName.equalsIgnoreCase(TAG_STATUS)) {
						int status = readInt(parser, TAG_STATUS);
						apiResponse.setStatus(status);
					} else if (tagName.equalsIgnoreCase(TAG_MESSAGE)) {
						String message = readString(parser, TAG_MESSAGE);
						apiResponse.setMessage(message);
					} else if (tagName.equalsIgnoreCase(TAG_REQUEST_NAME)) {
						String requestName = readString(parser, TAG_REQUEST_NAME);
						apiResponse.setRequestName(requestName);
					} else if (tagName.equalsIgnoreCase(TAG_RESPONSE_DATA)) {
						Object data = readData(parser);
						apiResponse.setData(data);
					}
				}
				eventType = parser.next();
			}
			parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPOND);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
	}

}
