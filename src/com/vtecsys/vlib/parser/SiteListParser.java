package com.vtecsys.vlib.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.Site;
import com.vtecsys.vlib.model.result.SiteListResult;

public class SiteListParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		SiteListResult result = new SiteListResult();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPONSE_DATA);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_COUNT.equalsIgnoreCase(tagName)) {
				int count = readInt(parser, TAG_COUNT);
				result.setCount(count);
			} else if (TAG_ONE_SITE.equalsIgnoreCase(tagName)) {
				Site site = readSite(parser);
				result.addSite(site);
			}
		}
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPONSE_DATA);
		return result;
	}
	
	private Site readSite(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		Site site = new Site();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_ONE_SITE);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_SITE_CODE.equalsIgnoreCase(tagName)) {
				String code = readString(parser, TAG_SITE_CODE);
				site.setCode(code);
			} else if (TAG_SITE_NAME.equalsIgnoreCase(tagName)) {
				String name = readString(parser, TAG_SITE_NAME);
				site.setName(name);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_SITE);
		return site;
	}

}
