package com.chinaairlines.component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


public class DeviceDetail {
    
	public String getAppName()
	{
		String AppName = "CIEMP";
	    return AppName;
	}
	
	public String getOsCode() {
		String OsCode = "A";		
		return OsCode;		
	}
	
	public String getOsVersion() {
		String OsVersion = Build.VERSION.RELEASE;
		return OsVersion;		
	}
	
	public String getDevBrand() {
		String DevBrand = Build.MANUFACTURER;
		return DevBrand;		
	}
	
	public String getDevModel() {
		String DevModel = Build.MODEL;
		return DevModel;		
	}

	public String getResolution(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int screenHeight = display.getWidth();
		int screenWidth  = display.getHeight();
		String Resolution = String.valueOf(screenHeight) + "*" + String.valueOf(screenWidth);
		return Resolution;		
	}
	
	public String getOsLang() {
		String OsLang = Locale.getDefault().toString();
		return OsLang;		
	}
	
	public String getAppLang() {
		String OsLang = Locale.getDefault().toString();
		return OsLang;		
	}
	
	public String getAppVersion(Context context) {
		String AppVersion = getAppVersionCode(context);
		return AppVersion;		
	}
	
	public String[] locationServiceInitial(Context context) {
		String[] loc = null;
		String Lng = null;
		String Lat = null;
		Location location;	
		LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	
		if (mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
					|| mLocationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			
//			try {
//				location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//				Lng = String.valueOf(location.getLongitude());
//				Lat = String.valueOf(location.getLatitude());
//			} catch (Exception e) {
//				location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//				Lng = String.valueOf(location.getLongitude());
//				Lat = String.valueOf(location.getLatitude());
//			}
			Lng = String.valueOf("25N08'00");
			Lat = String.valueOf("121E44'00");
			loc = new String[]{Lng,Lat};
		}	
		return loc;	
	
	}
	
	public String getPushToken(Context context) {
		//String PushToken = GCMRegistrar.getRegistrationId(context);
		String PushToken ="TMPTOKEN";
		return PushToken;
	}
	
//	public String getDevId(Context context) {
//		String DevId = new String();	
//		DB db=new DB(context);
//		if (db.getUserInfo(context).length != 0) {
//			DevId = db.getUserInfo(context)[0][26];	
//		}		
//		
//		return DevId;		
//	}

	public String getFirst() {
		String First = "0";
		return First;		
	} 
	
	public String getIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
						String Ip = inetAddress.getHostAddress().toString();
						return Ip;						
					}
				}				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 String getAppVersionCode(Context context) {
		PackageInfo pInfo = null;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String AppVersion = pInfo.versionName;
		return AppVersion;
	}

}
