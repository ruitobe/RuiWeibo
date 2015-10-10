package com.rui.ruiweibo.model;
import android.graphics.Bitmap;

public class LoginUserInfo
{

	private Long id;
	private String userId;
	private String userName;
	private String token;
	private String isDefault;
	public Bitmap userIcon;
	private String userIconUrl;
	private Boolean isV;

	/*
	 * constants used to create database table
	 */
	public static final String TB_NAME = "UserInfo";
	
	public static final String ID = "_id";
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	
	public static final String TOKEN="token";
	public static final String IS_DEFAULT="isDefault";
	public static final String USER_ICON="userIcon";
	

	public LoginUserInfo(String userId, String userName, String token, String isDefault) {
		
		this.userId = userId;
		this.userName = userName;
		this.token = token;
		this.isDefault = isDefault;
	}	
	
	public LoginUserInfo() {
		
		super();
		this.userName = "";
	}

	public Long getId() {
		
		return id;
	}
	
	public void setId(Long id) {
		
		this.id = id;
	}
	
	public String getUserId() {
		
		return userId;
	}
	
	public void setUserId(String userId) {
		
		this.userId = userId;
	}
	
	public String getUserName() {
		
		return userName;
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
	
	public String getToken() {
		
		return token;
	}
	
	public void setToken(String token) {
		
		this.token = token;
	}
	
	public String getIsDefault() {
		
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		
		this.isDefault = isDefault;
	}
	
	public Bitmap getUserIcon() {
		
		return userIcon;
	}
	
	public void setUserIcon(Bitmap userIcon) {
		
		this.userIcon = userIcon;
	}
	
	public String getUserIconUrl() {
		
		return userIconUrl;
	}
	
	public void setUserIconUrl(String userIconUrl) {
		
		this.userIconUrl = userIconUrl;
	}

	public Boolean getIsV() {
		
		return isV;
	}
	public void setIsV(Boolean isV) {
		
		this.isV = isV;
	}
	
}
