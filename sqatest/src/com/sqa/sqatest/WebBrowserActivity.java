package com.sqa.sqatest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebBrowserActivity extends Activity {

	WebView webBrowser;
	String result = "null";
	Intent intent;
	Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webbrowserlayout);
		

		intent = this.getIntent();
		bundle = intent.getExtras();
		System.out.println(bundle.getString("item"));
		
		webBrowser = (WebView)findViewById(R.id.webbrowser);
		webBrowser.getSettings().setJavaScriptEnabled(true);
		webBrowser.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("onPageStarted");
			}
			
			@Override
			public void onPageFinished (WebView view, String url){
				super.onPageFinished(view, url);
				System.out.println("onPageFinished");
				if(result.equals("null"))
					result = "PASS";
				System.out.println(result);
				bundle.putString("webBrowserResult",result);  
				intent.putExtras(bundle);
				setResult(0, intent);
				finish();
			}

			@Override
			public void onReceivedError (WebView view, int errorCode, String description, String failingUrl){
				super.onReceivedError(view, errorCode, description, failingUrl);
				System.out.println("onReceivedError");
				System.out.println(errorCode);
				System.out.println(description);
				System.out.println(failingUrl);
				result = description;
			}
		});

		webBrowser.loadUrl("http://tw.yahoo.com");
	}
}
