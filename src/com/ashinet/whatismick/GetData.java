package com.ashinet.whatismick;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GetData extends Activity {
	Context mContext;
	TextView dataTxView;
	ProgressDialog mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_getdata);
		mContext = getBaseContext();

		dataTxView = (TextView) findViewById(R.id.data);
		mProgress = ProgressDialog.show(this, "","", true);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Description");

		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> list, ParseException arg1) {
				Toast.makeText(mContext, "list:" + list.size(), Toast.LENGTH_SHORT).show();
				StringBuilder build = new StringBuilder();
				for (ParseObject parseObject : list) {
					build.append(parseObject.getString("Content"));
					build.append("\n");
				}
				
				dataTxView.setText(build.toString());
				mProgress.dismiss();
			}
		});
		
		
	}

}
