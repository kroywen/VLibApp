package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Log;

import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.model.CatalogueResult;
import com.vtecsys.vlib.model.Volume;

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
				Log.d("ApiParser", "BookCover: " + bookCover);
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
				Log.d("ApiParser", "ISBN: " + isbn);
				book.setISBN(isbn);
			} else if (TAG_TITLE.equalsIgnoreCase(tagName)) {
				String title = readString(parser, TAG_TITLE);
				Log.d("ApiParser", "Title: " + title);
				book.setTitle(title);
			} else if (TAG_AUTHOR.equalsIgnoreCase(tagName)) {
				String author = readString(parser, TAG_AUTHOR);
				Log.d("ApiParser", "Author: " + author);
				book.setAuthor(author);
			} else if (TAG_PUBLICATION.equalsIgnoreCase(tagName)) {
				String publication = readString(parser, TAG_PUBLICATION);
				Log.d("ApiParser", "Publication: " + publication);
				book.setPublication(publication);
			} else if (TAG_CALL_NUMBER.equalsIgnoreCase(tagName)) {
				String callNumber = readString(parser, TAG_CALL_NUMBER);
				Log.d("ApiParser", "CallNumber: " + callNumber);
				book.setCallNumber(callNumber);
			} else if (TAG_EDITION.equalsIgnoreCase(tagName)) {
				String edition = readString(parser, TAG_EDITION);
				Log.d("ApiParser", "Edition: " + edition);
				book.setEdition(edition);
			}
		}
		parser.require(XmlPullParser.START_TAG, namespace, TAG_TAGS);
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
			if (TAG_VOLUME.equalsIgnoreCase(tagName)) {
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
				Log.d("ApiParser", "Volume: " + vol);
				volume.setVolume(vol);
			} else if (TAG_ITEM.equalsIgnoreCase(tagName)) {
				String item = readString(parser, TAG_ITEM);
				Log.d("ApiParser", "Item: " + item);
				volume.setItem(item);
			} else if (TAG_STATUS.equalsIgnoreCase(tagName)) {
				String status = readString(parser, TAG_STATUS);
				Log.d("ApiParser", "Status: " + status);
				volume.setStatus(status);
			} else if (TAG_LOCATION.equalsIgnoreCase(tagName)) {
				String location = readString(parser, TAG_LOCATION);
				Log.d("ApiParser", "Location: " + location);
				volume.setLocation(location);
			} else if (TAG_CAN_RESERVE.equalsIgnoreCase(tagName)) {
				String canReserve = readString(parser, TAG_CAN_RESERVE);
				Log.d("ApiParser", "CanReserve: " + canReserve);
				volume.setCanReserve(!TextUtils.isEmpty(canReserve) &&
					canReserve.equalsIgnoreCase("Y"));
			}
		}
		parser.require(XmlPullParser.END_TAG, namespace, TAG_ONE_ITEM);
		return volume;
	}

}
