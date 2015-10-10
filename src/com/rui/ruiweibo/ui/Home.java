package com.rui.ruiweibo.ui;

import java.util.HashMap;
import java.util.Map;

import com.rui.ruiweibo.R;
import com.rui.ruiweibo.logic.MainService;
import com.rui.ruiweibo.model.Task;
import com.rui.ruiweibo.util.SharePreferencesUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Fragment {

	private View homeView;
	
	private TextView titleTv;
	private Button refreshBtn;
	private Button updateBtn;
	private Button loadMoreBtn;
	private Activity thisActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		homeView = inflater.inflate(R.layout.home, container, false);

		init();
        return homeView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		// Attached to MainActivity
		thisActivity = activity;
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
		// home view from MainActivity:
		MainActivity.weiboList = (ListView)homeView.findViewById(R.id.lv_weibos);
		MainActivity.progress = (LinearLayout)homeView.findViewById(R.id.layout_progress);
		titleTv = (TextView)homeView.findViewById(R.id.txt_wb_title);
		refreshBtn = (Button)homeView.findViewById(R.id.btn_refresh);
		updateBtn = (Button)homeView.findViewById(R.id.btn_writer);
		
		//set bottom list view - loading more

		View loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreBtn = (Button)loadMoreView.findViewById(R.id.loadMoreButton);  
        MainActivity.weiboList.addFooterView(loadMoreView);   
		
        MainActivity.weiboList.setFadingEdgeLength(0);
        
		final String token = SharePreferencesUtil.getLoginUser(thisActivity.getApplicationContext()).getToken();
		
		titleTv.setText(SharePreferencesUtil.getLoginUser(thisActivity.getApplicationContext()).getUserName());

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("token", token);
		Task task = new Task(Task.GET_WEIBOS, params);
		MainActivity.progress.setVisibility(View.VISIBLE);
		MainService.newTask(task);
		MainService.addActivty(thisActivity);
		
		
		loadMoreBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("token", token);
				params.put("max_id", MainActivity.weibos.get(MainActivity.weibos.size() - 1).getWid());
				loadMoreBtn.setText("Loading more，please wait...");
				Task task=new Task(Task.LOADMORE, params);
				MainService.newTask(task);
				MainService.addActivty(thisActivity);				
			}
		});
		
		refreshBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("token", token);
				MainActivity.progress.setVisibility(View.VISIBLE);
				Task task=new Task(Task.GET_WEIBOS, params);
				MainService.newTask(task);
				MainService.addActivty(thisActivity);	
			}
		});

		updateBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Toast.makeText(thisActivity.getApplicationContext(), "Hi，this function is in built, give me more time...", Toast.LENGTH_LONG).show();
			}
		});
		
	}
	/*
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
	*/
	
}
