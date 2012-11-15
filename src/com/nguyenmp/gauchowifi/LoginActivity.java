package com.nguyenmp.gauchowifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class LoginActivity extends SherlockFragmentActivity {
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	
	public void onCreate(Bundle inState) {
		super.onCreate(inState);
		
		setContentView(R.layout.view_pager);
		
		mViewPager = (ViewPager) super.findViewById(R.id.view_pager);
		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		
		//Add the site news to the list
		mTabsAdapter.addTab(actionBar.newTab().setText("Login"), LoginFragment.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Log"), LogFragment.class, null);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add("Settings").setIcon(R.drawable.ic_menu_settings_holo_light).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else if (item.getTitle().equals("Settings")) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
