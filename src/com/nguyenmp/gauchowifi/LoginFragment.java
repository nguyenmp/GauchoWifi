package com.nguyenmp.gauchowifi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class LoginFragment extends SherlockFragment {
	public static final String KEY_USERNAME = "username_key";
	public static final String KEY_PASSWORD = "password_key";
	/**
	 * The Keys for getting the encoded username and password
	 */
	public static final String KEY_USERNAME_BASE64 = "username_key_base64", KEY_PASSWORD_BASE64 = "password_key_base64";
	private EditText mUsername, mPassword;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle inState) {
		View inflatedView = inflater.inflate(R.layout.account, container, false);
		
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		//Remove deprecated unencrypted stores
		prefs.edit().remove(KEY_USERNAME).remove(KEY_PASSWORD).commit();
		
		//Use encrypted stores instead
		final ObscuredSharedPreferences obscuredPrefs = new ObscuredSharedPreferences(getActivity(), prefs);
		
		mUsername = (EditText) inflatedView.findViewById(R.id.account_username_edit_text);
		mPassword = (EditText) inflatedView.findViewById(R.id.account_password_edit_text);
		
		if (inState != null) {
			mUsername.setText(inState.getString(KEY_USERNAME));
			mPassword.setText(inState.getString(KEY_PASSWORD));
		} else {
			mUsername.setText(obscuredPrefs.getString(LoginFragment.KEY_USERNAME_BASE64, null));
			mPassword.setText(obscuredPrefs.getString(LoginFragment.KEY_PASSWORD_BASE64, null));
		}
		
		final Button storeButton = (Button) inflatedView.findViewById(R.id.account_store_button);
		
		storeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				obscuredPrefs.edit().putString(LoginFragment.KEY_USERNAME_BASE64, mUsername.getText().toString())
						.putString(LoginFragment.KEY_PASSWORD_BASE64, mPassword.getText().toString())
						.commit();
				
				Toast.makeText(v.getContext(), "You are now ready!  You can quit now. :)", Toast.LENGTH_LONG).show();
			}
		});
		
		return inflatedView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_USERNAME, mUsername.getText().toString());
		outState.putString(KEY_PASSWORD, mPassword.getText().toString());
	}
}
