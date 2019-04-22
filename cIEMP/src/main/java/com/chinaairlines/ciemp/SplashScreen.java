package com.chinaairlines.ciemp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chinaairlines.component.AnalyseInput;
import com.chinaairlines.component.AuthStateManager;
import com.chinaairlines.component.CheckConnection;
import com.chinaairlines.component.Configuration;
import com.chinaairlines.component.DeviceDetail;
import com.chinaairlines.component.Language;
import com.chinaairlines.component.OpenOptionDialog;
import com.chinaairlines.component.Preference;
import com.chinaairlines.component.Variable;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationService;


public class SplashScreen extends Activity {

	private String TAG = "SplashScreen";
	private static int SPLASH_TIME_OUT = 1000;
	private CIEMPApplication application;


	private AuthStateManager mStateManager;
	private AuthorizationService mAuthService;

	private Configuration mConfiguration;
	private CustomTabsSession mSession = null;
	private CustomTabsClient mClient;
	private CustomTabsIntent.Builder intentBuilder;
	private CustomTabsServiceConnection mConnection;

	final static int REQUEST_DESIGERD_PERMISSION = 1;
	String[] permissions = new String[]{
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.READ_EXTERNAL_STORAGE
	};

	List<String> denyPermissionList = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Get personal preference.

		super.onCreate(savedInstanceState);

