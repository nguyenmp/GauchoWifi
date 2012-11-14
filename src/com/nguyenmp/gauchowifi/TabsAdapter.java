package com.nguyenmp.gauchowifi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabsAdapter extends FragmentStatePagerAdapter
			implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
	private final Activity mActivity;
	private final ActionBar mActionBar;
	private final ViewPager mViewPager;
	private final List<TabInfo> mTabs = new ArrayList<TabInfo>();
	
	static final class TabInfo {
		private final Class<?> clss;
		private final Bundle args;
		
		TabInfo(Class<?> _class, Bundle _args) {
			clss = _class;
			args = _args;
		}
	}
	
	public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
		super(activity.getSupportFragmentManager());
		mActivity = activity;
		mActionBar = activity.getSupportActionBar();
		mViewPager = pager;
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
	}
	
	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
		TabInfo info = new TabInfo(clss, args);
		tab.setTag(info);
		tab.setTabListener(this);
		mTabs.add(info);
		mActionBar.addTab(tab);
		notifyDataSetChanged();
	}
	
	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mActivity, info.clss.getName(), info.args);
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	public void onPageScrollStateChanged(int arg0) {
		//Do nothing
	}

	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		//Do nothing
	}

	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
		selectInSpinnerIfPresent(position, true);
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
		Object tag = tab.getTag();
		for (int i = 0; i < mTabs.size(); i++) {
			if (mTabs.get(i) == tag) {
				mViewPager.setCurrentItem(i);
			}
		}
		
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		//Do nothing
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		//Do nothing
	}
	
	
	private void selectInSpinnerIfPresent(int position, boolean animate) {
		try {
			View actionBarView = mActivity.findViewById(com.actionbarsherlock.R.id.abs__action_bar);
			if (actionBarView == null) {
				int id = mActivity.getResources().getIdentifier("action_bar", "id", "android");
				actionBarView = mActivity.findViewById(id);
			}
	
			Class<?> actionBarViewClass = actionBarView.getClass();
			Field mTabScrollViewField = actionBarViewClass.getDeclaredField("mTabScrollView");
			mTabScrollViewField.setAccessible(true);
	
			Object mTabScrollView = mTabScrollViewField.get(actionBarView);
			if (mTabScrollView == null) {
				return;
			}
	
			Field mTabSpinnerField = mTabScrollView.getClass().getDeclaredField("mTabSpinner");
			mTabSpinnerField.setAccessible(true);
	
			Object mTabSpinner = mTabSpinnerField.get(mTabScrollView);
			if (mTabSpinner == null) {
				return;
			}
	
			Method setSelectionMethod = mTabSpinner.getClass().getSuperclass().getDeclaredMethod("setSelection", Integer.TYPE, Boolean.TYPE);
			setSelectionMethod.invoke(mTabSpinner, position, animate);
	
			Method requestLayoutMethod = mTabSpinner.getClass().getSuperclass().getDeclaredMethod("requestLayout");
			requestLayoutMethod.invoke(mTabSpinner);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
