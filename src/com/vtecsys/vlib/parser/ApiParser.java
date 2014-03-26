package com.vtecsys.vlib.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.vtecsys.vlib.api.ApiResponse;

public abstract class ApiParser {
	
	public static final String TAG_RESPONSE = "Response";
	public static final String TAG_STATUS = "Status";
	public static final String TAG_MESSAGE = "Message";
	public static final String TAG_RESPONSE_DATA = "ResponseData";
	
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
						int status = readStatus(parser);
						apiResponse.setStatus(status);
					} else if (tagName.equalsIgnoreCase(TAG_MESSAGE)) {
						String message = readMessage(parser);
						Log.d("ApiResponse", "Message: " + message);
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
	
	private int readStatus(XmlPullParser parser) 
		throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, namespace, TAG_STATUS);
	    int status = Integer.parseInt(readText(parser));
	    parser.require(XmlPullParser.END_TAG, namespace, TAG_STATUS);
	    return status;
	}
	
	private String readMessage(XmlPullParser parser) 
		throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, namespace, TAG_MESSAGE);
	    String message = readText(parser);
	    parser.require(XmlPullParser.END_TAG, namespace, TAG_MESSAGE);
	    return message;
	}
	
	protected String readText(XmlPullParser parser) 
		throws IOException, XmlPullParserException 
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
