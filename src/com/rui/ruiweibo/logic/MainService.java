package com.rui.ruiweibo.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.rui.ruiweibo.model.*;
import com.rui.ruiweibo.ui.RWeiboActivity;
import com.rui.ruiweibo.util.DBLoginUserUtil;
import com.rui.ruiweibo.util.GetLoginUserInfoUtil;
import com.rui.ruiweibo.util.SharePreferencesUtil;
import com.rui.ruiweibo.util.WeiboUtil;


import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class MainService extends Service implements Runnable {
	
	// task queue
	private static Queue<Task> tasks = new LinkedList<Task>();
	private static ArrayList<Activity> appActivities = new ArrayList<Activity>();
	
	private boolean isRun;
	public LoginUserInfo recentUser;
	private Handler handler;
	
	@SuppressLint("HandlerLeak")
	public void onCreate() {
		
		// Perform loop task queue 
		isRun = true;		
		Thread thread = new Thread(this);
		thread.start();
				
		super.onCreate();
		
		handler = new  Handler() {
			
			public void handleMessage(Message msg) {
				
				RWeiboActivity activity = null;
				
				switch(msg.what) {
				
					case Task.WEIBO_LOGIN:
						
						activity = (RWeiboActivity)getActivityByName("LoginActivity");
						activity.refresh(Task.WEIBO_LOGIN, msg.obj);
						break;
					
						// get user head icon url and user name
					case Task.GET_USERINFO_BY_TOKEN:
						
						activity = (RWeiboActivity)getActivityByName("AuthActivity");
						activity.refresh(Task.GET_USERINFO_BY_TOKEN, msg.obj);
						break;
						
					case Task.GET_USERINFO_IN_LOGIN:
						
						activity = (RWeiboActivity)getActivityByName("LoginActivity");
						activity.refresh(Task.GET_USERINFO_IN_LOGIN, msg.obj);
						break;
						
					case Task.GET_WEIBOS:
						
						activity = (RWeiboActivity)getActivityByName("MainActivity");
						activity.refresh(Task.GET_WEIBOS, msg.obj);
						break;
						
					case Task.LOADMORE:
						
						activity = (RWeiboActivity)getActivityByName("MainActivity");
						activity.refresh(Task.LOADMORE, msg.obj);
						break;
						
					case Task.UPDATE_WEIBO:
						
						activity = (RWeiboActivity)getActivityByName("UpdateWeibo");
						activity.refresh(Task.UPDATE_WEIBO, msg.obj);
						break;
						
					case Task.FOLLOW:
						activity = (RWeiboActivity)getActivityByName("MoreActivity");
						activity.refresh(Task.FOLLOW, msg.obj);
						break;
						
					default:
						break;
				}
			}
		};
		
	}
	

	public void run() {

		while(isRun) {
			
			if(!tasks.isEmpty()) {
				
				doTask(tasks.poll());
			}
			
		}
	
	}
	
	public static void newTask(Task task) {
		
		tasks.add(task);
	}
	
	public static void addActivty(Activity activity) {
		
		appActivities.add(activity);
	}
	
	public static void removeActivty(Activity activity) {
		
		appActivities.remove(activity);
	}
	
	
	// Find activity object by activity name
	public Activity getActivityByName(String name) {
		
		if(!appActivities.isEmpty()) {
			
			for(Activity activity:appActivities) {
				
				if(activity.getClass().getName().indexOf(name) > 0) {
					
					return activity;
				}
			}
		}
		
		return null;
	}

	public void doTask(Task task) {
		
		Message msg = handler.obtainMessage();
		msg.what = task.getTaskID();
		
		switch(task.getTaskID()) {
		
			case Task.WEIBO_LOGIN:
				
			{
				LoginUserInfo newLoginUser = (LoginUserInfo)(task.getParams().get("loginUser"));
				Context context = getActivityByName("LoginActivity");
				SharePreferencesUtil.saveLoginUser(context, newLoginUser);
				msg.obj = "Login Success";
				
				break;
			}

			case Task.GET_USERINFO_BY_TOKEN:

			{
				    
					Oauth2AccessToken access_token = (Oauth2AccessToken)task.getParams().get("token");
					
		        	String uid = "";
		        	GetLoginUserInfoUtil.reqUID(access_token);

		        	while (uid == "") {
		        		
		        		uid = GetLoginUserInfoUtil.getUID();
		        	}
		        	
		        	long _uid = Long.parseLong(uid);
		        	LoginUserInfo addedUser = new LoginUserInfo();
		        	
		        	GetLoginUserInfoUtil.reqUserInfo(access_token, _uid);
		        	
		        	while (addedUser.getUserName().equals("")) {
		        		
		        		addedUser = GetLoginUserInfoUtil.getUserInfo();
		        		
		        	}
		        	
		        	addedUser.setUserId(uid);

		        	msg.obj="Success";
		        	
				break;
			}
			
			case Task.GET_USERINFO_IN_LOGIN:
				
			{
	        	DBLoginUserUtil db = new DBLoginUserUtil(getActivityByName("LoginActivity"));
	        	List<LoginUserInfo> users = db.getAllUserInfo();
	        	msg.obj = users;
	        	break;

			}
			
			case Task.GET_WEIBOS:

			{
				String token = (String)task.getParams().get("token");
				WeiboUtil weiboutil = new WeiboUtil();
				List<Weibo> weibos = null;
				weibos = weiboutil.getWeiboList(token, 0);
				msg.obj = weibos;
				break;
			}
			
			case Task.LOADMORE:

			{
				String token = (String)task.getParams().get("token");
				long max_id = (Long) task.getParams().get("max_id");
				WeiboUtil weiboutil = new WeiboUtil();
				List<Weibo> weibos = null;
				weibos = weiboutil.getWeiboList(token, max_id);
				msg.obj = weibos;
				break;

			}
			
			case Task.UPDATE_WEIBO:
				
			{
				String token = (String)task.getParams().get("token");
				String text = (String)task.getParams().get("text");
				WeiboUtil weiboutil = new WeiboUtil();
				if(weiboutil.update(token, text) == 0) {
					msg.obj = 0;	
				}
				else {
					msg.obj = 1;
				}
				break;
			}

			case Task.FOLLOW:

				String token = (String)task.getParams().get("token");
				
				WeiboUtil weiboutil = new WeiboUtil();
				switch(weiboutil.guanzhuMe(token)) {

					case 0:msg.obj = 0;
						break;
					case 1:msg.obj = 1;
						break;
					case 2:msg.obj = 2;
						break;
					case 3:msg.obj = 3;
						break;
				}
				
				break;				

			default :
				break;
				
		}
		
		handler.sendMessage(msg);
	}

	public static void appExit(Context context)
	{
		for (Activity activity : appActivities) {
			if(!activity.isFinishing())
				activity.finish();
		}
		
		Intent service = new Intent("com.rui.ruiweibo.logic.MainService");
		context.stopService(service);
		
		
	}
	
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
}