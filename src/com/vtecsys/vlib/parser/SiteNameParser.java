package com.vtecsys.vlib.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.result.SiteNameResult;

public class SiteNameParser extends ApiParser {
	
	@Override
	public Object readData(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		SiteNameResult result = new SiteNameResult();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPONSE_DATA);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_SITE_NAME.equalsIgnoreCase(tagName)) {
				String siteName = readString(parser, TAG_SITE_NAME);
				result.setSiteName(siteName);
			} else if (TAG_WEB_OPAC.equalsIgnoreCase(tagName)) {
				String url = readString(parser, TAG_WEB_OPAC);
				result.setUrl(url);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPONSE_DATA);		
		return result;
	}

}
