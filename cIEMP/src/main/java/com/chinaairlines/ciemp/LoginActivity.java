package com.chinaairlines.ciemp;

import com.chinaairlines.component.AuthStateManager;
import com.chinaairlines.component.BrowserSelectionAdapter;
import com.chinaairlines.component.BrowserSelectionAdapter.*;
import com.chinaairlines.component.Configuration;
import com.chinaairlines.component.Variable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ClientSecretBasic;
import net.openid.appauth.RegistrationRequest;
import net.openid.appauth.RegistrationResponse;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.browser.AnyBrowserMatcher;
import net.openid.appauth.browser.BrowserMatcher;
import net.openid.appauth.browser.ExactBrowserMatcher;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private static final int RC_AUTH = 100;
	private Button btnLogin;
	private AuthorizationService mAuthService;
	private AuthStateManager mAuthStateManager;
	private Configuration mConfiguration;

	private final AtomicReference<String> mClientId = new AtomicReference<>();
	private final AtomicReference<AuthorizationRequest> mAuthRequest = new AtomicReference<>();
	private final AtomicReference<CustomTabsIntent> mAuthIntent = new AtomicReference<>();
	private CountDownLatch mAuthIntentLatch = new CountDownLatch(1);
	private BrowserMatcher mBrowserMatcher = AnyBrowserMatcher.INSTANCE;

	private ExecutorService mExecutor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mExecutor = Executors.newSingleThreadExecutor();
		mAuthStateManager = AuthStateManager.getInstance(this);
		mConfiguration = Configuration.getInstance(this);
		setContentView(R.layout.activity_login);

		if (Variable.isDebug) {
			Log.e(TAG, "User is already authenticated or not: "+mAuthStateManager.getCurrent().isAuthorized());
			Log.e(TAG, "mConfiguration.hasConfigurationChanged(): "+mConfiguration.hasConfigurationChanged());
		}

		initViews();

		if (!mConfiguration.isValid()) {
			if (Variable.isDebug) {
				Log.e(TAG, "!mConfiguration.isValid()");
			}
			//displayError(mConfiguration.getConfigurationError(), false);
			return;
		}

		if (mConfiguration.hasConfigurationChanged()) {
			// discard any existing authorization state due to the change of configuration
			if (Variable.isDebug) {
				Log.e(TAG, "Configuration change detected, discarding old state");
			}
			mAuthStateManager.replace(new AuthState());
			mConfiguration.acceptConfiguration();
		}

		configureBrowserSelector();
		mExecutor.submit(this::initializeAppAuth);
	}

	private String getLoginHint() {
		return "ttkkwu";
	}

	private void initViews() {
		btnLogin = (Button) findViewById(R.id.bLogin);
		btnLogin.setOnClickListener(
				(View view) -> startAuth()
		);

//		etUserId = (EditText) findViewById(R.id.etUserId);
//		etPwd = (EditText) findViewById(R.id.etPwd);
//		tvVersion = (TextView) findViewById(R.id.tvVersion);
//		tvVersion.setText("Version " + SplashScreen.AppVersion);
//		tvVersion.setText(application.getAppVersion());
//		Preference preference=new Preference();
//		if (preference.getPrefs(activity, "EMP", "userId") != null) {
//			etUserId.setText(preference.getPrefs(activity, "EMP", "userId"));
//		}
	}

	private void initializeAppAuth() {
		if (Variable.isDebug) {
			Log.e(TAG, "initializeAppAuth(): Initializing AppAuth");
		}
		recreateAuthorizationService();

		if (mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration() != null) {
			// configuration is already created, skip to client initialization
			if (Variable.isDebug) {
				Log.e(TAG, "initializeAppAuth(): auth config already established");
			}

			initializeClient();
			return;
		}

		// if we are not using discovery, build the authorization service configuration directly
		// from the static configuration values.
		if (mConfiguration.getDiscoveryUri() == null) {
			if (Variable.isDebug) {
				Log.e(TAG, "initializeAppAuth(): Creating auth config from res/raw/auth_config.json");
			}

			AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(
					mConfiguration.getAuthEndpointUri(),
					mConfiguration.getTokenEndpointUri(),
					mConfiguration.getRegistrationEndpointUri());

			mAuthStateManager.replace(new AuthState(config));
			initializeClient();
			return;
		}

		// WrongThread inference is incorrect for lambdas
		// noinspection WrongThread
		//runOnUiThread(() -> displayLoading("Retrieving discovery document"));
		if (Variable.isDebug) {
			Log.e(TAG, "Retrieving OpenID discovery doc");
		}

		AuthorizationServiceConfiguration.fetchFromUrl(
				mConfiguration.getDiscoveryUri(),
				this::handleConfigurationRetrievalResult,
				mConfiguration.getConnectionBuilder());
	}
	@MainThread
	private void handleConfigurationRetrievalResult(
			AuthorizationServiceConfiguration config,
			AuthorizationException ex) {
		if (config == null) {
			if (Variable.isDebug) {
				Log.e(TAG, "Failed to retrieve discovery document", ex);
			}
			//displayError("Failed to retrieve discovery document: " + ex.getMessage(), true);
			return;
		}
		if (Variable.isDebug) {
			Log.e(TAG, "Discovery document retrieved");
		}

		mAuthStateManager.replace(new AuthState(config));
		mExecutor.submit(this::initializeClient);
	}

    private void configureBrowserSelector() {
        Spinner spinner = (Spinner) findViewById(R.id.browser_selector);
        final BrowserSelectionAdapter adapter = new BrowserSelectionAdapter(this);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BrowserInfo info = adapter.getItem(position);

                if (info == null) {
                    mBrowserMatcher = AnyBrowserMatcher.INSTANCE;
                    return;
                } else {
                    mBrowserMatcher = new ExactBrowserMatcher(info.mDescriptor);
                }

                recreateAuthorizationService();
                createAuthRequest(getLoginHint());
                warmUpBrowser();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBrowserMatcher = AnyBrowserMatcher.INSTANCE;
            }
        });
    }


    /**
	 * Initiates a dynamic registration request if a client ID is not provided by the static
	 * configuration.
	 */
	@WorkerThread
	private void initializeClient() {
		if (mConfiguration.getClientId() != null) {
			//Log.e(TAG, "Using static client ID: " + mConfiguration.getClientId());
			// use a statically configured client ID
			if (Variable.isDebug) {
				Log.e(TAG, " initializeClient(): mConfiguration.getClientId() != null: " + (mConfiguration.getClientId() != null));
			}
			mClientId.set(mConfiguration.getClientId());
			runOnUiThread(this::initializeAuthRequest);
			return;
		}

		RegistrationResponse lastResponse =
				mAuthStateManager.getCurrent().getLastRegistrationResponse();
		if (lastResponse != null) {
			//Log.e(TAG, "Using dynamic client ID: " + lastResponse.clientId);
			if (Variable.isDebug) {
				Log.e(TAG, " initializeClient(): lastResponse != null: " + (lastResponse != null));
			}
			// already dynamically registered a client ID
			mClientId.set(lastResponse.clientId);
			runOnUiThread(this::initializeAuthRequest);
			return;
		}

		// WrongThread inference is incorrect for lambdas
		// noinspection WrongThread
		//runOnUiThread(() -> displayLoading("Dynamically registering client"));
		//Log.e(TAG, "Dynamically registering client");

		RegistrationRequest registrationRequest = new RegistrationRequest.Builder(
				mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration(),
				Collections.singletonList(mConfiguration.getRedirectUri()))
				.setTokenEndpointAuthenticationMethod(ClientSecretBasic.NAME)
				.build();

		mAuthService.performRegistrationRequest(
				registrationRequest,
				this::handleRegistrationResponse);
	}



	@MainThread
	private void handleRegistrationResponse(
			RegistrationResponse response,
			AuthorizationException ex) {
		mAuthStateManager.updateAfterRegistration(response, ex);
		if (response == null) {
			//Log.e(TAG, "Failed to dynamically register client", ex);
			//displayErrorLater("Failed to register client: " + ex.getMessage(), true);
			return;
		}

		//Log.e(TAG, "Dynamically registered client: " + response.clientId);
		mClientId.set(response.clientId);
		initializeAuthRequest();
	}
	@MainThread
	private void initializeAuthRequest() {
		if (Variable.isDebug) {
			Log.e(TAG, "initializeAuthRequest()");
		}
		createAuthRequest(getLoginHint());
		warmUpBrowser();
		//displayAuthOptions();
	}


	@MainThread
	void startAuth() {
		// WrongThread inference is incorrect for lambdas
		// noinspection WrongThread

		if (Variable.isDebug) {
			Log.e(TAG, "startAuth");
		}
		mExecutor.submit(this::doAuth);
	}
