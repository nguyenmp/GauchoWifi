package com.nguyenmp.gauchowifi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.TextView;

import java.io.IOException;

public class ShowMessageActivity extends Activity {
	public static final String EXTRA_MESSAGE = "com.nguyenmp.gauchowifi.ShowMessageActivity.EXTRA_MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_message);
		
		//Set hte message view to show the message text
		String message = getIntent().getStringExtra(EXTRA_MESSAGE);
		TextView textView = (TextView) findViewById(R.id.activity_show_message_text);
		textView.setText(message);
		
		//Set logout button to commit a log out thread
		Button logout = (Button) findViewById(R.id.activity_show_message_logout);
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("https://login.wireless.ucsb.edu/logout.html"));
				startActivity(intent);
			}
		});
	}
}
