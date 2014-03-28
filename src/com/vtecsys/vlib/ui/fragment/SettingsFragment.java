package com.vtecsys.vlib.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.screen.AboutScreen;
import com.vtecsys.vlib.ui.screen.BaseScreen;
import com.vtecsys.vlib.util.Utilities;

public class SettingsFragment extends BaseFragment implements OnClickListener {
	
	private Spinner fontSize;
	private Spinner preDueDaysNotification;
	private Switch dueDateNotification;
	private Switch overdueDateNotification;
	private Switch collectionNotification;
	private RadioGroup language;
	private Button aboutBtn;
	
	private int[] languages = new int[] { R.id.lang0, R.id.lang1, R.id.lang2 };
	private boolean langChanged;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		langChanged = false;
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
				}
			}
		});
		aboutBtn = (Button) rootView.findViewById(R.id.aboutBtn);
		aboutBtn.setOnClickListener(this);
	}
	
	private void updateViews() {
		fontSize.setSelection(settings.getInt(Settings.FONT_SIZE));
		preDueDaysNotification.setSelection(
			settings.getInt(Settings.PRE_DUE_DAYS_NOTIFICATION));
		dueDateNotification.setChecked(
			settings.getBoolean(Settings.DUE_DATE_NOTIFICATION));
		overdueDateNotification.setChecked(
			settings.getBoolean(Settings.OVERDUE_DATE_NOTIFICATION));
		collectionNotification.setChecked(
			settings.getBoolean(Settings.COLLECTION_NOTIFICATION));
		language.check(languages[settings.getInt(Settings.LANGUAGE)]);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.aboutBtn) {
			Intent intent = new Intent(getActivity(), AboutScreen.class);
			getActivity().startActivity(intent);
		}
	}
	
	private void saveSettings() {
		settings.setInt(Settings.FONT_SIZE, fontSize.getSelectedItemPosition());
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
			if (Utilities.isConnectionAvailable(getActivity())) {
				requestSiteName();
			} else {
				settings.setInt(Settings.LANGUAGE, currentLang);
				Toast.makeText(getActivity(), R.string.settings_saved, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getActivity(), R.string.settings_saved, Toast.LENGTH_SHORT).show();
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
				BaseScreen.appTitle = (String) apiResponse.getData();
				getActivity().setTitle(BaseScreen.appTitle);
				settings.setInt(Settings.LANGUAGE, getLanguage());
				langChanged = false;
				Toast.makeText(getActivity(), R.string.settings_saved, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private int getLanguage() {
		int checkedId = language.getCheckedRadioButtonId();
		return checkedId == R.id.lang0 ? 0 :
			checkedId == R.id.lang1 ? 1 :
			checkedId == R.id.lang2 ? 2 : -1;
	}

}
