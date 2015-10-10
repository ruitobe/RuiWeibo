package com.rui.ruiweibo.ui;

public interface RWeiboActivity {

		public void init();
		
		// update UI
		public void refresh(int taskID, Object...objects);
}
