package com.nguyenmp.gauchowifi;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

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
}
