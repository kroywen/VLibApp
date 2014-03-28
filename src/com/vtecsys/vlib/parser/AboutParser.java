package com.vtecsys.vlib.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AboutParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		String about = null;
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPONSE_DATA);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_ABOUT.equalsIgnoreCase(tagName)) {
				about = readString(parser, TAG_ABOUT);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPONSE_DATA);		
		return about;
	}

}
