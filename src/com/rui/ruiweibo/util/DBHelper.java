package com.rui.ruiweibo.util;

import com.rui.ruiweibo.model.LoginUserInfo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
	
    public DBHelper(Context context) {
    	
        super(context,"weibo", null, 1);
    }
    
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    	
        String sql = "CREATE TABLE IF NOT EXISTS  " + LoginUserInfo.TB_NAME + "( _id INTEGER PRIMARY KEY,userId TEXT, userName TEXT, token TEXT,isDefault TEXT,userIcon BLOB)";
        sqLiteDatabase.execSQL(sql); 
    }  
    
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
