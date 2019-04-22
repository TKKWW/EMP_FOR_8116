package com.chinaairlines.component;

import com.chinaairlines.ciemp.CIEMPApplication;
import com.chinaairlines.ciemp.LoginActivity;
import com.chinaairlines.ciemp.R;
import com.chinaairlines.ciemp.SplashScreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenOptionDialog {
	ProgressDialog mProgressDialog;
	Activity myactivity;

	public  OpenOptionDialog(Activity  _activity) {
		myactivity = _activity;
		mProgressDialog = new ProgressDialog(_activity);
		mProgressDialog.setMessage(_activity.getString(R.string.pdWaiting));
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
	}

	public void Positive(final Context context, String title, String message) {
		new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
	                intent.setClass(context, LoginActivity.class);
	                context.startActivity(intent);
	                ((Activity) context).finish();
				}
			})
		.show();
		
	}
	
	public void PositiveNoTitle(Context context, String message) {
		new AlertDialog.Builder(context)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {}
			})
		.show();
		
	}	
	
	public void PositiveNegativeNoTitle(Context context, String message) {
		new AlertDialog.Builder(context)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {}
			})
		.setNegativeButton(android.R.string.cancel, 
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			})
		.show();
	}
	
	public void DialogCloseApp(final Activity activity, String message) {
		new AlertDialog.Builder(activity)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {
//					System.exit(0);
					activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					System.exit(0);
				}
			})
		.show();
		
	}	
	
	public void DialogReturn(final Activity activity, String message) {
		new AlertDialog.Builder(activity)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {
					activity.finish();
				}
			})
		.show();
		
	}
	
	public void DialogforConnection(final Activity activity, String message) {
		new AlertDialog.Builder(activity)
		.setMessage(message)
		.setPositiveButton(R.string.bRetry, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {
					activity.recreate();
				}
			})
		.setNegativeButton(R.string.bClose, 
	    	new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int which) {
	    			//close the App
	    			activity.finish();
	    		}
	    	})
		.show();		
	}
	
	public void DialogforCheckVersion(final Activity activity, String message, String buttonWording, final String url) {
		new AlertDialog.Builder(activity)
		.setMessage(message)
		.setPositiveButton(buttonWording, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {
					//open browser
//					Uri uri = Uri.parse(url);
//					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//					activity.startActivity(intent);
//					activity.finish();
//					CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//					CustomTabsIntent customTabsIntent = builder.build();
//					builder.setToolbarColor(Color.parseColor("#00aeef"));
//					customTabsIntent.launchUrl(activity, Uri.parse(url));
					DownloadTask downloadTask = new DownloadTask(activity);
					downloadTask.execute(url);
				}
			})
		.setNegativeButton(R.string.bClose, 
	    	new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int which) {
	    			//close the App
	    			activity.finish();
	    		}
	    	})
		.show();		
	}

	public void DialogforDetaildownload(final Activity activity, String message, String buttonWording, final String url) {
		new AlertDialog.Builder(activity)
				.setMessage(message)
				.setPositiveButton(buttonWording,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//open browser
//					Uri uri = Uri.parse(url);
//					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//					activity.startActivity(intent);
//					activity.finish();
//					CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//					CustomTabsIntent customTabsIntent = builder.build();
//					builder.setToolbarColor(Color.parseColor("#00aeef"));
//					customTabsIntent.launchUrl(activity, Uri.parse(url));
								DownloadTask downloadTask = new DownloadTask(activity);
								downloadTask.execute(url);
							}
						})
				.setNegativeButton(R.string.bCancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								//close the App
								activity.finish();
							}
						})
				.show();
	}
	
	public void DialogforLogout(final Activity activity, String message) {
		final AlertDialog.Builder dialog= new AlertDialog.Builder(activity);
		dialog
		.setMessage(message) 
		.setPositiveButton(android.R.string.ok, 
			new DialogInterface.OnClickListener() {			
				public void onClick(DialogInterface dialog, int which) {
//
//					try {
//						// Logoout
//						CIEMPApplication application=(CIEMPApplication)activity.getApplication();
//						//SplashScreen.LoginAuthStatus = "O";
//						application.setLoginAuthStatus("O");
//						new Logout(activity).execute();
//					} catch (Exception e) {
//						String func = e.getStackTrace()[2].getFileName().substring(0, 4) + e.getStackTrace()[2].getMethodName().substring(0, 4);
//						String desc = e.toString().split(": ")[0].toString();
//						DB db=new DB(activity);
//						db.addLog(activity, func, desc);
//					}
//
//				}
//				class Logout extends AsyncTask<Void, Void, Boolean> {
//
//					private Activity activity;
//
//					public Logout(Activity activity) {
//						this.activity = activity;
//					}
//
//					@Override
//					protected Boolean doInBackground(Void... params) {
//						AnalyseInput analyseInput=new AnalyseInput();
//			            return analyseInput.loginAuth(activity);
//					}
//
//					@Override
//					protected void onPostExecute(Boolean result) {
//						if (result == true) {
//							// Clean preference on userId and pwd for login. Then logout.
//							Preference preference=new Preference();
//							preference.clearPrefs(activity, "EMP", "userId");
//				        	preference.clearPrefs(activity, "EMP", "pwd");
//
//				        	CIEMPApplication application=(CIEMPApplication)activity.getApplication();
//				        	application.setUserId("");
//				        	application.setPwd("");
//
//			    			Intent i = new Intent(activity, LoginActivity.class);
//							activity.startActivity(i);
//						    activity.finish();
//						}
//			    	}

					Intent i = new Intent(activity, SplashScreen.class);
					activity.startActivity(i);
					activity.finish();
				}


			})
		.setNegativeButton(android.R.string.cancel, 
	    	new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int which) {}
	    	})
		.show();		
	}
	
	

	private void downloadApk(Activity _activity ,String apkurl) {

		// instantiate it within the onCreate method
		// execute this when the downloader must be fired
		final DownloadTask downloadTask = new DownloadTask(_activity);
		downloadTask.execute(apkurl);

		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				downloadTask.cancel(true);
			}
		});
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// if we get here, length is known, now set indeterminate to false
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			Log.e("result","start:"+result+":end");
			mWakeLock.release();
			mProgressDialog.dismiss();
			if (result != null)
				Toast.makeText(context,"Download result: "+result, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			String returnnmsg = "";
			String PATH = Environment.getExternalStorageDirectory() + "/download";

			try {
				URL url = new URL(sUrl[0]);
				//Log.e("TAG","url "+url.toURI());
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP " + connection.getResponseCode()
							+ " " + connection.getResponseMessage();
				}

				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();
				//Log.e("TAG","length: "+fileLength);
				// download the file
				input = connection.getInputStream();
				//store file name
				String path = url.getPath();
				String filenameStr = path.substring(path.lastIndexOf('/') + 1);
				//File f = File.createTempFile("", null, getCacheDir());
				output = new FileOutputStream(PATH+"/"+filenameStr);

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					// publishing the progress....
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				OpenNewVersion(PATH,filenameStr);

			} catch (Exception e) {
				//Log.e("TAG",""+e.toString());
				returnnmsg = "faileded";
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
					//Log.e("TAG","IOException:"+ignored.toString());
				}

				if (connection != null)
					connection.disconnect();
				returnnmsg = "successsed";
			}
			return returnnmsg;
			//return null;
		}
	}

	void OpenNewVersion(String location, String filename) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(location + "/"+ filename)),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myactivity.startActivity(intent);
	}

}
