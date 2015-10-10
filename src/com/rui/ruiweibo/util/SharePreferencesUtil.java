package com.rui.ruiweibo.util;

import com.rui.ruiweibo.model.LoginUserInfo;

import android.content.Context;
import android.content.SharedPreferences;


public class SharePreferencesUtil {
	
	public static void saveLoginUser(Context context,LoginUserInfo user) {
		
		SharedPreferences sp = context.getSharedPreferences("LOGIN_USER", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		editor.putString(LoginUserInfo.TOKEN, user.getToken());
		editor.putString(LoginUserInfo.USER_ID, user.getUserId());
		editor.putString(LoginUserInfo.IS_DEFAULT, user.getIsDefault());
		editor.putString(LoginUserInfo.USER_NAME, user.getUserName());
		editor.commit();
	}

	public static LoginUserInfo getLoginUser(Context context) {
		
		SharedPreferences sp = context.getSharedPreferences("LOGIN_USER",Context.MODE_PRIVATE);
		String userName = sp.getString(LoginUserInfo.USER_NAME, "");
		String isDefault = sp.getString(LoginUserInfo.IS_DEFAULT, "");
		String userId = sp.getString(LoginUserInfo.USER_ID, "");
		String token = sp.getString(LoginUserInfo.TOKEN, "");
		
		if("".equals(userName))
			return null;
		
		else
			return new LoginUserInfo(userId, userName, token, isDefault);
	}
	
	public static void removeLoginUser(Context context) {
		
		SharedPreferences sp = context.getSharedPreferences("LOGIN_USER",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(LoginUserInfo.TOKEN);
		editor.remove(LoginUserInfo.USER_ID);
		editor.remove(LoginUserInfo.USER_NAME);	
		editor.commit();
	}
	
}
