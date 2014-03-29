package com.vtecsys.vlib.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	
	public static final String FILENAME = "VLibApp";
	
	public static final String FONT_SIZE = "font_size";
	public static final String PRE_DUE_DAYS_NOTIFICATION = "pre_due_days_notitication";
	public static final String DUE_DATE_NOTIFICATION = "due_date_notification";
	public static final String OVERDUE_DATE_NOTIFICATION = "overdue_date_notification";
	public static final String COLLECTION_NOTIFICATION = "collection_notification";
	public static final String LANGUAGE = "language";
	public static final String MEMBER_ID = "member_id";
	public static final String PASSWORD = "password";
	public static final String REMEMBER_PASSWORD = "remember_password";
	
	public static final int PRE_DUE_THREE_DAYS = 0;
	public static final int PRE_DUE_SEVEN_DAYS = 1;
	
	private SharedPreferences prefs;
	
	public Settings(Context context) {
		prefs = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
	}
	
	public String getString(String key, String defValue) {
		return prefs.getString(key, defValue);
	}
	
	public String getString(String key) {
		return getString(key, null);
	}
	
	public void setString(String key, String value) {
		SharedPreferences.Editor e = prefs.edit();
		e.putString(key, value);
		e.commit();
	}
	
	public int getInt(String key, int defValue) {
		return prefs.getInt(key, defValue);
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public void setInt(String key, int value) {
		SharedPreferences.Editor e = prefs.edit();
		e.putInt(key, value);
		e.commit();
	}
	
	public boolean getBoolean(String key, boolean defValue) {
		return prefs.getBoolean(key, defValue);
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public void setBoolean(String key, boolean value) {
		SharedPreferences.Editor e = prefs.edit();
		e.putBoolean(key, value);
		e.commit();
	}
	
	public float getFloat(String key, float defValue) {
		return prefs.getFloat(key, defValue);
	}
	
	public float getFloat(String key) {
		return getFloat(key, 0.0f);
	}
	
	public void setFloat(String key, float value) {
		SharedPreferences.Editor e = prefs.edit();
		e.putFloat(key, value);
		e.commit();
	}
	
	public long getLong(String key, long defValue) {
		return prefs.getLong(key, defValue);
	}
	
	public long getLong(String key) {
		return getLong(key, 0L);
	}
	
	public void setLong(String key, long value) {
		SharedPreferences.Editor e = prefs.edit();
		e.putLong(key, value);
		e.commit();
	}

}
