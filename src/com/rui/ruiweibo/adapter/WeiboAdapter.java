package com.rui.ruiweibo.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.rui.ruiweibo.ui.ImagePagerActivity;
import com.rui.ruiweibo.ui.NoScrollGridView;
import com.rui.ruiweibo.util.AsyncWeiboItemImageLoader;
import com.rui.ruiweibo.R;
import com.rui.ruiweibo.model.Weibo;

public class WeiboAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Weibo> weibos;
	
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
	
    public WeiboAdapter(Context context, ArrayList<Weibo> weibos) {
    	
		this.context = context;
		this.weibos = weibos;
	}
    
    public int getCount() {
    	
		return weibos == null? 0: weibos.size();
	}
    
    // develop in future
    
    public Object getItem(int position) {
		
		return weibos.get(position);
	}
    // check it for future.
    public long getItemId(int position) {
		
    	return 0;
	}
    
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView == null) {
			
			convertView = View.inflate(context, R.layout.weibo_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView)convertView.findViewById(R.id.txt_wb_item_uname);
			holder.tv_content = (TextView)convertView.findViewById(R.id.txt_wb_item_content);
			holder.tv_time = (TextView)convertView.findViewById(R.id.txt_wb_item_time);
			holder.tv_from = (TextView)convertView.findViewById(R.id.txt_wb_item_from);
			holder.tv_comment = (TextView)convertView.findViewById(R.id.txt_wb_item_comment);
			holder.tv_repost = (TextView)convertView.findViewById(R.id.txt_wb_item_redirect);
		
			holder.iv_userhead = (ImageView)convertView.findViewById(R.id.img_wb_item_head);
			holder.iv_isv = (ImageView)convertView.findViewById(R.id.img_wb_item_V);
			holder.contentGridview = (NoScrollGridView)convertView.findViewById(R.id.img_wb_item_content_gridview);
			holder.zContentGridview = (NoScrollGridView)convertView.findViewById(R.id.img_wb_subitem_content_gridview);
		
			holder.zlayout = (LinearLayout)convertView.findViewById(R.id.lyt_wb_item_sublayout);
			holder.tv_zcontent = (TextView)convertView.findViewById(R.id.txt_wb_item_subcontent);
		
			convertView.setTag(holder);
		}
		
		else {
			
			holder = (ViewHolder)convertView.getTag();
		}
		
		final Weibo weibo = weibos.get(position);
	    // add contents for widgets
		holder.tv_content.setText(weibo.getContent());
		holder.tv_name.setText(weibo.getUser().getName());
		holder.tv_from.setText("From:" + Html.fromHtml(weibo.getFrom()));
		holder.tv_repost.setText(weibo.getReposts_count() + "");
		holder.tv_comment.setText(weibo.getComments_count() + "");
		holder.tv_time.setText(dealTime(weibo.getTime()));
	    
		
		
		if(!weibo.getUser().getProfile_image_url().equals("")) {
			
			loadBitmap(weibo.getUser().getProfile_image_url(), holder.iv_userhead, 0, 0);
			holder.iv_userhead.setVisibility(View.VISIBLE);
			
		}
		
		if(weibo.getUser().isIsv()) {
        	
			holder.iv_isv.setVisibility(View.VISIBLE);
			
		} else {
	        	
			holder.iv_isv.setVisibility(View.GONE);
			
		}
		
		final ArrayList<String> contentImageUrls = weibo.getPicUrls();
		final ArrayList<String> bmiddleImageUrls = weibo.getBmiddlePicUrls();
		
		if ("".equals(contentImageUrls.get(0)) && contentImageUrls.size() == 1) {
			// if no image resources, hide GridView
			holder.contentGridview.setVisibility(View.GONE);
			
		} else {
			
			holder.contentGridview.setAdapter(new NoScrollGridAdapter(context, contentImageUrls));
			holder.contentGridview.setVisibility(View.VISIBLE);
			// click listener for big images in content grid view
			holder.contentGridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					imageBrower(position, bmiddleImageUrls);
				}
			});
		}
		 
		if(weibo.getWeibo() != null) {
	        	
			holder.zlayout.setVisibility(View.VISIBLE);
			holder.tv_zcontent.setText("@"+weibo.getWeibo().getUser().getName()+":"+weibo.getWeibo().getContent());
			
			final ArrayList<String> zContentImageUrls = weibo.getWeibo().getPicUrls();
			final ArrayList<String> zBmiddleImageUrls = weibo.getWeibo().getBmiddlePicUrls();
			if ("".equals(zContentImageUrls.get(0)) && zContentImageUrls.size() == 1) { 
				
				// if no image resources, hide GridView				
				holder.zContentGridview.setVisibility(View.GONE);
				
			} else {
				
				holder.zContentGridview.setAdapter(new NoScrollGridAdapter(context, zContentImageUrls));
				holder.zContentGridview.setVisibility(View.VISIBLE);
				
				// click listener for big images in zContent grid view
				holder.zContentGridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						imageBrower(position, zBmiddleImageUrls);
					}
				});
				
			}
		}
  
		else {
			
			holder.zlayout.setVisibility(View.GONE);
		}
		
		return convertView; 
	}
	
	protected void imageBrower(int position, ArrayList<String> urls2) {
		
		Intent intent = new Intent(context, ImagePagerActivity.class);
		
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		
		context.startActivity(intent);
	}
	
	public void addItem(Weibo weibo) {
		
		weibos.add(weibo);
	}
	
	private void loadBitmap(String urlStr, ImageView image,int width,int height) {
		
		AsyncWeiboItemImageLoader asyncLoader = new AsyncWeiboItemImageLoader(image, rLruCache, width, height);
		
		// load image from memory first, if no image found, then reload all images from Internet
		Bitmap bitmap = asyncLoader.getBitmapFromMemoryCache(urlStr);
		
		if (bitmap != null) {
			
			image.setImageBitmap(bitmap);
		} 
		else {
			// set image view to default image from layout
			image.setImageResource(R.drawable.user_head);
			// start async task to load image from net:
			asyncLoader.execute(urlStr);
		}
	}

	public String dealTime(String time) {
		
		Date now = new Date();
		long lNow = now.getTime() / 1000;
		//EEE MMM dd HH:mm:ss Z yyyy
		//Sun Dec 28 00:37:06 +0800 2014
		SimpleDateFormat parserSDF = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
		
		
		Date weiboDate = null;
		long lWeiboDate = 0;
		
		try {
			weiboDate = parserSDF.parse(time);
			lWeiboDate = weiboDate.getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if((lNow - lWeiboDate) < 60)
			return (lNow - lWeiboDate) + " seconds ago";
		
		else if((lNow - lWeiboDate) < 60 * 60)
			
			return ((lNow - lWeiboDate) / 60) + " minutes ago";
		
		else
			return weiboDate.getHours() + ":" + weiboDate.getMinutes();
	}
	
    public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
	
    class ViewHolder {
    	
    	private TextView tv_name;
    	private TextView tv_content;
    	private TextView tv_time;
    	private TextView tv_from;
    	private TextView tv_comment;
    	private TextView tv_repost;
    	private LinearLayout zlayout;
    	private TextView tv_zcontent;
    	private ImageView iv_userhead;
    	private ImageView iv_isv;
    	private NoScrollGridView contentGridview;
    	private NoScrollGridView zContentGridview;
    	
    }
}
