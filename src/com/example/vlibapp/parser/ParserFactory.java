package com.example.vlibapp.parser;

import com.example.vlibapp.api.ApiData;

import android.text.TextUtils;

public class ParserFactory {
	
	public static ApiParser getParser(String command) {
		if (TextUtils.isEmpty(command)) {
			return null;
		} else if (ApiData.COMMAND_SITENAME.equalsIgnoreCase(command)) {
			return new SiteNameParser();
		} else {
			return null;
		}
	}

}
