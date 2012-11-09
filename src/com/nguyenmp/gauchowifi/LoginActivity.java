package com.nguyenmp.gauchowifi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoginActivity extends SherlockFragmentActivity {
	/** @deprecated We no longer store in clear text. Use {@linkplain KEY_USERNAME_BASE64} */
	public static final String KEY_USERNAME = "username_key";
	/** @deprecated We no longer store in clear text. Use {@linkplain KEY_PASSWORD_BASE64} */
	public static final String KEY_PASSWORD = "password_key";
	/**
	 * The Keys for getting the encoded username and password
	 */
	public static final String KEY_USERNAME_BASE64 = "username_key_base64", KEY_PASSWORD_BASE64 = "password_key_base64";
	
	public void onCreate(Bundle inState) {
		super.onCreate(inState);
		super.setContentView(R.layout.account);
		
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Remove deprecated unencrypted stores
		prefs.edit().remove(KEY_USERNAME).remove(KEY_PASSWORD).commit();
		
		//Use encrypted stores instead
		final ObscuredSharedPreferences obscuredPrefs = new ObscuredSharedPreferences(this, prefs);

		final EditText usernameEditText = (EditText) super.findViewById(R.id.account_username_edit_text);
		final EditText passwordEditText = (EditText) super.findViewById(R.id.account_password_edit_text);

		usernameEditText.setText(obscuredPrefs.getString(LoginActivity.KEY_USERNAME_BASE64, null));
		passwordEditText.setText(obscuredPrefs.getString(LoginActivity.KEY_PASSWORD_BASE64, null));
		
		final Button storeButton = (Button) super.findViewById(R.id.account_store_button);
		
		storeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				obscuredPrefs.edit().putString(LoginActivity.KEY_USERNAME_BASE64, usernameEditText.getText().toString())
						.putString(LoginActivity.KEY_PASSWORD_BASE64, passwordEditText.getText().toString())
						.commit();
				
				Toast.makeText(v.getContext(), "You are now ready!  You can quit now. :)", Toast.LENGTH_LONG).show();
			}
		});
	}
}
