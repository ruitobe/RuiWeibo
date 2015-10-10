package com.rui.ruiweibo.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class AsyncWeiboItemImageLoader extends AsyncTask<String, Void, Bitmap> {
	
	private ImageView image;  
    private LruCache<String, Bitmap> lruCache;  
    private int width;
    private int height;
    
    // constructor:
    public AsyncWeiboItemImageLoader(ImageView image, LruCache<String, Bitmap> lruCache, int width, int height) {
    	
    	super();
        this.image = image;  
        this.lruCache = lruCache;  
        this.width=width;
        this.height=width;
    }  
    
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		
        Bitmap bitmap = GetLoginUserInfoUtil.getBitmap(params[0]);
        
        if(width != 0 && height != 0) {
        	
        	bitmap = GetLoginUserInfoUtil.scaleImg(bitmap, width, height);
        }
        	
        addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        
        return bitmap; 
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// TODO Auto-generated method stub
		super.onPostExecute(bitmap);
		image.setImageBitmap(bitmap);
		image.invalidate();
	}

	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		
        if (getBitmapFromMemoryCache(key) == null) {
        	
            lruCache.put(key, bitmap);  
        }  
    }  
  
    public Bitmap getBitmapFromMemoryCache(String key) {
    	
        return lruCache.get(key);  
    }
    
    public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
}
