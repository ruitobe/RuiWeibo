package com.rui.ruiweibo.ui;

//for WEIBO_ANDROID_SDK V2.5.1

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.rui.ruiweibo.R;
import com.rui.ruiweibo.model.Task;
import com.rui.ruiweibo.logic.MainService;
import com.rui.ruiweibo.model.Constants;
import com.rui.ruiweibo.util.AccessTokenKeeper;
import com.rui.ruiweibo.util.AsyncHeadIconLoader;
import com.rui.ruiweibo.util.DBLoginUserUtil;
import com.rui.ruiweibo.util.GetLoginUserInfoUtil;


import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class AuthActivity extends Activity implements RWeiboActivity {
 
 private WeiboAuth rWeiboAuth;
 private Oauth2AccessToken rAccessToken; 

 private Button authBtn;
 private Dialog rDialog;
 private ProgressDialog progressDialog;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		rWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		
		rAccessToken = AccessTokenKeeper.readAccessToken(this);
		
		if (rAccessToken.isSessionValid()) {
			
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Loading users information，please wait...");
			progressDialog.show();
			
			/*
			 * new task: GET_USERINFO_BY_TOKEN
			 * get user info by access token and then save it to database
			 */
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("token", rAccessToken);
			Task task = new Task(Task.GET_USERINFO_BY_TOKEN, params); 
			
			MainService.newTask(task);
			MainService.addActivty(AuthActivity.this);
			
     	}
		
		else {
			
			init();
		}
	}
	
	public void refresh(int taskID, Object... objects) {

		if(((String)objects[0]).equals("Success")) {
			
			progressDialog.dismiss();
			
			// start loading bitmap AsyncTask:
	    	AsyncHeadIconLoader bitmapAsyncTask = new AsyncHeadIconLoader();

	    	bitmapAsyncTask.execute(GetLoginUserInfoUtil.getUserInfo().getUserIconUrl());
	    	
			Bitmap bm = null;
			try {
				
				// get bitmap by AsyncTask:
				bm = bitmapAsyncTask.get();
				GetLoginUserInfoUtil.getUserInfo().setUserIcon(bm);
				
				// save new added user into database:
				
				DBLoginUserUtil db = new DBLoginUserUtil(AuthActivity.this);
	        	
	        	if(db.getUserInfoByUserId(GetLoginUserInfoUtil.getUserInfo().getUserId()) == null) {
	        		
	        		db.insertUserInfo(GetLoginUserInfoUtil.getUserInfo());

	        	}
	        	else {
	        		
	        		//sop("update db");
	        	}
				
				
			} 
			
			catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			MainService.removeActivty(AuthActivity.this);
			
			//Jump back to LoginActivity
			Intent intent = new Intent(AuthActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			
		}
		
	}
	
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			
			
			rAccessToken = Oauth2AccessToken.parseAccessToken(values);
			
			if (rAccessToken.isSessionValid()) {

				AccessTokenKeeper.writeAccessToken(AuthActivity.this, rAccessToken);
				Toast.makeText(AuthActivity.this, R.string.weibo_toast_auth_succeed, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(AuthActivity.this, AuthActivity.class));
				finish();
			}
			
			else {
				
				String code = values.getString("code");
				String message = getString(R.string.weibo_toast_auth_failed);
				
				if (!TextUtils.isEmpty(code)) {
					
					message = message + "\nObtained the code: " + code;
	                }
				
	                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_LONG).show();
	            }
			
	        }
		
		@Override
		public void onCancel() {
			
			Toast.makeText(AuthActivity.this, R.string.weibo_toast_auth_canceled, Toast.LENGTH_LONG).show();
	        }
		
		@Override
		public void onWeiboException(WeiboException e) {
			
			Toast.makeText(AuthActivity.this, "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	}

	@Override
	public void init() {
	
		View diaView=View.inflate(this, R.layout.dialog, null);
		
		rDialog=new Dialog(AuthActivity.this, R.style.dialog);
		rDialog.setContentView(diaView);
		rDialog.show();
		
		authBtn = (Button)diaView.findViewById(R.id.auth_btn);
		
		authBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				rWeiboAuth.anthorize(new AuthListener());

			}
		});
		
	}
	/*
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
	*/
}
