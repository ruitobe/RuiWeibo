package com.rui.ruiweibo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.rui.ruiweibo.model.WeiboItemUser;
import com.rui.ruiweibo.model.Weibo;

public class WeiboUtil {

	//Get weibo list
	
	public ArrayList<Weibo> getWeiboList(String token, long maxID) {
		
		ArrayList<Weibo> weibos = new ArrayList<Weibo>();
		String url = "https://api.weibo.com/2/statuses/home_timeline.json";
		String params = "access_token=" + token + "&max_id=" + maxID;

		String result = HttpRequest(1, url, params);
		
		try {
			
			JSONObject json_res = new JSONObject(result);
			
			JSONArray json_weibos = json_res.getJSONArray("statuses");
			
			for(int j = 0; j < json_weibos.length(); j++) {
				
				Weibo weibo = new Weibo();
				
				JSONObject json_weibo = json_weibos.getJSONObject(j);
				weibo.setComments_count(json_weibo.getInt("comments_count"));
				weibo.setContent(json_weibo.getString("text"));
				weibo.setFrom(json_weibo.getString("source"));
				weibo.setReposts_count(json_weibo.getInt("reposts_count"));
				weibo.setTime(json_weibo.getString("created_at"));
				weibo.setWid(json_weibo.getLong("id"));
				
				WeiboItemUser user = new WeiboItemUser();
				
				JSONObject json_user = json_weibo.getJSONObject("user");
				user.setUid(json_user.getLong("id"));
				user.setName(json_user.getString("screen_name"));
				user.setProfile_image_url(json_user.getString("profile_image_url"));
				user.setIsv(json_user.getBoolean("verified"));
				
				weibo.setUser(user);	
				
				if(json_weibo.getJSONArray("pic_urls").length() != 0) {
					
					JSONArray picUrlsArr = json_weibo.getJSONArray("pic_urls");
					
					ArrayList<String> list = new ArrayList<String>();
					ArrayList<String> bList = new ArrayList<String>();
					String tempStr = "";
					for (int i = 0; i < picUrlsArr.length(); i++) {
						tempStr = picUrlsArr.getJSONObject(i).getString("thumbnail_pic");
						list.add(tempStr);
						tempStr = tempStr.replaceAll("thumbnail", "bmiddle");
						bList.add(tempStr);
					}
					
					weibo.setPicUrls(list);
					weibo.setBmiddlePicUrls(bList);
					
				}

				else {
					
					ArrayList<String> list = new ArrayList<String>();
					ArrayList<String> bList = new ArrayList<String>();
					list.add("");
					bList.add("");
					weibo.setPicUrls(list);
					weibo.setBmiddlePicUrls(bList);

				}
					
				//weibo.setBmiddle_pic("");
				
				if(!json_weibo.isNull("retweeted_status")) {
					
					Weibo rWeibo = new Weibo();
					
					JSONObject json_rWeibo = json_weibo.getJSONObject("retweeted_status");
					rWeibo.setComments_count(json_rWeibo.getInt("comments_count"));
					rWeibo.setContent(json_rWeibo.getString("text"));
					rWeibo.setFrom(json_rWeibo.getString("source"));
					rWeibo.setReposts_count(json_rWeibo.getInt("reposts_count"));
					rWeibo.setTime(json_rWeibo.getString("created_at"));
					rWeibo.setWid(json_rWeibo.getLong("id"));
					
					WeiboItemUser rUser = new WeiboItemUser();
					
					JSONObject json_rUser = json_rWeibo.getJSONObject("user");
					rUser.setUid(json_rUser.getLong("id"));
					rUser.setName(json_rUser.getString("screen_name"));
					rUser.setProfile_image_url(json_rUser.getString("profile_image_url"));
					rUser.setIsv(json_rUser.getBoolean("verified"));
					rWeibo.setUser(rUser);
					
					if(json_rWeibo.getJSONArray("pic_urls").length() != 0) {
						
						JSONArray rPicUrlsArr = json_rWeibo.getJSONArray("pic_urls");
						
						ArrayList<String> rList = new ArrayList<String>();
						ArrayList<String> bRList = new ArrayList<String>();
						String rTempStr = "";
						for (int i = 0; i < rPicUrlsArr.length(); i++) {
							rTempStr = rPicUrlsArr.getJSONObject(i).getString("thumbnail_pic");
							rList.add(rTempStr);
							rTempStr = rTempStr.replaceAll("thumbnail", "bmiddle");
							bRList.add(rTempStr);
						}
						
						rWeibo.setPicUrls(rList);
						rWeibo.setBmiddlePicUrls(bRList);
						
					}
					
					else {
						
						ArrayList<String> rList = new ArrayList<String>();
						ArrayList<String> bRList = new ArrayList<String>();
						rList.add("");
						bRList.add("");
						rWeibo.setPicUrls(rList);
						rWeibo.setBmiddlePicUrls(bRList);
						
					}
					
					weibo.setWeibo(rWeibo);
				}
				
				else
					// no retweeted weibo
					weibo.setWeibo(null);
				
				weibos.add(weibo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return weibos;
	}
	
	//HTTP
	public static String HttpRequest(int way, String url, String params) {
		String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        
        try {
        	
            String urlNameString = url + "?" + params;
            URL realUrl = new URL(urlNameString);
            HttpURLConnection connection;
            //URLConnection connection;
            // POST:
            if(way == 0) {

            	
            	connection = (HttpURLConnection)realUrl.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                out = new PrintWriter(connection.getOutputStream());
                out.print(params);
                out.flush();   
                
            	
            }
            // GET:
            else {
            	
            	connection = (HttpURLConnection) realUrl.openConnection();
                int responseCode = connection.getResponseCode();

                if (responseCode >= 400 && responseCode <= 499) {
                	
                	throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
                	
                }
            }
                
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = "";
            
            while ((line = in.readLine()) != null) {
            	
            	sb.append(line);
            }
            
            result = sb.toString();
            
        } 
        catch (Exception e) {
        	
            System.out.println("Send GET request abnormal!" + e);
            e.printStackTrace();
        }

        finally {
        	
            try {
                if (in != null) {
                    in.close();
                }
            } 
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        return result;
	}
	
	public int update(String token, String text) {
		return 0;
	}

	public int guanzhuMe(String token) {
		return 0;
	}

	
	public static void sop (Object obj) {
		
		System.out.println(obj.toString());
	}
	
}
