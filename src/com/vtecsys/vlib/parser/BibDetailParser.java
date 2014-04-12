package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.Tag;

public class BibDetailParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		List<Tag> tags = new ArrayList<Tag>();
		parser.nextTag();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_TAGS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagName = parser.getName();
			if (TAG_ONE_TAG.equalsIgnoreCase(tagName)) {
				Tag tag = readTag(parser);
				tags.add(tag);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_TAGS);
		return tags;
	}
	
	private Tag readTag(XmlPullParser parser) 
		throws XmlPullParserException, IOException
	{
		Tag tag = new Tag();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_ONE_TAG);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_SEQ.equalsIgnoreCase(tagName)) {
				int seq = readInt(parser, TAG_SEQ);
				tag.setSeq(seq);
			} else if (TAG_TAG.equalsIgnoreCase(tagName)) {
				int tagNo = readInt(parser, TAG_TAG);
				tag.setTag(tagNo);
			} else if (TAG_CAPTION.equalsIgnoreCase(tagName)) {
				String caption = readString(parser, TAG_CAPTION);
				tag.setCaption(caption);
			} else if (TAG_CONTENTS.equalsIgnoreCase(tagName)) {
				String contents = readString(parser, TAG_CONTENTS);
				tag.setContents(contents);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_TAG);
		return tag;
	}

}
