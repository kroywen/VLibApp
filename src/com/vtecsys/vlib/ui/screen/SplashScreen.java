package com.vtecsys.vlib.ui.screen;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Notices;
import com.vtecsys.vlib.model.Site;
import com.vtecsys.vlib.model.result.SiteListResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.Utilities;

public class SplashScreen extends BaseScreen {
	
	public static final int SPLASH_TIME = 2000;
	
	private ImageView splashView;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.splash_screen, null);
		setContentView(root);
		initializeViews(root);
		
		setSplash();
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
		
		if (Utilities.isConnectionAvailable(this)) {
			boolean siteSelected = settings.getBoolean(Settings.SITE_SELECTED, false);
			if (!siteSelected) {
				requestSiteList();
			} else {
				String memberID = settings.getString(Settings.MEMBER_ID);
				if (TextUtils.isEmpty(memberID)) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									startMainScreen(null);
								}
							});
						}
					}, 2000);
				} else {
					requestAlerts();
				}
			}
		} else {
			showConnectionErrorDialog();
		}
	}
	
	private void setSplash() {
		try {
			InputStream is = openFileInput("splash.png");
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			splashView.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
			splashView.setImageResource(R.drawable.splash);
		}
	}
	
	@Override
	protected void initializeViews(View root) {
		super.initializeViews(root);
		splashView = (ImageView) root.findViewById(R.id.splashView);
	}
	
	private void requestSiteList() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_SITELIST);
		startService(intent);
	}
	
	private void requestSiteSelect(String code) {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_SITESELECT);
		intent.putExtra(ApiData.PARAM_CODE, code);
		startService(intent);
	}
	
	private void requestAlerts() {
		Intent intent = new Intent(this, ApiService.class);
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
		startService(intent);
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				if (ApiData.COMMAND_CHECK_ALERTS.equalsIgnoreCase(apiResponse.getRequestName())) {
					Notices notices = (Notices) apiResponse.getData();
					startMainScreen(notices);
				} else if (ApiData.COMMAND_SITELIST.equalsIgnoreCase(apiResponse.getRequestName())) {
					final SiteListResult result = (SiteListResult) apiResponse.getData();
					
					String[] items = new String[result.getCount()];
					for (int i=0; i<result.getCount(); i++) {
						Site site = result.getSites().get(i);
						items[i] = site.getName();
					}
					
					DialogUtils.showSelectionDialog(this, getString(R.string.select_resource_centre), items,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String code = result.getSites().get(which).getCode();
								requestSiteSelect(code);
							}
						}, new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								finish();
							}
						});
				} else if (ApiData.COMMAND_SITESELECT.equalsIgnoreCase(apiResponse.getRequestName())) {
					final Site site = (Site) apiResponse.getData();
					storeSiteSettings(site);
					setTitle(site.getName());
					
					if (site.getLanguages() > 0) {
						new LoadLanguagesTask(site).execute((Void[]) null);
					} else {
						checkAlerts();
					}
				}
			}
		}
	}
	
	private void checkAlerts() {
		String memberID = settings.getString(Settings.MEMBER_ID);
		if (TextUtils.isEmpty(memberID)) {
			startMainScreen(null);
		} else {
			requestAlerts();
		}
	}
	
	private void storeSiteSettings(Site site) {
		if (site == null) {
			return;
		}
		settings.setBoolean(Settings.SITE_SELECTED, true);
		settings.setString(Settings.SITE_CODE, site.getCode());
		settings.setString(Settings.SITE_NAME, site.getName());
		settings.setString(Settings.SITE_URL, site.getUrl());
		settings.setString(Settings.SITE_ICON, site.getIcon());
		settings.setString(Settings.SITE_SPLASH, site.getSplash());
		settings.setInt(Settings.SITE_LANGUAGES, site.getLanguages());
		settings.setString(Settings.SITE_LANG0, site.getLang0());
		settings.setString(Settings.SITE_LANG1, site.getLang1());
		settings.setString(Settings.SITE_LANG2, site.getLang2());
		settings.setBoolean(Settings.SITE_MSC, site.getMsc());
	}
	
	private void startMainScreen(Notices notices) {
		Intent intent = new Intent(this, MainScreen.class);
		intent.putExtra("notices", notices);
		startActivity(intent);
		finish();
	}
	
	class LoadLanguagesTask extends AsyncTask<Void, Void, Void> {
		
		private Site site;
		private ProgressDialog dialog;
		
		public LoadLanguagesTask(Site site) {
			this.site = site;
		}
		
		@SuppressLint("InflateParams")
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(SplashScreen.this, null, "Loading resources", true, false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			downloadLanguages();
			downloadSplash();
			return null;
		}
		
		private void downloadLanguages() {			
			for (int i=0; i<site.getLanguages(); i++) {
				String langUrl = settings.getString("site_lang" + i);
				if (!TextUtils.isEmpty(langUrl)) {
					try {
						URL url = new URL(langUrl);
						HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
						urlConnection.setRequestMethod("GET");
						urlConnection.setDoOutput(true);
						urlConnection.connect();
						
						FileOutputStream fos = openFileOutput("lang" + i + ".txt", MODE_PRIVATE);
						InputStream is = urlConnection.getInputStream();
						
						byte[] data = new byte[1024];
						int count;
						while ((count = is.read(data)) != -1) { 
							fos.write(data, 0, count);
						}
						
						fos.flush();
						fos.close();
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private void downloadSplash() {			
			String splashUrl = site.getSplash();
			if (!TextUtils.isEmpty(splashUrl)) {
				try {
					URL url = new URL(splashUrl);
					URLConnection urlConnection = url.openConnection();
					 
					FileOutputStream fos = openFileOutput("splash.png", MODE_PRIVATE);
					InputStream is = urlConnection.getInputStream();
					
					byte[] data = new byte[1024];
			        int count = 0;
			        while ((count = is.read(data, 0, data.length)) >= 0) {
			            fos.write(data, 0, count);
			        }
					
					fos.flush();
					fos.close();
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			
			setSplash();
			checkAlerts();
		}
		
	}
	
}
