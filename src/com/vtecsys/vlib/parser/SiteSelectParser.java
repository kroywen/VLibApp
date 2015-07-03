package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;

import com.vtecsys.vlib.model.Site;

public class SiteSelectParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		Site result = new Site();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_SITE);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_CODE.equalsIgnoreCase(tagName)) {
				String code = readString(parser, TAG_CODE);
				result.setCode(code);
			} else if (TAG_NAME.equalsIgnoreCase(tagName)) {
				String name = readString(parser, TAG_NAME.toLowerCase(Locale.US));
				result.setName(name);
			} else if (TAG_URL.equalsIgnoreCase(tagName)) {
				String url = readString(parser, TAG_URL);
				result.setUrl(url);
			} else if (TAG_ICON.equalsIgnoreCase(tagName)) {
				String icon = readString(parser, TAG_ICON);
				result.setIcon(icon);
			} else if (TAG_SPLASH.equalsIgnoreCase(tagName)) {
				String splash = readString(parser, TAG_SPLASH);
				result.setSplash(splash);
			} else if (TAG_LANGUAGES.equalsIgnoreCase(tagName)) {
				int languages = readInt(parser, TAG_LANGUAGES);
				result.setLanguages(languages);
			} else if (TAG_LANG0.equalsIgnoreCase(tagName)) {
				String lang0 = readString(parser, TAG_LANG0);
				result.setLang0(lang0);
			} else if (TAG_LANG1.equalsIgnoreCase(tagName)) {
				String lang1 = readString(parser, TAG_LANG1);
				result.setLang1(lang1);
			} else if (TAG_LANG2.equalsIgnoreCase(tagName)) {
				String lang2 = readString(parser, TAG_LANG2);
				result.setLang2(lang2);
			} else if (TAG_MSC.equalsIgnoreCase(tagName)) {
				String mscStr = readString(parser, TAG_MSC);
				boolean msc = !TextUtils.isEmpty(mscStr) && mscStr.equalsIgnoreCase("y");
				result.setMsc(msc);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_SITE);
		return result;
	}

}
