package com.vtecsys.vlib.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiResponseReceiver;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.api.OnApiResponseListener;
import com.vtecsys.vlib.model.Notices;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.screen.SplashScreen;
import com.vtecsys.vlib.util.Utilities;

public class CheckAlertsReceiver extends BroadcastReceiver implements OnApiResponseListener {
	
	private Context context;
	private ApiResponseReceiver responseReceiver;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if (Utilities.isConnectionAvailable(context)) {
			Settings settings = new Settings(context);
			String memberId = settings.getString(Settings.MEMBER_ID);
			String password = settings.getString(Settings.PASSWORD);
			if (!(TextUtils.isEmpty(memberId) || TextUtils.isEmpty(password))) {
				IntentFilter intentFilter = new IntentFilter(ApiService.ACTION_API_RESULT);
				responseReceiver = new ApiResponseReceiver(this);
				LocalBroadcastManager.getInstance(context).registerReceiver(
					responseReceiver, intentFilter);
				requestAlerts();
			}
		}
	}

	private void requestAlerts() {
		Settings settings = new Settings(context);
		Intent intent = new Intent(context, ApiService.class);
		intent.setAction(ApiData.COMMAND_CHECK_ALERTS);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		String predue = Utilities.getPreDueDaysNotificationParam(
			settings.getInt(Settings.PRE_DUE_DAYS_NOTIFICATION));
		if (!TextUtils.isEmpty(predue)) {
			intent.putExtra(ApiData.PARAM_PREDUE, predue);
		}
		intent.putExtra(ApiData.PARAM_N_D, settings.getBoolean(Settings.DUE_DATE_NOTIFICATION) ? "y" : "n");
		intent.putExtra(ApiData.PARAM_N_OD, settings.getBoolean(Settings.OVERDUE_DATE_NOTIFICATION) ? "y" : "n");
		intent.putExtra(ApiData.PARAM_N_COLL, settings.getBoolean(Settings.COLLECTION_NOTIFICATION) ? "y" : "n");
		context.startService(intent);
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(responseReceiver);
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				if (ApiData.COMMAND_CHECK_ALERTS.equalsIgnoreCase(apiResponse.getRequestName())) {
					Notices notices = (Notices) apiResponse.getData();
					if (notices != null && notices.hasNotices()) {
						Settings settings = new Settings(context);
						String contentTitle = TextUtils.isEmpty(settings.getString(Settings.APP_TITLE)) ?
							context.getString(R.string.app_name) :
							settings.getString(Settings.APP_TITLE);
						String contentText = Notices.getNoticesText(notices);
						
						NotificationCompat.Builder mBuilder =
					        new NotificationCompat.Builder(context)
					        .setSmallIcon(R.drawable.ic_launcher)
					        .setAutoCancel(true)
					        .setContentTitle(contentTitle)
					        .setVibrate(new long[] {0, 100, 200, 300})
					        .setContentText(contentText)
					        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));
	
						Intent resultIntent = new Intent(context, SplashScreen.class);
	
						TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
						stackBuilder.addParentStack(SplashScreen.class);
						stackBuilder.addNextIntent(resultIntent);
						
						PendingIntent resultPendingIntent =
					        stackBuilder.getPendingIntent(
					            0,
					            PendingIntent.FLAG_UPDATE_CURRENT
					        );
						mBuilder.setContentIntent(resultPendingIntent);
						
						NotificationManager notificationManager =
						    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						notificationManager.notify(0, mBuilder.build());
					}
				}
			}
		}
	}

}
