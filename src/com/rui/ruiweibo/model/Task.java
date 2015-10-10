package com.rui.ruiweibo.model;

import java.util.Map;

public class Task {
	
	private int taskID;

	private Map<String, Object> Params;
	
	// Task id constants
	
	// Login task id
	public static final int WEIBO_LOGIN = 1;
	
	// Get user info by access token and save it to database
	public static final int GET_USERINFO_BY_TOKEN = 2;
	
	// Get user info in login
	public static final int GET_USERINFO_IN_LOGIN = 3;
	
	// Get weibo
	public static final int GET_WEIBOS = 4;
	
	// Load more weibo
	public static final int LOADMORE = 5;
	
	// Send weibo
	public static final int UPDATE_WEIBO = 6;
	
	// Follow
	public static final int FOLLOW = 0;
	
	
	
	// Constructor
	public Task(int taskID, Map<String, Object> Params) {
		
		this.Params = Params;
		this.taskID = taskID;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		
		this.taskID = taskID;
	}

	public Map<String, Object> getParams() {
		
		return Params;
	}

	public void setParams(Map<String, Object> params) {
		
		Params = params;
	}
	
}
