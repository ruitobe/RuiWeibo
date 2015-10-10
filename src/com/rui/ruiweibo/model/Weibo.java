package com.rui.ruiweibo.model;

import java.util.ArrayList;

public class Weibo {

	public long wid;
	public String content;
	public String from;
	public String time;
	public ArrayList<String> picUrls;
	public ArrayList<String> bmiddlePicUrls;
	public int reposts_count;
	public int comments_count;
	public Weibo rWeibo;
	public WeiboItemUser user;
	
	public long getWid() {
		
		return wid;
	}
	public void setWid(long wid) {
		
		this.wid = wid;
	}
	
	public String getContent() {
		
		return content;
	}
	
	public void setContent(String content) {
		
		this.content = content;
	}
	
	public String getFrom() {
		
		return from;
	}
	
	public void setFrom(String from) {
		
		this.from = from;
	}
	
	public String getTime() {
		
		return time;
	}
	
	public void setTime(String time) {
		
		this.time = time;
	}
	
	public ArrayList<String> getPicUrls() {
		
		return picUrls;
	}
	
	public void setPicUrls(ArrayList<String> picUrls) {
		
		this.picUrls = picUrls;
	}
	
	public ArrayList<String> getBmiddlePicUrls() {
		
		return bmiddlePicUrls;
	}
	
	public void setBmiddlePicUrls(ArrayList<String> bmilldePicUrls) {
		
		this.bmiddlePicUrls = bmilldePicUrls;
	}
	
	public int getReposts_count() {
		
		return reposts_count;
	}
	
	public void setReposts_count(int reposts_count) {
		
		this.reposts_count = reposts_count;
	}
	
	public int getComments_count() {
		
		return comments_count;
	}
	
	public void setComments_count(int comments_count) {
		
		this.comments_count = comments_count;
	}
	
	public Weibo getWeibo() {
		
		return rWeibo;
	}
	
	public void setWeibo(Weibo rWeibo) {
		
		this.rWeibo = rWeibo;
	}
	
	public WeiboItemUser getUser() {
		
		return user;
	}
	
	public void setUser(WeiboItemUser user) {
		
		this.user = user;
	}
	
}
