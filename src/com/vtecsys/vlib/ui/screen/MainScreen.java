package com.vtecsys.vlib.ui.screen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.SimpleAdapter;
import com.vtecsys.vlib.model.Notices;
import com.vtecsys.vlib.ui.dialog.NoticesDialog;
import com.vtecsys.vlib.ui.fragment.AboutFragment;
import com.vtecsys.vlib.ui.fragment.AccountFragment;
import com.vtecsys.vlib.ui.fragment.SearchFragment;
import com.vtecsys.vlib.ui.fragment.SettingsFragment;
import com.vtecsys.vlib.ui.fragment.WebOpacFragment;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class MainScreen extends BaseScreen implements OnItemClickListener {
	
	public static final String APP_TITLE = "app_title";
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private int selected = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		
		Utilities.setupCheckAlertsAlarm(this); 
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setOnItemClickListener(this);
		populateDrawerList();
				
		mDrawerToggle = new ActionBarDrawerToggle(
			MainScreen.this, mDrawerLayout, R.drawable.ic_drawer, 0, 0)
		{
	        public void onDrawerClosed(View view) {
	        	invalidateOptionsMenu();
	        }
	        public void onDrawerOpened(View drawerView) {
	        	invalidateOptionsMenu();
	        }
	    };
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
	    
	    if (savedInstanceState == null) {
            selectItem(0);
        }
	    
	    Intent intent = getIntent();
	    if (intent != null) {
	    	Notices notices = (Notices) intent.getSerializableExtra("notices");
	    	showNotices(notices);
	    }
	}
	
	public void populateDrawerList() {
		String[] items = new String[] {
			locale.get(LocaleManager.SEARCH),
			locale.get(LocaleManager.MY_ACCOUNT),
			locale.get(LocaleManager.SETTINGS),
			locale.get(LocaleManager.WEB_OPAC),
			locale.get(LocaleManager.ABOUT)
		};
		SimpleAdapter adapter = new SimpleAdapter(
			this, R.layout.drawer_list_item, items);
		mDrawerList.setAdapter(adapter);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
        	return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectItem(position);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	private void selectItem(int position) {
		if (position == selected) {
	        mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}
		
		selected = position;
		
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new SearchFragment();
			break;
		case 1: 
			fragment = new AccountFragment();
			break;
		case 2: 
			fragment = new SettingsFragment();
			break;
		case 3:
			fragment = new WebOpacFragment();
			break;
		case 4:
			fragment = new AboutFragment();
			break;
		}
		
		if (fragment != null) {
	        FragmentManager fragmentManager = getFragmentManager();
	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	        
	        mDrawerList.setItemChecked(position, true);
	        mDrawerLayout.closeDrawer(mDrawerList);
		}
    }
	
	private void showNotices(Notices notices) {
		if (notices == null || !notices.hasNotices()) {
			return;
		}
		
		NoticesDialog dialog = new NoticesDialog();
		dialog.setRetainInstance(true);
		dialog.setNotices(notices);
		dialog.show(getFragmentManager(), "notices");
	}
	
	@Override
	public void onBackPressed() {
		DialogUtils.showConfirmDialog(this,
			null, 
			locale.get(LocaleManager.CONFIRM_EXIT),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			},
			new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {}
			}
		);
	}

}
