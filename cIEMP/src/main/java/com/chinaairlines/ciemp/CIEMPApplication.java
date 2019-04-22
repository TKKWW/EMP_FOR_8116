package com.chinaairlines.ciemp;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.chinaairlines.component.AppLanguageUtils;
import com.chinaairlines.component.Variable;

public class CIEMPApplication extends Application {
	private String AppName;
	private String OsCode;
	private String OsVersion;
	private String DevBrand;
	private String DevModel;
	private String Resolution;
	private String OsLang;
	private String AppLang;
	private String AppVersion;
	private String Lng;
	private String Lat;
	private String PushToken;
	private String DevId = "";
	private String First;
	private String Ip;
	private String UserId;
	private String Pwd;
	private String Token;
	private String Url;
	private String NotifiInfo;
	private String LoginAuthStatus;
	private String DownloadUrl;
	private boolean isFromNoti = false;
	private String NewMsgId;
	private String NotificationUrl;
	private boolean isSettingFgmOnMainPage = false;
	private boolean isVPN = false;
	private String ErrMsg;
	private String InterNet;


	public String getAppName()
	{
		return AppName;
	}
	
	public void setAppName(String appName)
	{
		AppName = appName;		
	}
	/**
	 * @return the osCode
	 */
	public String getOsCode() {
		return OsCode;
	}

