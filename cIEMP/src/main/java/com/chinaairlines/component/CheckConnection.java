package com.chinaairlines.component;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {

	public boolean cCheckConnection(Context context) {
		boolean isConnection = false;
		
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivityManager != null) {
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
				isConnection = true;
			} else {
				isConnection = false;
			}
		} else {
			isConnection = false;
		}
					
		return isConnection;
	}
	
	
}
