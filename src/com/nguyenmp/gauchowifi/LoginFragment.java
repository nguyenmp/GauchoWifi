package com.nguyenmp.gauchowifi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.app.ProgressDialog;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginFragment extends Fragment {
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
		
		final Button checkButton = (Button) inflatedView.findViewById(R.id.account_check_button);
		checkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String username = mUsername.getText().toString();
				String password = mPassword.getText().toString();
				
				ProgressDialog progressDialog = new ProgressDialog(checkButton.getContext());
				progressDialog.setTitle("Check Login");
				progressDialog.setMessage("Please wait...");
				progressDialog.show();
				
				Handler handler = new CheckLoginHandler(progressDialog);
				Thread checkLoginThread = new CheckLoginThread(username, password, handler);
				checkLoginThread.start();
			}
		});
		
		return inflatedView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_USERNAME, mUsername.getText().toString());
		outState.putString(KEY_PASSWORD, mPassword.getText().toString());
	}
	
	private static class CheckLoginThread extends HandledThread {
		private final String mUsername, mPassword;
		
		CheckLoginThread(String username, String password, Handler handler) {
			mUsername = username;
			mPassword = password;
			setHandler(handler);
		}
		
		@Override
		public void run() {
			try {
				dispatchMessage(login(mUsername, mPassword));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				dispatchMessage(e);
			} catch (IOException e) {
				e.printStackTrace();
				dispatchMessage(e);
			}
		}
	}
	
	private static class CheckLoginHandler extends Handler {
		private final ProgressDialog mProgressDialog;
		
		CheckLoginHandler(ProgressDialog dialog) {
			mProgressDialog = dialog;
		}
		
		public void handleMessage(Message message) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				if (message.obj instanceof Boolean) {
					AlertDialog.Builder builder = new AlertDialog.Builder(mProgressDialog.getContext());
					builder.setTitle("Check Login");
					builder.setMessage("Login " + ((Boolean) message.obj ? "Valid" : "Invalid"));
					builder.show();
				} else if (message.obj instanceof Exception) {
					AlertDialog.Builder builder = new AlertDialog.Builder(mProgressDialog.getContext());
					builder.setTitle("Check Login");
					builder.setMessage("Check failed due to:\n" + ((Exception) message.obj).toString());
					builder.show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(mProgressDialog.getContext());
					builder.setTitle("Check Login");
					builder.setMessage("Check failed due to unknown reasons.");
					builder.show();
				}
			}
		}
	}
	
	private static boolean login(String username, String password) throws IOException {
		//Create the get url connection
		byte[] encodedPassword = Base64.encode(password.getBytes(), Base64.DEFAULT);
		String url = String.format("https://secure.identity.ucsb.edu/netid_diags/?rs=diags_authenticate&rst=&rsrnd=&rsargs[]=%s&rsargs[]=%s", URLEncoder.encode(username, "UTF-8"), URLEncoder.encode(new String(encodedPassword), "UTF-8"));
		
		//Connect
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		
		//Store response
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		int bytesRead;
		char[] buffer = new char[1024];
		StringBuilder builder = new StringBuilder();
		while ((bytesRead = reader.read(buffer, 0, buffer.length)) != -1) {
			builder.append(buffer, 0, bytesRead);
		}
		
		//Return the negation of an error
		return !(builder.toString().equals("+:var res = 'both'; res;") ||
				builder.toString().equals("+:var res = 'primary'; res;") ||
				builder.toString().equals("+:var res = 'legacy'; res;"));
	}
}
