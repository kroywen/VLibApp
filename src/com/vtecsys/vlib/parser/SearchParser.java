package com.vtecsys.vlib.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.model.SearchResult;

public class SearchParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		SearchResult result = new SearchResult();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESULTS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagName = parser.getName();
			if (TAG_HITS.equalsIgnoreCase(tagName)) {
				int hits = readInt(parser, TAG_HITS);
				result.setHits(hits);
			} else if (TAG_LOADED.equalsIgnoreCase(tagName)) {
				int loaded = readInt(parser, TAG_LOADED);
				result.setLoaded(loaded);
			} else if (TAG_ONE_RID.equalsIgnoreCase(tagName)) {
				Book book = readBook(parser);
				result.addBook(book);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESULTS);
		return result;
	}
	
	private Book readBook(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		Book book = new Book();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_ONE_RID);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_RID.equalsIgnoreCase(tagName)) {
				String rid = readString(parser, TAG_RID);
				book.setRID(rid);
			} else if (TAG_TITLE.equalsIgnoreCase(tagName)) {
				String title = readString(parser, TAG_TITLE);
				book.setTitle(title);
			} else if (TAG_AUTHOR.equalsIgnoreCase(tagName)) {
				String author = readString(parser, TAG_AUTHOR);
				book.setAuthor(author);
			} else if (TAG_PUBLICATION.equalsIgnoreCase(tagName)) {
				String publication = readString(parser, TAG_PUBLICATION);
				book.setPublication(publication);
			} else if (TAG_CALL_NO.equalsIgnoreCase(tagName)) {
				String callNumber = readString(parser, TAG_CALL_NO);
				book.setCallNumber(callNumber);
			} else if (TAG_BOOK_COVER.equalsIgnoreCase(tagName)) {
				String bookCover = readString(parser, TAG_BOOK_COVER);
				book.setBookCover(bookCover);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_RID);
		return book;
	}

}
