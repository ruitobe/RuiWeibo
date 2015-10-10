package com.rui.ruiweibo.ui;

import java.util.ArrayList;

import com.rui.ruiweibo.adapter.WeiboAdapter;
import com.rui.ruiweibo.logic.MainService;
import com.rui.ruiweibo.model.Task;
import com.rui.ruiweibo.model.Weibo;
import com.rui.ruiweibo.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements RWeiboActivity {
	
	private FragmentTabHost rTabHost;
	
	private static final String HOME_TAB="home"; 
	private static final String AT_TAB="at"; 
	private static final String MSG_TAB="msg"; 
	private static final String MORE_TAB="more";
	
	public static ArrayList<Weibo> weibos;
	public static WeiboAdapter adapter;
	public static ListView weiboList;
	public static LinearLayout progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(com.rui.ruiweibo.R.layout.activity_main);
		
		rTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		rTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		
		rTabHost.addTab(rTabHost.newTabSpec(HOME_TAB).setIndicator(HOME_TAB), Home.class, null);
		rTabHost.addTab(rTabHost.newTabSpec(AT_TAB).setIndicator(AT_TAB), At.class, null);
		rTabHost.addTab(rTabHost.newTabSpec(MSG_TAB).setIndicator(MSG_TAB), Msg.class, null);
		rTabHost.addTab(rTabHost.newTabSpec(MORE_TAB).setIndicator(MORE_TAB), More.class, null);
		
		// set tabs invisible, use radio buttons only
		rTabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
		rTabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
		rTabHost.getTabWidget().getChildAt(2).setVisibility(View.GONE);
		rTabHost.getTabWidget().getChildAt(3).setVisibility(View.GONE);
		
		// set home fragment as default one
		rTabHost.setCurrentTabByTag(HOME_TAB);
		
		RadioGroup radioGroup =  (RadioGroup)this.findViewById(R.id.main_radio);
	    final RadioButton rb = (RadioButton)this.findViewById(R.id.rb_home);
	    rb.setBackgroundResource(R.drawable.tabhost_press);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				rb.setBackgroundResource(R.drawable.tabhost_bg_selector);
				
				switch (checkedId) {
				
					case R.id.rb_home:
						rTabHost.setCurrentTabByTag(HOME_TAB);
						break;
						
					case R.id.rb_at:
						rTabHost.setCurrentTabByTag(AT_TAB);
						break;
						
					case R.id.rb_mess:
						rTabHost.setCurrentTabByTag(MSG_TAB);
						break;
						
					case R.id.rb_more:
						rTabHost.setCurrentTabByTag(MORE_TAB);
						break;
						
					default:
						break;
				}
			}
		});
		
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void refresh(int taskID, Object... objects) {
		// TODO Auto-generated method stub
		// update data
		switch (taskID) {
		
		case Task.GET_WEIBOS:
			
			weibos = (ArrayList<Weibo>)objects[0];
			adapter = new WeiboAdapter(MainActivity.this, weibos);
			weiboList.setAdapter(adapter);	
					
		break;
				
		case Task.LOADMORE:
			weibos = (ArrayList<Weibo>)objects[0];
			for(int i = 1; i<weibos.size(); i++)
				adapter.addItem(weibos.get(i));
			adapter.notifyDataSetChanged(); 
		
		}
		
		progress.setVisibility(View.GONE);
		MainService.removeActivty(MainActivity.this);
		
	}
	
	/*
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}

	 */
}
