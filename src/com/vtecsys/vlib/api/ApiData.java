package com.vtecsys.vlib.api;

import java.util.Iterator;
import java.util.Set;

import android.net.Uri;
import android.os.Bundle;

public class ApiData {
	
	public static final String BASE_URL = "http://123.100.239.16:6061/";
	
	public static final String COMMAND_SITENAME = "SiteName";
	public static final String COMMAND_ABOUT = "About";
	public static final String COMMAND_BROWSE = "Browse";
	public static final String COMMAND_SEARCH = "Search";
	public static final String COMMAND_CATALOGUE = "Catalogue";
	public static final String COMMAND_PATR_ACCOUNT = "PatrAccount";
	public static final String COMMAND_CHANGE_PASSWORD = "ChangePassword";
	public static final String COMMAND_RENEW_LOAN = "RenewLoan";
	public static final String COMMAND_CANCEL_RESERVATION = "CancelReservation";
	
	public static final String PARAM_LANG = "lang";
	public static final String PARAM_TERM = "term";
	public static final String PARAM_BROWSE_BY = "browseBy";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_SORT_BY = "sortBy";
	public static final String PARAM_SEARCH_BY = "searchBy";
	public static final String PARAM_RID = "RID";
	public static final String PARAM_ID = "id";
	public static final String PARAM_PASSWD = "passwd";
	public static final String PARAM_NEW_PWD = "newpwd";
	public static final String PARAM_ITEM_NO = "item_no";
	public static final String PARAM_ISSUE = "issue";
	public static final String PARAM_VOLUME = "volume";
	
	public static final String TYPE_SEARCH = "search";
	public static final String TYPE_BROWSE = "browse";
	
	public static String createURL(String command, Bundle params) {
		Uri.Builder uriBuilder = Uri.parse(BASE_URL + command).buildUpon();
		if (params != null) {
			Set<String> keys = params.keySet();
			if (keys != null && !keys.isEmpty()) {
				Iterator<String> i = keys.iterator();
				while (i.hasNext()) {
					String key = i.next();
					String value = String.valueOf(params.get(key));
					uriBuilder.appendQueryParameter(key, value);
				}
			}
		}
		return uriBuilder.build().toString();
	}

}
