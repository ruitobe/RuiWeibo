package com.rui.ruiweibo.ui;


import com.rui.ruiweibo.R;
import com.rui.ruiweibo.util.AsyncWeiboItemImageLoader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ImageDetail extends Fragment {
	
	// get current app's max allocated memory  
	private final int maxMemory = (int) Runtime.getRuntime().maxMemory();
		
		//provide fifth of allocated memory to cache the image 
	private final int cacheSize = maxMemory / 5;
	
	private LruCache<String, Bitmap> rLruCache = new LruCache<String, Bitmap>(cacheSize) {
		
		protected int sizeOf(String key, Bitmap bitmap) {
			// overwrite sizeof()
			// replaced by getByteCount() in API 12
			// unit in KB
			return bitmap.getRowBytes() * bitmap.getHeight() / 1024;   
	        }  
	    };
	
	private String rImageUrl;
	private ImageView rImageView;
	//private ProgressBar progressBar;
	private PhotoViewAttacher rAttacher;

	public static ImageDetail newInstance(String imageUrl) {
		final ImageDetail id = new ImageDetail();

		final Bundle args = new Bundle();
		
		args.putString("url", imageUrl);
		id.setArguments(args);

		return id;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		rImageView = (ImageView)v.findViewById(R.id.bmiddleWeiboImage);
		rAttacher = new PhotoViewAttacher(rImageView);
		
		rAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				// when user single tap the image, it goes back to the weibo list
				getActivity().finish();
			}
		});
		
		//progressBar = (ProgressBar)v.findViewById(R.id.loading);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		// load image and display image
		loadImage(rImageUrl, rImageView, 0, 0);
		rAttacher.update();
	}
	
	private void loadImage(String urlStr, ImageView image, int width, int height) {
		
		AsyncWeiboItemImageLoader asyncLoader = new AsyncWeiboItemImageLoader(image, rLruCache, width, height);
		
		// load image from memory first, if no image found, then reload all images from Internet
		Bitmap bitmap = asyncLoader.getBitmapFromMemoryCache(urlStr);
		
		if (bitmap != null) {
			
			image.setImageBitmap(bitmap);
		} 
		else {
			// start async task to load image from net:
			asyncLoader.execute(urlStr);
		}
		
	}
	
}
