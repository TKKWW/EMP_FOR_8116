package com.chinaairlines.component;

import com.chinaairlines.ciemp.R;

import android.app.Activity;
import android.content.Context;

public class ErrorMsg {

	public void ShowMsg(final Context context, final String errCode, final String errMsg) {
		
		final String title;
		final Activity activity = (Activity) context;
		OpenOptionDialog openOptionDialog=new OpenOptionDialog(activity);
		// checkVersion
		if (errCode.substring(0, 1).equals("1")) {
			title = activity.getString(R.string.eReport);
			activity.runOnUiThread(new Runnable() {					
				@Override
				public void run() {

					openOptionDialog.Positive(activity, title, errMsg);
			}
			});
		// loginAuth	
		} else if (errCode.substring(0, 1).equals("2")) {

			title = activity.getString(R.string.eLogin);
			
			// 登入帳號不存在
			if (errCode.equals("2001")) {
				activity.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						openOptionDialog.Positive(activity, title, errMsg);						
					}
				});
				
			// 登入密碼錯誤
			} else if (errCode.equals("2002")) {

				activity.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						openOptionDialog.Positive(activity, title, errMsg);
					}
				});
				
			// 使用者帳號鎖定
			} else if (errCode.equals("2004")) {
				activity.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						openOptionDialog.Positive(activity, title, errMsg);
					}
				});
				
			// 使用者登入SESSION輸入固定密碼錯誤
			} else if (errCode.equals("2008")) {
				activity.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						openOptionDialog.Positive(activity, title, errMsg);
					}
				});				
			
			// 設備鎖定
			} else if (errCode.equals("2101")) {
				activity.runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						openOptionDialog.Positive(activity, title, errMsg);
					}
				});				
				
			}			
			
		} 		
		
	}
	
}
