package com.example.vlibapp.api;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.IntentService;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.support.v4.content.LocalBroadcastManager;

import com.example.vlibapp.parser.ApiParser;
import com.example.vlibapp.parser.ParserFactory;

public class ApiService extends IntentService {
	
	public static final String ACTION_API_RESULT = "action_api_result";
	public static final String EXTRA_API_STATUS = "extra_api_status";
	public static final String EXTRA_API_RESPONSE = "extra_api_response";
	
	public static final int API_STATUS_NONE = -1;
	public static final int API_STATUS_SUCCESS = 0;
	public static final int API_STATUS_ERROR = 1;
	
	public ApiService() {
		this(ApiService.class.getSimpleName());
	}

	public ApiService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String command = intent.getAction();
		String url = ApiData.createURL(command, intent.getExtras());
		HttpClient client = AndroidHttpClient.newInstance(
			System.getProperty("http.agent"), this);
		HttpGet request = new HttpGet(url);
		
		HttpResponse response = null;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
//				String responseXml = Utilities.streamToString(entity.getContent());
//				Log.d("ApiService", responseXml);
				InputStream is = entity.getContent();
				ApiParser parser = ParserFactory.getParser(command);
				parser.parse(is);
				
				ApiResponse apiResponse = parser.getApiResponse();
				sendResult(API_STATUS_SUCCESS, apiResponse);
				
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendResult(API_STATUS_ERROR, null);
		}
	}
	
	private void sendResult(int apiStatus, ApiResponse apiResponse) {
		Intent resultIntent = new Intent(ACTION_API_RESULT);
		resultIntent.putExtra(EXTRA_API_STATUS, apiStatus);
		resultIntent.putExtra(EXTRA_API_RESPONSE, apiResponse);
		LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
	}

}
