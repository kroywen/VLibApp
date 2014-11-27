package com.vtecsys.vlib.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.SimpleAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.result.SiteNameResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.screen.MainScreen;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class SettingsFragment extends BaseFragment implements OnClickListener {
	
	private Spinner fontSize;
	private View maxBookmarks;
	private TextView maxBookmarksValue;
	private Spinner checkAlertsInterval;
	private Spinner preDueDaysNotification;
	private Switch dueDateNotification;
	private Switch overdueDateNotification;
	private Switch collectionNotification;
	private RadioGroup language;
	
	private int[] languages = new int[] { R.id.lang0, R.id.lang1, R.id.lang2 };
	private boolean langChanged;
	private boolean fontSizeChanged;
	private boolean intervalChanged;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		langChanged = false;
		fontSizeChanged = false;
		intervalChanged = false;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.settings, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_save) {
			saveSettings();
		}
		return true;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings_fragment, null);
		initializeViews(rootView);
		updateViews();
		return rootView;
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		
		fontSize = (Spinner) rootView.findViewById(R.id.fontSize);
		fontSize.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int prevFontSize = settings.getInt(Settings.FONT_SIZE);
				int currentFontSize = position;
				if (currentFontSize != prevFontSize) {
					fontSizeChanged = true;
				} else {
					fontSizeChanged = false;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		maxBookmarks = rootView.findViewById(R.id.maxBookmarks);
		maxBookmarks.setOnClickListener(this);
		
		maxBookmarksValue = (TextView) rootView.findViewById(R.id.maxBookmarksValue);
		
		checkAlertsInterval = (Spinner) rootView.findViewById(R.id.checkAlertsInterval);
		checkAlertsInterval.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int prevInterval = settings.getInt(Settings.CHECK_ALERTS_INTERVAL);
				int currentInterval = position;
				if (currentInterval != prevInterval) {
					intervalChanged = true;
				} else {
					intervalChanged = false;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		preDueDaysNotification = (Spinner)
			rootView.findViewById(R.id.preDueDaysNotification);
		
		dueDateNotification = (Switch) 
			rootView.findViewById(R.id.dueDateNotification);
		
		overdueDateNotification = (Switch)
			rootView.findViewById(R.id.overdueDateNotification);
		
		collectionNotification = (Switch)
			rootView.findViewById(R.id.collectionNotification);
		
		language = (RadioGroup) rootView.findViewById(R.id.language);
		language.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int prevLang = settings.getInt(Settings.LANGUAGE);
				int currentLang = getLanguage();
				if (currentLang != prevLang) {
					langChanged = true;
				} else {
					langChanged = false;
				}
			}
		});
	}
	
	private void populateFontSizeSpinner() {
		String[] items = null;
		if (Utilities.isTabletDevice(getActivity())) {
			items = new String[] {
				locale.get(LocaleManager.SMALL),
				locale.get(LocaleManager.MEDIUM),
				locale.get(LocaleManager.LARGE),
				locale.get(LocaleManager.EXTRA_LARGE)
			};
		} else {
			items = new String[] {
				locale.get(LocaleManager.SMALL),
				locale.get(LocaleManager.MEDIUM),
				locale.get(LocaleManager.LARGE),	
			};
		}
		SimpleAdapter adapter = new SimpleAdapter(
			getActivity(), R.layout.simple_spinner_item, items);
		fontSize.setAdapter(adapter);
	}
	
	private void populateCheckAlertsIntervalSpinner() {
		String[] items = new String[] {
			locale.get(LocaleManager.INTERVAL_12),
			locale.get(LocaleManager.INTERVAL_24)
		};
		SimpleAdapter adapter = new SimpleAdapter(
			getActivity(), R.layout.simple_spinner_item, items);
		checkAlertsInterval.setAdapter(adapter);
	}
	
	private void populateNotificationSpinner() {
		String[] items = new String[] {
			locale.get(LocaleManager.NONE),
			locale.get(LocaleManager.THREE_DAYS),
			locale.get(LocaleManager.SEVEN_DAYS)
		};
		SimpleAdapter adapter = new SimpleAdapter(
			getActivity(), R.layout.simple_spinner_item, items);
		preDueDaysNotification.setAdapter(adapter);
	}
	
	private void updateViews() {
		populateFontSizeSpinner();
		fontSize.setSelection(settings.getInt(Settings.FONT_SIZE));
		
		maxBookmarksValue.setText(String.valueOf(settings.getInt(Settings.MAX_BOOKMARKS, 10)));
		
		populateCheckAlertsIntervalSpinner();
		checkAlertsInterval.setSelection(settings.getInt(Settings.CHECK_ALERTS_INTERVAL));
		populateNotificationSpinner();
		preDueDaysNotification.setSelection(
			settings.getInt(Settings.PRE_DUE_DAYS_NOTIFICATION));
		dueDateNotification.setTextOn(locale.get(LocaleManager.ON));
		dueDateNotification.setTextOff(locale.get(LocaleManager.OFF));
		dueDateNotification.setChecked(
			settings.getBoolean(Settings.DUE_DATE_NOTIFICATION));
		overdueDateNotification.setTextOn(locale.get(LocaleManager.ON));
		overdueDateNotification.setTextOff(locale.get(LocaleManager.OFF));
		overdueDateNotification.setChecked(
			settings.getBoolean(Settings.OVERDUE_DATE_NOTIFICATION));
		collectionNotification.setTextOn(locale.get(LocaleManager.ON));
		collectionNotification.setTextOff(locale.get(LocaleManager.OFF));
		collectionNotification.setChecked(
			settings.getBoolean(Settings.COLLECTION_NOTIFICATION));
		language.check(languages[settings.getInt(Settings.LANGUAGE)]);
	}
	
	private void saveSettings() {
		settings.setInt(Settings.FONT_SIZE, fontSize.getSelectedItemPosition());
		settings.setInt(Settings.CHECK_ALERTS_INTERVAL, 
			checkAlertsInterval.getSelectedItemPosition());
		settings.setInt(Settings.PRE_DUE_DAYS_NOTIFICATION, 
			preDueDaysNotification.getSelectedItemPosition());
		settings.setBoolean(Settings.DUE_DATE_NOTIFICATION, 
			dueDateNotification.isChecked());
		settings.setBoolean(Settings.OVERDUE_DATE_NOTIFICATION, 
			overdueDateNotification.isChecked());
		settings.setBoolean(Settings.COLLECTION_NOTIFICATION, 
			collectionNotification.isChecked());
		
		if (langChanged) {
			int currentLang = getLanguage();
			locale.setLanguage(getActivity(), currentLang);
			locale.apply(getView());
			((MainScreen) getActivity()).populateDrawerList();
			
			if (Utilities.isConnectionAvailable(getActivity())) {
				requestSiteName();
			} else {
				settings.setInt(Settings.LANGUAGE, currentLang);
				updateViews();
			}
		} 
		if (fontSizeChanged) {
			((MainScreen) getActivity()).populateDrawerList();
			Utilities.setFontSize(getView(), Utilities.getFontSize(
				settings.getInt(Settings.FONT_SIZE)));
		}
		if (intervalChanged) {
			Utilities.setupCheckAlertsAlarm(getActivity());
		}
		if (!langChanged) {
			Toast.makeText(getActivity(), locale.get(LocaleManager.SETTINGS_SAVED),
				Toast.LENGTH_SHORT).show();
		}
	}
	
	private void requestSiteName() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiData.COMMAND_SITENAME);
		intent.putExtra(ApiData.PARAM_LANG, getLanguage());
		getActivity().startService(intent);
		showProgress(false);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				if (ApiData.COMMAND_SITENAME.equals(apiResponse.getRequestName())) {
					SiteNameResult result = (SiteNameResult) apiResponse.getData();
					settings.setString(Settings.APP_TITLE, result.getSiteName());
					settings.setString(Settings.WEB_OPAC_URL, result.getUrl());
					getActivity().setTitle(result.getSiteName());
					settings.setInt(Settings.LANGUAGE, getLanguage());
					updateViews();
					langChanged = false;
					Toast.makeText(getActivity(), locale.get(LocaleManager.SETTINGS_SAVED),
						Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private int getLanguage() {
		int checkedId = language.getCheckedRadioButtonId();
		return checkedId == R.id.lang0 ? 0 :
			checkedId == R.id.lang1 ? 1 :
			checkedId == R.id.lang2 ? 2 : -1;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.maxBookmarks:
			showSelectMaxBookmarksDialog();
			break;
		}
	}
	
	private void showSelectMaxBookmarksDialog() {
		int checkedItem = settings.getInt(Settings.MAX_BOOKMARKS, 10) - 10;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final String[] items = Utilities.generateRangeItems(10, 100);
		builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				settings.setInt(Settings.MAX_BOOKMARKS, Integer.parseInt(items[which]));
				updateViews();
			}
		})
		.create()
		.show();
	}

}
