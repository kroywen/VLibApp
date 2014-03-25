package com.example.vlibapp.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SiteNameParser extends ApiParser {
	
	public static final String TAG_SITE_NAME = "SiteName";
	
	@Override
	public Object readData(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		String siteName = null;
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPONSE_DATA);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (tagName.equalsIgnoreCase(TAG_SITE_NAME)) {
				siteName = readSiteName(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPONSE_DATA);		
		return siteName;
	}
	
	private String readSiteName(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, namespace, TAG_SITE_NAME);
		String siteName = readText(parser);
		parser.require(XmlPullParser.END_TAG, namespace, TAG_SITE_NAME); 
		return siteName;
	}

}
