package com.rui.ruiweibo.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.rui.ruiweibo.model.LoginUserInfo;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;

import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.AccountAPI;

import com.sina.weibo.sdk.net.RequestListener;

/*
 * get user info by weibo API
 */

public class GetLoginUserInfoUtil {

	public static String UID = "";
	private static LoginUserInfo user = new LoginUserInfo();
	
	/*
	 * request UID by access token
	 */
	
	public static void reqUID(Oauth2AccessToken accessToken) {
		
		AccountAPI account = new AccountAPI(accessToken);
		
		account.getUid(new RequestListener() {
			
			public void onComplete(String result) {
				try {
					
					JSONObject object = new JSONObject(result);
					UID = object.getString("uid");
				} 
				catch (JSONException e) {
					
					e.printStackTrace();
				}
			}
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/*
	 * get UID after request
	 */
	
	public static String getUID() {
		
		String id = UID;
		UID = "";
		return id;
	}

	/*
	 * request user info by access token and UID
	 */
	public static void reqUserInfo(final Oauth2AccessToken accessToken, long uid) {
		
		user = new LoginUserInfo();
		UsersAPI userapi = new UsersAPI(accessToken);
		
		userapi.show(uid, new RequestListener() {
			
			public void onComplete(String arg0) {
				
				JSONObject object;
				
				try {
					object = new JSONObject(arg0);
					
					// try AsyncTask to start network task to avoid:
					/*
					 12-30 21:01:14.440: W/System.err(6025): android.os.NetworkOnMainThreadException
					 12-30 21:01:14.440: W/System.err(6025): 	at android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork(StrictMode.java:1147)
					 */
					
					//Bitmap bm = GetLoginUserInfoUtil.getBitmap(object.getString("profile_image_url"));
					//GetLoginUserInfoUtil.user.setUserIcon(bm);
					GetLoginUserInfoUtil.user.setIsDefault("0");
					GetLoginUserInfoUtil.user.setToken(accessToken.getToken());
					GetLoginUserInfoUtil.user.setUserName(object.getString("screen_name"));
					GetLoginUserInfoUtil.user.setUserIconUrl(object.getString("profile_image_url"));
					
				} 
				catch (JSONException e) {
					
					e.printStackTrace();
				}
				
				
			}

			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	public static LoginUserInfo getUserInfo() {

		return user;
	}
	
	public static Bitmap getBitmap(String biturl) {
		
		Bitmap bitmap=null;
		
		try {
			
			URL url = new URL(biturl);
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(new BufferedInputStream(in));

		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return bitmap;
		
	}
	
	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight)  {

	    int width = bm.getWidth();
	    int height = bm.getHeight();

	    int newWidth1 = newWidth;
	    int newHeight1 = newHeight;

	    float scaleWidth = ((float) newWidth1) / width;
	    float scaleHeight = ((float) newHeight1) / height;

	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);

	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
	    return newbm;
	    
	 }
	
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
}
