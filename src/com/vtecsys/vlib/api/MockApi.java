package com.vtecsys.vlib.api;

import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.vtecsys.vlib.parser.ApiParser;
import com.vtecsys.vlib.parser.ParserFactory;

public class MockApi {
	
	public static void requestSitename(Context context) {
		request(context, "mock_response/sitename", ApiData.COMMAND_SITENAME);
	}
	
	public static void requestSearch(Context context) {
		request(context, "mock_response/search", ApiData.COMMAND_SEARCH);
	}
	
	public static void requestCatalogue(Context context) {
		request(context, "mock_response/catalogue", ApiData.COMMAND_CATALOGUE);
	}
	
	private static void request(Context context, String filename, String command) {
		try {
			AssetManager am = context.getAssets();
			InputStream is = am.open(filename);
			ApiParser parser = ParserFactory.getParser(command);
			if (parser != null) {
				parser.parse(is);
				ApiResponse apiResponse = parser.getApiResponse();
				sendResult(context, ApiService.API_STATUS_SUCCESS, apiResponse);
			}
			is.close();
		} catch (Exception e) {
			Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			sendResult(context, ApiService.API_STATUS_ERROR, null);
		}
	}
	
	private static void sendResult(Context context, int apiStatus, ApiResponse apiResponse) {
		Intent resultIntent = new Intent(ApiService.ACTION_API_RESULT);
		resultIntent.putExtra(ApiService.EXTRA_API_STATUS, apiStatus);
		resultIntent.putExtra(ApiService.EXTRA_API_RESPONSE, apiResponse);
		LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent);
	}

}
