package com.vtecsys.vlib.ui.screen;

import android.app.Activity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vtecsys.vlib.storage.Settings;

public class BaseScreen extends Activity {
	
	protected static String appTitle;
	protected Settings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new Settings(this);
		setTitle(appTitle);
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.build();
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
			.defaultDisplayImageOptions(options)
			.build();
		ImageLoader.getInstance().init(configuration);
	}

}
