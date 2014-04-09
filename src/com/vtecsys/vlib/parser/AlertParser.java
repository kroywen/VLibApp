package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.Notices;

import android.util.Xml;

public class AlertParser extends ApiParser {
	
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

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		Notices notices = null;
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPONSE_DATA);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagName = parser.getName();
			if (TAG_NOTICES.equalsIgnoreCase(tagName)) {
				notices = readNotices(parser);
			} else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPONSE_DATA);
		return notices;
	}
	
	private Notices readNotices(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		Notices notices = new Notices();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_NOTICES);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_PRE_DUE.equalsIgnoreCase(tagName)) {
				String predue = readString(parser, TAG_PRE_DUE);
				notices.setPredue(predue);
			} else if (TAG_DUE.equalsIgnoreCase(tagName)) {
				String due = readString(parser, TAG_DUE);
				notices.setDue(due);
			} else if (TAG_OVER_DUE.equalsIgnoreCase(tagName)) {
				String overdue = readString(parser, TAG_OVER_DUE);
				notices.setOverdue(overdue);
			} else if (TAG_COLLECTION.equalsIgnoreCase(tagName)) {
				String collection = readString(parser, TAG_COLLECTION);
				notices.setCollection(collection);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_NOTICES);
		return notices;
	}

}
