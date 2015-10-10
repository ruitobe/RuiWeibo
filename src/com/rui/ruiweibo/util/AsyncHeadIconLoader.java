package com.rui.ruiweibo.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class AsyncHeadIconLoader extends AsyncTask<String, Void, Bitmap> {

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		Bitmap bitmap=null;
		
		try {
			
			URL url = new URL(params[0]);
			
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(new BufferedInputStream(in));

		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		
	}
	
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
}
