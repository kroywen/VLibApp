package com.vtecsys.vlib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vtecsys.vlib.receiver.CheckAlertsReceiver;
import com.vtecsys.vlib.storage.Settings;

public class Utilities {
	
	public static boolean isConnectionAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isConnected();
	}
	
	public static String streamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
	
	public static String uppercase(String word) {
		return TextUtils.isEmpty(word) ? word :
			word.length() == 1 ? String.valueOf(Character.toUpperCase(word.charAt(0))) : 
			Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}
	
	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}
	
	public static String extractDate(String str) {
		if (TextUtils.isEmpty(str)) {
			return null;
		}
		Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
		Matcher matcher = pattern.matcher(str);
		return matcher.find() ? matcher.group() : null;
	}
	
	public static String convertDate(String date, String fromPattern, String toPattern) {
		DateFormat originalFormat = new SimpleDateFormat(fromPattern, Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat(toPattern, Locale.ENGLISH);
		try {
			Date d = originalFormat.parse(date);
			return targetFormat.format(d); 
		} catch (Exception e) {
			e.printStackTrace();
			return date;
		}		
	}
	
	public static String convertDate(String date) {
		return convertDate(date, "yyyy-MM-dd", "dd/MM/yyyy");
	}
	
	public static float getFontSize(int position) {
		switch (position) {
		case 0:
			return 12.0f;
		case 1:
			return 14.0f;
		case 2: 
			return 18.0f;
		default:
			return 14.0f;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setFontSize(View view, float fontSize) { 
		try {
			Class[] paramFloat = new Class[1];	
			paramFloat[0] = Float.TYPE;
			
			Class clas = Class.forName("android.widget.TextView");
			Method method = clas.getDeclaredMethod("setTextSize", paramFloat);
			
			if (view instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) view;
				for (int i=0; i<group.getChildCount(); i++) {
					View child = group.getChildAt(i);
					setFontSize(child, fontSize);
				}
			} else {
				method.invoke(view, Float.valueOf(fontSize));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getPreDueDaysNotificationParam(int position) {
		switch (position) {
		case 0:
			return "none";
		case 1:
			return "3";
		case 2:
			return "7";
		default:
			return null;
		}
	}
	
	public static void setupCheckAlertsAlarm(Context context) {
		Settings settings = new Settings(context);
		int checkAlertsInterval = settings.getInt(Settings.CHECK_ALERTS_INTERVAL);
//		long interval = (checkAlertsInterval) == 0 ? AlarmManager.INTERVAL_HALF_DAY : AlarmManager.INTERVAL_DAY;
		long interval = (checkAlertsInterval) == 0 ? 5 * 60 * 1000 : 10 * 60 * 1000;
		
		AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, CheckAlertsReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
			interval, interval, alarmIntent);
	}

}
