package com.vtecsys.vlib.ui.screen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.ui.fragment.AccountFragment;
import com.vtecsys.vlib.ui.fragment.SearchFragment;
import com.vtecsys.vlib.ui.fragment.SettingsFragment;
import com.vtecsys.vlib.ui.fragment.WebOpacFragment;

public class MainScreen extends BaseScreen implements OnItemClickListener {
	
	public static final String APP_TITLE = "app_title";
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private String[] mMainMenu;
	private int selected = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		
		mMainMenu = getResources().getStringArray(R.array.main_menu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMainMenu));
		mDrawerList.setOnItemClickListener(this);
				
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
		}
		
		if (fragment != null) {
	        FragmentManager fragmentManager = getFragmentManager();
	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	        
	        mDrawerList.setItemChecked(position, true);
	        mDrawerLayout.closeDrawer(mDrawerList);
		}
		
		selected = position;
    }

}