//	public Thread splashTread = new Thread(){
//
//		@Override
//		public void run() {
//	           try{
//	                int waited =1000;
//	                while (_active && (waited < _splashTime)) {
//	                    sleep(1000);
//	                    if (_active) {
//	                        waited +=1000;
//	                    }
//	                }
//	            }catch(Exception e){
//	                // do nothing
//	            }finally{
//	              	runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							if (dialog != null) {
//				        		dialog.dismiss();
//				        		dialog = null;
//							}
//						}
//
//					});
//
//            	    Intent intent=new Intent();
//	                intent.setClass(activity, MainActivity.class);
//	                startActivity(intent);
//	                finish();
//	            }
//		}
//	};
	
//	public class login extends AsyncTask<Void, Void, Boolean> {
//
//		private Context context;
//
//		public login(Context context) {
//			this.context = context;
//		}
//
//		@Override
//		protected Boolean doInBackground(Void... params) {
//			AnalyseInput analyseInput=new AnalyseInput();
//			application.setUserId(etUserId.getText().toString());
//			application.setLoginAuthStatus("I");
//            return analyseInput.loginAuth(LoginActivity.this);
//		}
//
//		@Override
//    	protected void onPostExecute(Boolean result) {
//			if (result == true) {
//    			// Save preference on userId and pwd for login automatically next time.
//				// Then move to list page.
//				if (dialog != null) {
//	        		dialog.dismiss();
//	        		dialog = null;
//				}
//				Preference preference=new Preference();
//    			//preference.setPrefs(LoginActivity.this, "EMP", "userId", SplashScreen.UserId);
//    			preference.setPrefs(LoginActivity.this, "EMP", "userId", application.getUserId());
//    			preference.setPrefs(LoginActivity.this, "EMP", "pwd", "PASS");
//
//    			Intent i = new Intent(LoginActivity.this, MainActivity.class);
//
//    			if(application.getIsFromNoti()){
////    				Bundle bundle=getIntent().getExtras();
////    				i.putExtras(bundle);//放入從推播來的url
//    			}
//			    startActivity(i);
//				finish();
//			} else {
//				if (dialog != null) {
//	        		dialog.dismiss();
//	        		dialog = null;
//				}
////				login_linearlayout.setBackgroundResource(R.drawable.login_bg);
//				etPwd.setVisibility(View.VISIBLE);
//				etUserId.setVisibility(View.VISIBLE);
//				bLogin.setVisibility(View.VISIBLE);
//	    	}
//    	}
//
//	}
//	private final BroadcastReceiver mHandleMessageReceiver =
//            new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String newMessage = intent.getExtras().toString();
//
//        }
//
//
//    };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Variable.isDebug) {
			Log.e(TAG, "onActivityResult()");
		}
		//displayAuthOptions();
		if (resultCode == RESULT_CANCELED) {
			//displayAuthCancelled();
		} else {
			Intent intent = new Intent(this, TokenActivity.class);
			intent.putExtras(data.getExtras());
			startActivity(intent);
			finish();
		}
	}
    
    @Override
	protected void onPause()
	{
	    //unregisterReceiver(mHandleMessageReceiver);
	    super.onPause();
	}

	private void doAuth() {
		if (Variable.isDebug) {
			Log.e(TAG, "doAuth");
		}

		try {
			mAuthIntentLatch.await();
		} catch (InterruptedException ex) {
			if (Variable.isDebug) {
				Log.e(TAG, "Interrupted while waiting for auth intent"+ ex.toString());
			}
		}

		Intent intent = mAuthService.getAuthorizationRequestIntent(
				mAuthRequest.get(),
				mAuthIntent.get());
		if (Variable.isDebug) {
			Log.e(TAG, "mAuthRequest.get().toUri(): "+ mAuthRequest.get().toUri());
			Log.e(TAG, "mAuthIntent.get().intent.getPackage(): "+ mAuthIntent.get().intent.getPackage());
			Log.e(TAG, "intent.getPackage(): "+ intent.getPackage());
			//deleteCache(this);
		}

		//intent.setPackage(getApplicationContext().getPackageName());
		startActivityForResult(intent, RC_AUTH);

//		mAuthService.performAuthorizationRequest(
//				mAuthRequest.get(),
//				PendingIntent.getActivity(this, 0, new Intent(this, TokenActivity.class), 0),
//				PendingIntent.getActivity(this, 0, new Intent(this, TokenActivity.class), 0));

//		Intent completionIntent = new Intent(this, TokenActivity.class);
//		Intent cancelIntent = new Intent(this, LoginActivity.class);
//		cancelIntent.putExtra(EXTRA_FAILED, true);
//		cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//		mAuthService.performAuthorizationRequest(
//				mAuthRequest.get(),
//				PendingIntent.getActivity(this, 0, completionIntent, 0),
//				PendingIntent.getActivity(this, 0, cancelIntent, 0),
//				mAuthIntent.get());
	}

	private void recreateAuthorizationService() {
		if (Variable.isDebug) {
			Log.e(TAG, "Discarding existing AuthService instance");
		}
		if (mAuthService != null) {
			if (Variable.isDebug) {
				Log.e(TAG, "dispose old mAuthService");
			}
			mAuthService.dispose();
		}
		mAuthService = createAuthorizationService();
		mAuthRequest.set(null);
		mAuthIntent.set(null);
	}

	private AuthorizationService createAuthorizationService() {

		if (Variable.isDebug) {
			Log.e(TAG, "Creating new authorization service");
		}
		AppAuthConfiguration.Builder builder = new AppAuthConfiguration.Builder();
		builder.setBrowserMatcher(mBrowserMatcher);
		builder.setConnectionBuilder(mConfiguration.getConnectionBuilder());

		return new AuthorizationService(this, builder.build());
	}

	private void warmUpBrowser() {
		mAuthIntentLatch = new CountDownLatch(1);
		mExecutor.execute(() -> {

			if (Variable.isDebug) {
				Log.e(TAG, "Warming up browser instance for auth request");
			}
			Uri realAuthUri = mAuthRequest.get().toUri();
			Uri dummyAuthUri = realAuthUri.buildUpon().appendQueryParameter("warmup","true").build();

			CustomTabsIntent.Builder intentBuilder =
					mAuthService.createCustomTabsIntentBuilder(dummyAuthUri);

			intentBuilder.setToolbarColor(getColorCompat(R.color.colorPrimary));

			mAuthIntent.set(intentBuilder.build());
			mAuthIntentLatch.countDown();
		});
	}

	private void createAuthRequest(@Nullable String loginHint) {

		if (Variable.isDebug) {
			Log.e(TAG, "createAuthRequest()");
		}
		AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest.Builder(
				mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration(),
				mClientId.get(),
				ResponseTypeValues.CODE,
				mConfiguration.getRedirectUri())
				.setScope(mConfiguration.getScope());

		if (Variable.isDebug) {
			Log.e(TAG, "Creating auth request for login hint: " + loginHint+" "+mConfiguration.getScope());
		}
		if (!TextUtils.isEmpty(loginHint)) {
			authRequestBuilder.setLoginHint(loginHint);
		}

		mAuthRequest.set(authRequestBuilder.build());
	}

	@TargetApi(Build.VERSION_CODES.M)
	@SuppressWarnings("deprecation")
	private int getColorCompat(@ColorRes int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return getColor(color);
		} else {
			return getResources().getColor(color);
		}
	}
}
