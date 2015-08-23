package com.ashinet.whatismick;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Context mContext;
	String mUserEmail = "Guest@gmail.com";
	String mUserName = "Guest";
	EditText mEditText;
	Button mButton;
	Button mButtonWatch;
	ProgressDialog mProgress;
	Activity selfRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// for parse
		try {
			Parse.initialize(this, "0SuUdNtxurH5Tisnghl8usVmZ0jdyN09ABXXjGvj",
					"unPn9xCJssHunmdylPD0djUbfqVbtTQhaFa2sMO2");
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		setContentView(R.layout.activity_main);
		mContext = getBaseContext();
		selfRef = this;
		// debug
		Account account = getAccount(AccountManager.get(mContext));
		if (account != null) {
			mUserEmail = account.name;
			mUserName = mUserEmail.substring(0, mUserEmail.lastIndexOf("@"));
			Toast.makeText(mContext, mUserName, Toast.LENGTH_SHORT).show();
		}

		init();
	}

	private void init() {
		mEditText = (EditText) findViewById(R.id.description);
		mButton = (Button) findViewById(R.id.btn_send);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEditText != null
						&& !TextUtils.isEmpty(mEditText.getText().toString())) {
					mUploadData.execute();
				} else {
					Toast.makeText(mContext, R.string.error_empty,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		mButtonWatch = (Button) findViewById(R.id.btn_watch);
		SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
		if (prefs.getBoolean("IS_SEND", false)) {
			mButtonWatch.setEnabled(true);
			mButtonWatch.setAlpha(1.0f);
		}else{
			mButtonWatch.setEnabled(false);
			mButtonWatch.setAlpha(0.5f);
		}
		
		mButtonWatch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, GetData.class));
			}
		});
	}

	AsyncTask<Void, Void, Boolean> mUploadData = new AsyncTask<Void, Void, Boolean>() {
		@Override
		protected void onPreExecute() {
			mEditText.setEnabled(false);
			mEditText.setAlpha(0.5f);
			mButton.setEnabled(false);
			mButton.setAlpha(0.5f);
			mProgress = ProgressDialog.show(selfRef, getString(R.string.progress_title), getString(R.string.progress_message), true);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if (mEditText != null&& !TextUtils.isEmpty(mEditText.getText().toString())) {
				ParseObject testObject = new ParseObject("Description");
				testObject.put("UserName", mUserName);
				testObject.put("Content", mEditText.getText().toString());
				try {
					testObject.save();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				return true;
			}else{
				return false;
			}
			
		};
		
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(mContext, R.string.error_empty,
						Toast.LENGTH_SHORT).show();
			}else{
				mButtonWatch.setEnabled(true);
				mButtonWatch.setAlpha(1.0f);
				SharedPreferences prefs = selfRef.getPreferences(Context.MODE_PRIVATE);
				prefs.edit().putBoolean("IS_SEND", true).apply();
			}
			if (mProgress != null) {
				mProgress.dismiss();
			}
			
		}


	};

	public static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

}
