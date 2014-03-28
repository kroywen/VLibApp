package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.vtecsys.vlib.api.ApiResponse;

public abstract class ApiParser {
	
	public static final String TAG_RESPONSE = "Response";
	public static final String TAG_STATUS = "Status";
	public static final String TAG_MESSAGE = "Message";
	public static final String TAG_RESPONSE_DATA = "ResponseData";
	public static final String TAG_SITE_NAME = "SiteName";
	public static final String TAG_RESULTS = "Results";
	public static final String TAG_HITS = "Hits";
	public static final String TAG_LOADED = "Loaded";
	public static final String TAG_ONE_RID = "OneRID";
	public static final String TAG_RID = "RID";
	public static final String TAG_TITLE = "Title";
	public static final String TAG_AUTHOR = "Author";
	public static final String TAG_PUBLICATION = "Publication";
	public static final String TAG_CALL_NO = "CallNo";
	public static final String TAG_BOOK_COVER = "BookCover";
	public static final String TAG_TAGS = "Tags";
	public static final String TAG_ISBN = "ISBN";
	public static final String TAG_CALL_NUMBER = "CallNumber";
	public static final String TAG_EDITION = "Edition";
	public static final String TAG_VOLUMES = "Volumes";
	public static final String TAG_ONE_ITEM = "OneItem";
	public static final String TAG_VOLUME = "Volume";
	public static final String TAG_ITEM = "Item";
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_CAN_RESERVE = "CanReserve";
	public static final String TAG_ONE_AUTH = "OneAuth";
	public static final String TAG_AUTH_NO = "AuthNo";
	public static final String TAG_AUTH_ENTRY = "AuthEntry";
	public static final String TAG_ABOUT = "About";
	
	protected static final String namespace = null;
	
	private ApiResponse apiResponse;
	
	public ApiParser() {
		apiResponse = new ApiResponse();
	}
	
	public ApiResponse getApiResponse() {
		return apiResponse;
	}
	
	public void setApiResponse(ApiResponse apiResponse) {
		this.apiResponse = apiResponse;
	}
	
	public void parse(InputStream is) 
		throws XmlPullParserException, IOException 
	{
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(is, "utf-8");
			parser.next();
			
			parser.require(XmlPullParser.START_TAG, namespace, TAG_RESPONSE);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String tagName = parser.getName();
					if (tagName.equalsIgnoreCase(TAG_STATUS)) {
						int status = readInt(parser, TAG_STATUS);
						apiResponse.setStatus(status);
					} else if (tagName.equalsIgnoreCase(TAG_MESSAGE)) {
						String message = readString(parser, TAG_MESSAGE);
						apiResponse.setMessage(message);
					} else if (tagName.equalsIgnoreCase(TAG_RESPONSE_DATA)) {
						Object data = readData(parser);
						apiResponse.setData(data);
					}
				}
				eventType = parser.next();
			}
			parser.require(XmlPullParser.END_TAG, namespace, TAG_RESPONSE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
	}
	
	protected String readString(XmlPullParser parser, String tagName)
		throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, namespace, tagName);
		String str = readText(parser);
		parser.require(XmlPullParser.END_TAG, namespace, tagName);
		return str;
	}
	
	protected int readInt(XmlPullParser parser, String tagName)
		throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, namespace, tagName);
		int number = Integer.parseInt(readText(parser));
		parser.require(XmlPullParser.END_TAG, namespace, tagName);
		return number;
	}
	
	protected String readText(XmlPullParser parser) 
		throws XmlPullParserException, IOException
	{
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	public abstract Object readData(XmlPullParser parser)
		throws XmlPullParserException, IOException;

}