	/**
	 * @param osCode the osCode to set
	 */
	public void setOsCode(String osCode) {
		OsCode = osCode;
	}

	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return OsVersion;
	}

	/**
	 * @param osVersion the osVersion to set
	 */
	public void setOsVersion(String osVersion) {
		OsVersion = osVersion;
	}

	/**
	 * @return the devBrand
	 */
	public String getDevBrand() {
		return DevBrand;
	}

	/**
	 * @param devBrand the devBrand to set
	 */
	public void setDevBrand(String devBrand) {
		DevBrand = devBrand;
	}

	/**
	 * @return the devModel
	 */
	public String getDevModel() {
		return DevModel;
	}

	/**
	 * @param devModel the devModel to set
	 */
	public void setDevModel(String devModel) {
		DevModel = devModel;
	}

	/**
	 * @return the resolution
	 */
	public String getResolution() {
		return Resolution;
	}

	/**
	 * @param resolution the resolution to set
	 */
	public void setResolution(String resolution) {
		Resolution = resolution;
	}

	/**
	 * @return the osLang
	 */
	public String getOsLang() {
		return OsLang;
	}

	/**
	 * @param osLang the osLang to set
	 */
	public void setOsLang(String osLang) {
		OsLang = osLang;
	}

	/**
	 * @return the appLang
	 */
	public String getAppLang() {
		return AppLang;
	}

	/**
	 * @param appLang the appLang to set
	 */
	public void setAppLang(String appLang) {
		AppLang = appLang;
	}

	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return AppVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		AppVersion = appVersion;
	}

	/**
	 * @return the lng
	 */
	public String getLng() {
		return Lng;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng(String lng) {
		Lng = lng;
	}

	/**
	 * @return the lat
	 */
	public String getLat() {
		return Lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(String lat) {
		Lat = lat;
	}

	/**
	 * @return the pushToken
	 */
	public String getPushToken() {
		return PushToken;
	}

	/**
	 * @param pushToken the pushToken to set
	 */
	public void setPushToken(String pushToken) {
		PushToken = pushToken;
	}

	/**
	 * @return the devId
	 */
	public String getDevId() {
		return DevId;
	}

	/**
	 * @param devId the devId to set
	 */
	public void setDevId(String devId) {
		DevId = devId;
	}

	/**
	 * @return the first
	 */
	public String getFirst() {
		return First;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(String first) {
		First = first;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return Ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		Ip = ip;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return UserId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		UserId = userId;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return Pwd;
	}

	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		Pwd = pwd;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return Token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		Token = token;
	}
	
	/**
	 * @return the token
	 */
	public String getNotifiInfo() {
		return NotifiInfo;
	}

	/**
	 * @param token the token to set
	 */
	public void setNotifiInfo(String notifiInfo) {
		NotifiInfo = notifiInfo;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return Url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		Url = url;
	}

//	/**
//	 * @return the downloadUrl
//	 */
//	public String getDownloadUrl() {
//		return DownloadUrl;
//	}
//
//	/**
//	 * @param downloadUrl the downloadUrl to set
//	 */
//	public void setDownloadUrl(String downloadUrl) {
//		DownloadUrl = downloadUrl;
//	}
	
	/**
	 * @return the isFromNoti
	 */
	public boolean getIsFromNoti() {
		return isFromNoti;
	}

	/**
	 * @param isFromNoti the isFromNoti to set
	 */
	public void setIsFromNoti(boolean isFromNoti) {
		this.isFromNoti = isFromNoti;
	}
	
	/**
	 * @return the newMsgId
	 */
	public String getNewMsgId() {
		return NewMsgId;
	}

	/**
	 * @param newMsgId the newMsgId to set
	 */
	public void setNewMsgId(String newMsgId) {
		NewMsgId = newMsgId;
	}
	
	/**
	 * @return the notificationUrl
	 */
	public String getNotificationUrl() {
		return NotificationUrl;
	}

	/**
	 * @param notificationUrl the notificationUrl to set
	 */
	public void setNotificationUrl(String notificationUrl) {
		NotificationUrl = notificationUrl;
	}
	
	/**
	 * @return the loginAuthStatus
	 */
	public String getLoginAuthStatus() {
		return LoginAuthStatus;
	}

	/**
	 * @param loginAuthStatus the loginAuthStatus to set
	 */
	public void setLoginAuthStatus(String loginAuthStatus) {
		LoginAuthStatus = loginAuthStatus;
	}	

	/**
	 * @return the isSettingFgmOnMainPage
	 */
	public boolean getIsSettingFgmOnMainPage() {
		return isSettingFgmOnMainPage;
	}

	/**
	 * @param isSettingFgmOnMainPage the isSettingFgmOnMainPage to set
	 */
	public void setIsSettingFgmOnMainPage(boolean isSettingFgmOnMainPage) {
		this.isSettingFgmOnMainPage = isSettingFgmOnMainPage;
	}
	
	/**
	 * @return the isVPN
	 */
	public String getInterNet() {
		return InterNet;
	}

	/**
	 * @param isVPN the isVPN to set
	 */
	public void setInterNet(String interNet) {
		this.InterNet = interNet;
	}

	public String getErrMsg() {
		return ErrMsg;
	}

	/**
	 * @param loginAuthStatus the loginAuthStatus to set
	 */
	public void setErrMsg(String errMsg) {
		ErrMsg = errMsg;
	}
	public boolean getIsVPN() {
		return isVPN;
	}

	/**
	 * @param isVPN the isVPN to set
	 */
	public void setIsVPN(boolean isVPN) {
		this.isVPN = isVPN;
	}
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */


	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}



	/* (non-Javadoc)
	 * @see android.app.Application#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onTrimMemory(int)
	 */
	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}

	/* (non-Javadoc)
	 * @see android.app.Application#registerComponentCallbacks(android.content.ComponentCallbacks)
	 */
	@Override
	public void registerComponentCallbacks(ComponentCallbacks callback) {
		// TODO Auto-generated method stub
		super.registerComponentCallbacks(callback);
	}

	/* (non-Javadoc)
	 * @see android.app.Application#unregisterComponentCallbacks(android.content.ComponentCallbacks)
	 */
	@Override
	public void unregisterComponentCallbacks(ComponentCallbacks callback) {
		// TODO Auto-generated method stub
		super.unregisterComponentCallbacks(callback);
	}

	/* (non-Javadoc)
	 * @see android.app.Application#registerActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks)
	 */
	@Override
	public void registerActivityLifecycleCallbacks(
			ActivityLifecycleCallbacks callback) {
		// TODO Auto-generated method stub
		super.registerActivityLifecycleCallbacks(callback);
	}

	/* (non-Javadoc)
	 * @see android.app.Application#unregisterActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks)
	 */
	@Override
	public void unregisterActivityLifecycleCallbacks(
			ActivityLifecycleCallbacks callback) {
		// TODO Auto-generated method stub
		super.unregisterActivityLifecycleCallbacks(callback);
	}

	private static CIEMPApplication sInstances;
	private static Context sContext;

	public static CIEMPApplication getInstances() {
		return sInstances;
	}

	public static Context getContext() {
		return sContext;
	}


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(AppLanguageUtils.attachBaseContext(base, getAppLanguage(base)));
	}


	private void onLanguageChange() {
		AppLanguageUtils.changeAppLanguage(this, AppLanguageUtils.getSupportLanguage(getAppLanguage(this)));
	}

	private String getAppLanguage(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(context.getString(R.string.bSetting), Variable.ENGLISH);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sInstances = this;
		sContext = this;
		onLanguageChange();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		onLanguageChange();
	}
}
