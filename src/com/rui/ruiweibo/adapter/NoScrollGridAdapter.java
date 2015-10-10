package com.rui.ruiweibo.adapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.rui.ruiweibo.R;
import com.rui.ruiweibo.util.AsyncWeiboItemImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NoScrollGridAdapter extends BaseAdapter {

	private Context rContext;
	private ArrayList<String> imageUrls;
	
	// get current app's max allocated memory  
	private final int maxMemory = (int) Runtime.getRuntime().maxMemory();
		
	private final int cacheSize = maxMemory / 5;
    private LruCache<String, Bitmap> rLruCache = new LruCache<String, Bitmap>(cacheSize) {
    	
    	protected int sizeOf(String key, Bitmap bitmap) {
    		
    		// overwrite sizeof()
            // replaced by getByteCount() in API 12
    		// unit in KB
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;   
        }  
    };
	
	public NoScrollGridAdapter(Context rContext, ArrayList<String> imageUrls) {
		
		this.rContext = rContext;
		this.imageUrls = imageUrls;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls == null ? 0 : imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = View.inflate(rContext, R.layout.image_item_gridview, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.weibo_image);

		loadBitmap(imageUrls.get(position), imageView, 0, 0);
		imageView.setVisibility(View.VISIBLE);
		
		return view;
	}
	
	private void loadBitmap(String urlStr, ImageView image,int width,int height) {
		
		AsyncWeiboItemImageLoader asyncLoader = new AsyncWeiboItemImageLoader(image, rLruCache, width, height);
		
		// load image from memory first, if no image found, then reload all images from Internet
		final Bitmap bitmap = asyncLoader.getBitmapFromMemoryCache(urlStr);
		
		if (bitmap != null) {
			
			image.setImageBitmap(bitmap);
			image.invalidate();
		} 
		else {
			// set image view to default image from layout
			image.setImageResource(R.drawable.user_head);
			// start async task to load image from net:
			
			asyncLoader.execute(urlStr);
			
		}
	}

	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
}
