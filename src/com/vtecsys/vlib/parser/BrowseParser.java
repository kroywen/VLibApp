package com.vtecsys.vlib.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.Auth;
import com.vtecsys.vlib.model.result.BrowseResult;

public class BrowseParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		BrowseResult result = new BrowseResult();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESULTS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagName = parser.getName();
			if (TAG_LOADED.equalsIgnoreCase(tagName)) {
				int loaded = readInt(parser, TAG_LOADED);
				result.setLoaded(loaded);
			} else if (TAG_ONE_AUTH.equalsIgnoreCase(tagName)) {
				Auth auth = readAuth(parser);
				result.addAuth(auth);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESULTS);
		return result;
	}
	
	private Auth readAuth(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		Auth auth = new Auth();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_ONE_AUTH);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_AUTH_NO.equalsIgnoreCase(tagName)) {
				String authNo = readString(parser, TAG_AUTH_NO);
				auth.setAuthNo(authNo);
			} else if (TAG_AUTH_ENTRY.equalsIgnoreCase(tagName)) {
				String authEntry = readString(parser, TAG_AUTH_ENTRY);
				auth.setAuthEntry(authEntry);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_AUTH);
		return auth;
	}

}