		mStateManager = AuthStateManager.getInstance(this);
		mConfiguration = Configuration.getInstance(this);
		mAuthService = new AuthorizationService(
				this,
				new AppAuthConfiguration.Builder()
						.setConnectionBuilder(mConfiguration.getConnectionBuilder())
						.build());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);
		application = (CIEMPApplication)getApplication();

		if (getPremission()) {
			doChecks();
		}
	}

	private boolean getPremission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			for (int i = 0; i < permissions.length; i++) {
				if (checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
					denyPermissionList.add(permissions[i]);
				}
			}
			if (denyPermissionList.isEmpty()) {
				return true;
			}else{
				String[] yetpermissions = denyPermissionList.toArray(new String[denyPermissionList.size()]);
				requestPermissions(yetpermissions, REQUEST_DESIGERD_PERMISSION);
				return false;
			}
		}else{
			return true;
		}
	}

	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case REQUEST_DESIGERD_PERMISSION:
				boolean allReanted=true;
				if (grantResults.length > 0) {
					for (int i = 0; i < grantResults.length; i++) {
						if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
							allReanted = false;
							break;
						}
					}
					if (allReanted) {
						doChecks();
					}else{
						new AlertDialog.Builder(SplashScreen.this)
						.setMessage(getString(R.string.ePermission))
						.setPositiveButton(
								android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										finish();
									}
								}
						).show();
					}
				}
				break;
			default:
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void doChecks() {
		initialStorage();
		getDeviceDetail();
		getpreference();
		if (checkExternalStorageSize()) {
			docheckVersion();
		}
	}

	private void docheckVersion(){
		CheckConnection checkConnection=new CheckConnection();
		if (checkConnection.cCheckConnection(SplashScreen.this)) {
			new checkVersion().execute();
		} else {
			OpenOptionDialog openOptionDialog=new OpenOptionDialog(SplashScreen.this);
			openOptionDialog.DialogforConnection(SplashScreen.this, getString(R.string.eConnection));
		}
	}

	private class checkVersion extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// If this version is the latest one, check if need to login or not.
			// Otherwise, need to download the latest one.
			if (result == true) {
				//Intent i = new Intent(SplashScreen.this, LoginActivity.class);
				if (Variable.isDebug) {
					Log.e(TAG, "checkVersion result: " + result);
				}


				signOut();
				finish();
			}else {
				if (application.getUrl() != null) {
					OpenOptionDialog openOptionDialog=new OpenOptionDialog(SplashScreen.this);
					openOptionDialog.DialogforCheckVersion(SplashScreen.this, getString(R.string.eVersion), getString(R.string.bDownLoadCIAppstore), application.getUrl());
				} else {
					OpenOptionDialog openOptionDialog=new OpenOptionDialog(SplashScreen.this);
					openOptionDialog.DialogforConnection(SplashScreen.this, getString(R.string.eConnection));
				}
			}
		}
	}

	private void getpreference() {
		Preference preference=new Preference();
		if(application.getAppLang() != null){
			if (Variable.isDebug) {
				Log.e(TAG,"getpreference null");
			}
			Language language=new Language();
			language.setLanguage(SplashScreen.this, new Locale(application.getAppLang()));
			application.setAppLang(preference.getPrefs(SplashScreen.this, "EMP", "language"));
		} else {
			DeviceDetail deviceDetail=new DeviceDetail();
			if (deviceDetail.getOsLang().equals("zh_TW")) {
				if (Variable.isDebug) {
					Log.e(TAG,"zh_TW");
				}
				//Log.e(TAG,"zh_TW");
				preference.setPrefs(SplashScreen.this, "EMP", "language", "tw");
				Language language=new Language();
				language.setLanguage(SplashScreen.this, Locale.TRADITIONAL_CHINESE);
				application.setAppLang(preference.getPrefs(SplashScreen.this, "EMP", "language"));
			} else {
				if (Variable.isDebug) {
					Log.e(TAG,"en");
				}
				preference.setPrefs(SplashScreen.this, "EMP", "language", "en");
				Language language=new Language();
				language.setLanguage(SplashScreen.this, Locale.ENGLISH);
				application.setAppLang(preference.getPrefs(SplashScreen.this, "EMP", "language"));
			}
		}

		application.setDevId(preference.getPrefs(SplashScreen.this, "EMP", "devId"));
		application.setLoginAuthStatus("I");
		//LoginAuthStatus = "I";
		if (Variable.isDebug) {
			Log.e(TAG,application.getAppLang()+"");
			Log.e(TAG, application.getAppLang() + " / " + application.getDevId());
		}
	}

	//空間最少容量定義
	private boolean checkExternalStorageSize() {
		long available_size = Util.getAvailableExternalMemorySize(); //MB

		long test_size = 20 * 1024 * 1024 ; // 20 MB
		if (Variable.isDebug) {
			Log.e("available_size","available_size: "+available_size+" "+test_size+" "+(available_size < test_size));
		}
		if (available_size < test_size) {

			OpenOptionDialog openOptionDialog=new OpenOptionDialog(SplashScreen.this);
			openOptionDialog.DialogforConnection(SplashScreen.this, getString(R.string.app_storage_not_enough));
			return false;
		}else{
			return true;
		}
	}

	private void initialStorage() {

		File external_storage = getExternalFilesDir(null);

		File internal_storage = getFilesDir();

		if (Variable.isDebug) {
			Log.d("AppService",
					"external path:" + external_storage.getAbsolutePath());

			Log.d("AppService",
					"internal path:" + internal_storage.getAbsolutePath());
		}
		// 設計原則(1)(2)
		File data_root = new File(external_storage, Variable.STORAGE_DATA_ROOT);

		final File license_file = new File(internal_storage, "streams.emp");

		if (!data_root.exists()) {
			data_root.mkdir();
		}

		// if (!data_root.exists() && !license_file.exists()) {
		else if (!data_root.exists() && !license_file.exists()) {
			data_root.mkdir();

			InputStream license_is = getResources().openRawResource(
					Variable.LICENSE_FILE_BUNDLE);

			try {
				license_file.createNewFile();

				OutputStream license_os = new FileOutputStream(license_file);

				byte[] buffer = new byte[1024];

				int len = 0;

				while ((len = license_is.read(buffer)) > 0) {
					license_os.write(buffer, 0, len);
				}

				license_is.close();

				license_os.close();

			} catch (Exception e) {
				e.printStackTrace();

				license_file.delete();
			}
		}
		// 設計原則(2)
		// 測試異常
		else if (data_root.exists() 	&& license_file.exists()) {
			OpenOptionDialog openOptionDialog = new OpenOptionDialog(SplashScreen.this);
			openOptionDialog.DialogforConnection(SplashScreen.this, getString(R.string.app_storage_error));
		}

		File nomedia_file = new File(external_storage, ".nomedia");

		if (!nomedia_file.exists()) {
			try {
				nomedia_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
		return true;
	}
	
	private void getDeviceDetail() {
		DeviceDetail deviceDetail=new DeviceDetail();
		application.setOsCode(deviceDetail.getOsCode());
		application.setOsVersion(deviceDetail.getOsVersion());
		application.setDevBrand(deviceDetail.getDevBrand());
		application.setDevModel(deviceDetail.getDevModel());
		application.setResolution(deviceDetail.getResolution(this));
		application.setOsLang(deviceDetail.getOsLang());
		application.setAppVersion(deviceDetail.getAppVersion(this));
		try {
			application.setLng(deviceDetail.locationServiceInitial(this)[0]);
			application.setLat(deviceDetail.locationServiceInitial(this)[1]);
		} catch (Exception e) {
			application.setLng("");
			application.setLat("");
		}
		
		application.setPushToken(deviceDetail.getPushToken(this));
		application.setFirst(deviceDetail.getFirst());
		application.setIp(deviceDetail.getIp());
	}

	@MainThread
	private void signOut() {
		AuthState currentState = mStateManager.getCurrent();
		AuthState clearedState =
				new AuthState(currentState.getAuthorizationServiceConfiguration());
		if (currentState.getLastRegistrationResponse() != null) {
			clearedState.update(currentState.getLastRegistrationResponse());
		}
		mStateManager.replace(clearedState);

		ExecutorService mExecutor = Executors.newSingleThreadExecutor();

		mExecutor.execute(() -> {
			if (Variable.isDebug) {
				Log.e(TAG, "Warming up browser instance for auth request");
			}

			mClient = mAuthService.getCustomTabManager().getClient();
			mSession = mClient.newSession(new CustomTabsCallback() {
				@Override
				public void onNavigationEvent(int navigationEvent, Bundle extras) {
					Log.i("CustomTabsCallback", "onNavigationEvent: Code = " + navigationEvent);
					if (navigationEvent == 2) {
						Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
						startActivity(intent);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						finish();
					}
				}
			});

			if (Variable.isDebug) {
				Log.e(TAG, "AAA");
			}

			intentBuilder = new CustomTabsIntent.Builder(mSession);
			CustomTabsIntent customTabsIntent = intentBuilder.build();
			customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			customTabsIntent.launchUrl(this, Uri.parse(Variable.OIDCSignOutURL));

		});
	}


}

