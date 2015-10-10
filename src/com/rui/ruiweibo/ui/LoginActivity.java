package com.rui.ruiweibo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rui.ruiweibo.R;
import com.rui.ruiweibo.model.Task;
import com.rui.ruiweibo.model.LoginUserInfo;
import com.rui.ruiweibo.logic.MainService;
import com.rui.ruiweibo.util.GetLoginUserInfoUtil;
import com.rui.ruiweibo.util.SharePreferencesUtil;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import android.widget.AdapterView.OnItemSelectedListener;

public class LoginActivity extends Activity implements RWeiboActivity{
	
	private Button addAccountBtn, loginBtn;
	private Spinner userName;
    private ImageView iv_HeadIcon;
    @SuppressWarnings("rawtypes")
	private ArrayAdapter adapter;
    
    private List<LoginUserInfo> loginUsers;

    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);

        // If user info, go to MainActivity
        if(null != SharePreferencesUtil.getLoginUser(this)) {
        	
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            
        }
        
        // init login page
        init();
        
        /*
		 * start task：GET_USERINFO_IN_LOGIN
		 * get authenticated user info from database
		 */
        Task task_getuser = new Task(Task.GET_USERINFO_IN_LOGIN, null);
		MainService.newTask(task_getuser);
		MainService.addActivty(LoginActivity.this);
        
    }
	 
    public void init() {
    	
    	userName = (Spinner)findViewById(R.id.spinner_user_list);
    	iv_HeadIcon = (ImageView) this.findViewById(R.id.image_head);
		
    	// add new account, listener
    	addAccountBtn = (Button) findViewById(R.id.btn_add_account); 	
    	addAccountBtn.setOnClickListener(new OnClickListener() {
        	
            public void onClick(View v) {
            	
            	MainService.removeActivty(LoginActivity.this);
            	
                Intent intent=new Intent(LoginActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    	
    	// login, listener
    	loginBtn = (Button) findViewById(R.id.btn_login);	
		loginBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				LoginUserInfo loginUser = loginUsers.get(userName.getSelectedItemPosition());
				
				/*
				 * start new task: WEIBO_LOGIN
				 */
				
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("loginUser", loginUser);
				Task task = new Task(Task.WEIBO_LOGIN, params);
				MainService.newTask(task);
				MainService.addActivty(LoginActivity.this);
			}
		});
		
		// spinner, to select different user and update the image view, listener
		userName.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				// TODO Auto-generated method stub
				iv_HeadIcon.setImageBitmap(GetLoginUserInfoUtil.scaleImg(loginUsers.get(position).getUserIcon() , 200, 200));
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void refresh(int taskID, Object... objects) {
		
		// TODO Auto-generated method stub
				switch(taskID) {
					//after getting authenticated user info:
				
					case Task.GET_USERINFO_IN_LOGIN:
						
						loginUsers = (List<LoginUserInfo>)objects[0];
						// if user
						try {
							
							if(loginUsers != null) {
								
								List<String> list = new ArrayList<String>();
								for( LoginUserInfo user:loginUsers) {
									
									list.add(user.getUserName());
								}
								
								adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
								adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);						
								adapter.notifyDataSetChanged();
								userName.setAdapter(adapter);
								
								iv_HeadIcon.setImageBitmap(GetLoginUserInfoUtil.scaleImg(loginUsers.get(0).getUserIcon() , 200, 200));
							}
							// if no user, go to auth
							else {
								
								Intent intent=new Intent(LoginActivity.this, AuthActivity.class);
				                startActivity(intent);
				                finish();
							}
							
							MainService.removeActivty(LoginActivity.this);
						}
						catch (WeiboException e) {
							
							e.printStackTrace();						
						}

						break;
						
					// finish login
					case Task.WEIBO_LOGIN:
						
						MainService.removeActivty(LoginActivity.this);
						Intent intent =new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						break;
						
					default: break;
						
				}
			}

	/*
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
	*/
	
}
