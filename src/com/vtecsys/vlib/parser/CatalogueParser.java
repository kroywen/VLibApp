package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.model.Volume;
import com.vtecsys.vlib.model.result.CatalogueResult;

public class CatalogueParser extends ApiParser {

	@Override
	public Object readData(XmlPullParser parser) 
		throws XmlPullParserException, IOException 
	{
		CatalogueResult result = new CatalogueResult();
		Book book = new Book();
		
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_RESULTS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tagName = parser.getName();
			if (TAG_BOOK_COVER.equalsIgnoreCase(tagName)) {
				String bookCover = readString(parser, TAG_BOOK_COVER);
				book.setBookCover(bookCover);
			} else if (TAG_TAGS.equalsIgnoreCase(tagName)) {
				readTags(parser, book);
			} else if (TAG_VOLUMES.equalsIgnoreCase(tagName)) {
				readVolumes(parser, result.getVolumes());
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_RESULTS);
		
		result.setBook(book);
		return result;
	}
	
	private void readTags(XmlPullParser parser, Book book)
		throws XmlPullParserException, IOException
	{
		if (book == null) {
			return;
		}
		
		parser.require(XmlPullParser.START_TAG, namespace, TAG_TAGS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_ISBN.equalsIgnoreCase(tagName)) {
				String isbn = readString(parser, TAG_ISBN);
				book.setISBN(isbn);
			} else if (TAG_TITLE.equalsIgnoreCase(tagName)) {
				String title = readString(parser, TAG_TITLE);
				book.setTitle(title);
			} else if (TAG_AUTHOR.equalsIgnoreCase(tagName)) {
				String author = readString(parser, TAG_AUTHOR);
				book.setAuthor(author);
			} else if (TAG_PUBLICATION.equalsIgnoreCase(tagName)) {
				String publication = readString(parser, TAG_PUBLICATION);
				book.setPublication(publication);
			} else if (TAG_CALL_NUMBER.equalsIgnoreCase(tagName)) {
				String callNumber = readString(parser, TAG_CALL_NUMBER);
				book.setCallNumber(callNumber);
			} else if (TAG_EDITION.equalsIgnoreCase(tagName)) {
				String edition = readString(parser, TAG_EDITION);
				book.setEdition(edition);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_TAGS);
	}
	
	private void readVolumes(XmlPullParser parser, List<Volume> volumes)
		throws XmlPullParserException, IOException
	{
		if (volumes == null) {
			return;
		}
		
		parser.require(XmlPullParser.START_TAG, namespace, TAG_VOLUMES);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_ONE_ITEM.equalsIgnoreCase(tagName)) {
				Volume volume = readVolume(parser);
				volumes.add(volume);
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_VOLUMES);
	}
	
	private Volume readVolume(XmlPullParser parser)
		throws XmlPullParserException, IOException
	{
		Volume volume = new Volume();
		parser.require(XmlPullParser.START_TAG, namespace, TAG_ONE_ITEM);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
			String tagName = parser.getName();
			if (TAG_VOLUME.equalsIgnoreCase(tagName)) {
				String vol = readString(parser, TAG_VOLUME);
				volume.setVolume(vol);
			} else if (TAG_ITEM.equalsIgnoreCase(tagName)) {
				String item = readString(parser, TAG_ITEM);
				volume.setItem(item);
			} else if (TAG_STATUS.equalsIgnoreCase(tagName)) {
				String status = readString(parser, TAG_STATUS);
				volume.setStatus(status);
			} else if (TAG_LOCATION.equalsIgnoreCase(tagName)) {
				String location = readString(parser, TAG_LOCATION);
				volume.setLocation(location);
			} else if (TAG_CAN_RESERVE.equalsIgnoreCase(tagName)) {
				String canReserve = readString(parser, TAG_CAN_RESERVE);
				volume.setCanReserve("Y".equalsIgnoreCase(canReserve));
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_ITEM);
		return volume;
	}

}
