package com.chinaairlines.component;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.util.Log;

public class HtmlDownloadManager {
	//這一個Class的功能就是把URL內的HTML下載下來,再存到SQLITE的DB
	Context context;
	public HtmlDownloadManager(Context context) {

		this.context=context;
		//downloadHTMLContent();
	}
//    public void downloadHTMLContent(final NotificationItem item){
//      	AsyncHttpClient client = new AsyncHttpClient();
//      	client.get(item.getMsgUrl(), new AsyncHttpResponseHandler(){
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] response,
//					Throwable arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
//				// TODO Auto-generated method stub
//				if(arg0==200){
//					String str = new String(response); // for UTF-8 encoding
//					Log.v("TAG",str);
//					item.setMsgHTML(str);
//					DB db=new DB(context);
//					db.inserHtmlToMsg(context,item);
//				}
//			}});
//    }
	
	public void downloadHTMLContent(final String id, String url){
      	AsyncHttpClient client = new AsyncHttpClient();

      	client.get(url, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] response,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
				// TODO Auto-generated method stub
				if(arg0==200){
					String str = new String(response); // for UTF-8 encoding
					if (Variable.isDebug)
						Log.v("TAG",str);

//					item.setMsgHTML(str);
					DB db=new DB(context);
					db.inserHtmlToMsg(context, id, str);
				}
			}});
    }
	
}
