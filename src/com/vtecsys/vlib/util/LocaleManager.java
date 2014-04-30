package com.vtecsys.vlib.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;


public class LocaleManager {
	
	public static final int PLEASE_ENTER_THE_KEYWORDS = 2001;
	public static final int TITLE = 2002;
	public static final int AUTHOR = 2003;
	public static final int SUBJECT = 2004;
	public static final int SERIES = 2005;
	public static final int ALL = 2006;
	public static final int ISBN = 2007;
	public static final int NONE = 2008;
	public static final int CALL_NO = 2009;
	public static final int PUBLICATION_DATE = 2010;
	public static final int SORT_BY = 2011;
	public static final int BROWSE_BY_AUTHOR = 2012;
	public static final int BROWSE_BY_SUBJECT = 2013;
	public static final int BROWSE_BY_SERIES = 2014;
	public static final int SEARCH = 2015;
	public static final int MY_ACCOUNT = 2016;
	public static final int SETTINGS = 2017;
	public static final int WEB_OPAC = 2018;
	public static final int PUBLISHER = 2019;
	public static final int RECORDS_FOUND = 2020;
	public static final int RECORDS_LOADED = 2021;
//	public static final int ISBN = 2022; // 2007
	public static final int EDITION = 2023;
	public static final int PUBLICATION = 2024;
	public static final int LOGIN = 2025;
	public static final int RESERVE = 2026;
	public static final int ITEM_NO = 2027;
	public static final int STATUS = 2028;
	public static final int LOCATION = 2029;
	public static final int CALL_NO_ISSUE_VOL = 2030;
	public static final int MEMBER_ID = 2031;
	public static final int PASSWORD = 2032;
	public static final int RESET = 2033;
	public static final int REMEMBER_PASSWORD = 2034;
//	public static final int RESERVE = 2035; // 2026
	public static final int TITLE_CONFIRM_RESERVE = 2036;
	public static final int TITLE_RESERVED = 2037;
//	public static final int MEMBER_ID = 2038; // 2031
	public static final int MEMBER_NAME = 2039;
	public static final int TITLE_CONFIRM_RENEW = 2040;
	public static final int TITLE_RENEWED = 2041;
//	public static final int MY_ACCOUNT = 2042; // 2016
	public static final int LOAN_ACTIVITIES = 2043;
	public static final int RESERVATION_LIST = 2044;
	public static final int CHANGE_PASSWORD = 2045;
	public static final int TITLE_CONFIRM_CANCEL_RESERVATION = 2046;
	public static final int YES = 2047;
	public static final int NO = 2048;
	public static final int BACK = 2050;
	public static final int ENTER_NEW_PASSWORD = 2052;
	public static final int RE_ENTER_NEW_PASSWORD = 2053;
	public static final int LANG_0 = 2054;
	public static final int LANG_1 = 2055;
	public static final int LANG_2 = 2056;
	public static final int FONT_SIZE = 2057;
	public static final int SMALL = 2058;
	public static final int MEDIUM = 2059;
	public static final int LARGE = 2060;
	public static final int PRE_DUE_DAYS_NOTIFICATION = 2061;
	public static final int THREE_DAYS = 2063;
	public static final int SEVEN_DAYS = 2064;
	public static final int DUE_DATE_NOTIFICATION = 2065;
	public static final int OVERDUE_DATE_NOTIFICATION = 2066;
	public static final int COLLECTION_NOTIFICATION = 2067;
	public static final int ON = 2068;
	public static final int OFF = 2069;
	public static final int SAVE = 2070;
	public static final int SETTINGS_SAVED = 2071;
	public static final int SEARCH_BY = 2072;
	public static final int ABOUT = 2073;
	public static final int RESERVE_DATE = 2074;
	public static final int VOL_NO = 2075;
	public static final int READY_DATE = 2076;
	public static final int CANCEL = 2077;
	public static final int RENEW = 2078;
	public static final int DUE_DATE = 2079;
	public static final int CATALOGUE_DETAILS = 2080;
	public static final int OK = 2081;
	public static final int ERROR = 2082;
	public static final int EMPTY = 2083;
	public static final int LANGUAGE = 2084;
	public static final int NO_CONNECTION_ERROR = 2085;
	public static final int PASSWORDS_NOT_MATCH = 2086;
	public static final int LOGGED_OUT_SUCCESS = 2087;
	public static final int LOGOUT = 2088;
	public static final int TOUCH_PICTURE_MORE_DETAILS = 2089;
	public static final int TOUCH_PICTURE_FULL_DETAILS = 2090;
	public static final int CHECK_ALERTS_INTERVAL = 2091;
	public static final int INTERVAL_12 = 2092;
	public static final int INTERVAL_24 = 2093;
	public static final int VERSION = 2094;
	
	private int language = -1;
	private SparseArray<String> translations;
	private static LocaleManager instance;
	
	private LocaleManager() {
		translations = new SparseArray<String>();
	}
	
	public static LocaleManager getInstance() {
		if (instance == null) {
			instance = new LocaleManager();
		}
		return instance;
	}
	
	public void setLanguage(Context context, int language) {
		if (this.language == language) {
			return;
		}
		this.language = language;
		try {
			updateTranslations(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateTranslations(Context context) 
		throws IOException 
	{
		if (translations == null) {
			translations = new SparseArray<String>();
		}
		AssetManager assetManager = context.getAssets();
		String filename = "lang" + language + ".txt";
		InputStream is = assetManager.open(filename);
		String str = Utilities.streamToString(is);
		String[] lines = str.split("\n");
		for (String line : lines) {
			if (TextUtils.isEmpty(line) || line.startsWith("#")) {
				continue;
			}
			String[] pair = line.split(";");
			if (pair == null || pair.length != 2) {
				continue;
			}
			try {
				int key = Integer.valueOf(Integer.parseInt(pair[0]));
				translations.put(key, pair[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String get(int key) {
		if (translations == null || translations.size() == 0) {
			return null;
		}
		return translations.get(key);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void apply(View view) {
		try {
			Class[] params = new Class[1];	
			params[0] = CharSequence.class;
			
			Class clas = Class.forName("android.widget.TextView");
			
			if (view instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) view;
				for (int i=0; i<group.getChildCount(); i++) {
					View child = group.getChildAt(i);
					apply(child);
				}
			} else {
				Method setMethod = clas.getDeclaredMethod("setText", CharSequence.class);
				Object tagObj = view.getTag();
				if (tagObj != null) {
					int code = Integer.parseInt(tagObj.toString());
					if (code != 0) {
						setMethod.invoke(view, get(code));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
