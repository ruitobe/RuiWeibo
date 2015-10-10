package com.rui.ruiweibo.ui;

import com.rui.ruiweibo.R;
import com.rui.ruiweibo.logic.MainService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;


public class LogoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        // cancel title:
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // cancel status bar:
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_logo);
        // start main service
        Intent intent=new Intent(LogoActivity.this,MainService.class);
		startService(intent);
		
        ImageView imageLogo = (ImageView) this.findViewById(R.id.img_logo);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(3000);
        
        animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        imageLogo.setAnimation(animation);

    }

}