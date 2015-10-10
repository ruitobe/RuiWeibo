package com.rui.ruiweibo.ui;

import java.util.ArrayList;

import com.rui.ruiweibo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

public class ImagePagerActivity extends FragmentActivity {

	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	
	private ViewPager rPager;
	private int position;
	private TextView indicator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		
		position = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		rPager = (ViewPager)findViewById(R.id.weiboImagePager);
		ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
		
		ImagePagerAdapter rAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		rPager.setAdapter(rAdapter);
		indicator = (TextView)findViewById(R.id.weiboImageIndicator);
		CharSequence text = getString(R.string.weibo_image_indicator, 1, rPager.getAdapter().getCount());
		indicator.setText(text);
		
		rPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				CharSequence text = getString(R.string.weibo_image_indicator, arg0 + 1, rPager.getAdapter().getCount());
				indicator.setText(text);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		if (savedInstanceState != null) {
			
			position = savedInstanceState.getInt(STATE_POSITION);
		}
		
		rPager.setCurrentItem(position);
	}
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_POSITION, rPager.getCurrentItem());
	}



	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
			
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			
			String url = fileList.get(position);
			return ImageDetail.newInstance(url);
		}

	}
	
}
