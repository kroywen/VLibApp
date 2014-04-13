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
