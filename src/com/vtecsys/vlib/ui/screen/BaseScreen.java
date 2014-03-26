package com.vtecsys.vlib.ui.screen;

import android.app.Activity;
import android.os.Bundle;

import com.vtecsys.vlib.storage.Settings;

public class BaseScreen extends Activity {
	
	protected Settings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new Settings(this);
	}

}
