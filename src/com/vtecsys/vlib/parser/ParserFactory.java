package com.vtecsys.vlib.parser;

import com.vtecsys.vlib.api.ApiData;

import android.text.TextUtils;

public class ParserFactory {
	
	public static ApiParser getParser(String command) {
		if (TextUtils.isEmpty(command)) {
			return null;
		} else if (ApiData.COMMAND_SITENAME.equalsIgnoreCase(command)) {
			return new SiteNameParser();
		} else if (ApiData.COMMAND_SEARCH.equalsIgnoreCase(command)) {
			return new SearchParser();
		} else {
			return null;
		}
	}

}
