package com.rui.ruiweibo.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.rui.ruiweibo.model.LoginUserInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;


public class DBLoginUserUtil {
	
	private DBHelper dbHelper;
	private String[] columns = {LoginUserInfo.ID, LoginUserInfo.IS_DEFAULT, LoginUserInfo.TOKEN,
			LoginUserInfo.USER_ID, LoginUserInfo.USER_NAME, LoginUserInfo.USER_ICON};
	
	public DBLoginUserUtil(Context context) {
		
		dbHelper = new DBHelper(context);
	}	
	
	public void insertUserInfo(LoginUserInfo user) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		sop("user.getUserIcon = " +  user.getUserIcon());
		user.getUserIcon().compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] usericon = baos.toByteArray();
		
		ContentValues values = new ContentValues(5);
		values.put(LoginUserInfo.USER_ID,user.getUserId());
		values.put(LoginUserInfo.USER_NAME, user.getUserName());
		values.put(LoginUserInfo.TOKEN,user.getToken());
		values.put(LoginUserInfo.USER_ICON, usericon);
		
		db.insert(LoginUserInfo.TB_NAME, null, values);
		db.close();
		
	}
	
	public LoginUserInfo getUserInfoByUserId(String uid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		LoginUserInfo userInfo = null;
		
		Cursor cursor =db.query(LoginUserInfo.TB_NAME, new String[]{
				LoginUserInfo.ID, LoginUserInfo.IS_DEFAULT, LoginUserInfo.TOKEN, 
				LoginUserInfo.USER_ID, LoginUserInfo.USER_NAME, LoginUserInfo.USER_ICON}, 
				LoginUserInfo.USER_ID +"=?", new String[]{uid}, null, null, null);
			
		
		if(null != cursor) {
			
			if(cursor.getCount() > 0) {
				
				cursor.moveToFirst();
				userInfo = new LoginUserInfo();
				
				Long id = cursor.getLong(cursor.getColumnIndex(LoginUserInfo.ID));
				String uId = cursor.getString(cursor.getColumnIndex(LoginUserInfo.USER_ID));
				String userName = cursor.getString(cursor.getColumnIndex(LoginUserInfo.USER_NAME));
				String token = cursor.getString(cursor.getColumnIndex(LoginUserInfo.TOKEN));
				byte[] byteIcon = cursor.getBlob(cursor.getColumnIndex(LoginUserInfo.USER_ICON));
				userInfo.setId(id);
				userInfo.setUserId(uId);
				userInfo.setToken(token);
				userInfo.setToken(token);
				userInfo.setUserName(userName);
				
				if(null != byteIcon) {
					
					Bitmap userIcon=BitmapFactory.decodeByteArray(byteIcon, 0, byteIcon.length);
					userInfo.setUserIcon(userIcon);
				}
			}
		}
		
		cursor.close();
		db.close();
		return userInfo;
	}


	public List<LoginUserInfo> getAllUserInfo() {
		
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		
		//List<LoginUser> users = new ArrayList<LoginUser>();
		List<LoginUserInfo> users = null;
		
		Cursor cursor = db.query(LoginUserInfo.TB_NAME, columns,
				null, null, null, null, null);
		LoginUserInfo userInfo = null;
		
		if (null != cursor &&  cursor.getCount() > 0) {
			
			users = new ArrayList<LoginUserInfo>(cursor.getCount());
			
			while(cursor.moveToNext()) {
				
				userInfo = new LoginUserInfo();
				
				Long id = cursor.getLong(cursor.getColumnIndex(LoginUserInfo.ID));
				String uId = cursor.getString(cursor.getColumnIndex(LoginUserInfo.USER_ID));
				String userName = cursor.getString(cursor.getColumnIndex(LoginUserInfo.USER_NAME));
				String token = cursor.getString(cursor.getColumnIndex(LoginUserInfo.TOKEN));
				byte[] byteIcon = cursor.getBlob(cursor.getColumnIndex(LoginUserInfo.USER_ICON));
				
				userInfo.setId(id);
				userInfo.setUserId(uId);
				userInfo.setToken(token);
				userInfo.setUserName(userName);
				
				if(null != byteIcon) {
					
					Bitmap userIcon = BitmapFactory.decodeByteArray(byteIcon, 0, byteIcon.length);
					Log.i("OUTPUT","dbuserinfo"+userIcon);
					userInfo.setUserIcon(userIcon);
				}
				users.add(userInfo);
			}
		}
		
		cursor.close();
		
		db.close();
		return users;
	}
	
	// update user token information
	public void  updateUserInfo(LoginUserInfo user) {
		
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(LoginUserInfo.TOKEN, user.getToken());
		
		db.update(LoginUserInfo.TB_NAME, values, LoginUserInfo.USER_ID + "=?", new String[]{user.getUserId()});
		
		db.close();
	}
	
	// update user head icon and user name
	public void updateUserInfo(String userId,String userName,Bitmap userIcon) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues(2);
		values.put(LoginUserInfo.USER_NAME, userName);
		
		ByteArrayOutputStream os= new ByteArrayOutputStream();
		userIcon.compress(CompressFormat.PNG, 100, os);
		
		values.put(LoginUserInfo.USER_ICON, os.toByteArray());
		
		db.update(LoginUserInfo.TB_NAME, values, LoginUserInfo.USER_ID + "=?", new String[]{userId});
		db.close();
	}
	
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
}
