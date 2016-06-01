package com.sqa.sqatest;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {


	private List<WifiConfiguration> wifiConfigurationList;
	private ConnectivityManager connManager;
	private NetworkInfo netInfo;
	private TextView webBrowserText, youtubeText, hideText;
	private Button connectionButton, webBowserButton, youtubeButton;
	private Spinner securitySpinner, authSpinner, inputSpinner, keyLengthSpinner, defaultKeySpinner, encryptionSpinner;
	private EditText keyEditText, ssidEditText, usernameEditText, passwordEditText;
	private RelativeLayout mainLayout;
	private ProgressDialog dialog;
	
	private ArrayAdapter<String> listAdapter;

	
	private int securityType = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	String[] securityList = {"Disabled", "WEP", "WPA-PSK", "WPA2-PSK", "WPA-Enterprise", "WPA2-Enterprise"};
    	String[] authList = {"Open", "Shared"};
    	String[] inputList = {"Hex", "ASCII"};
    	String[] keyLengthList = {"40/64-bit", "104/128-bit"};
    	String[] defaultKeyList = {"1", "2", "3", "4"};
    	String[] encryptionList = {"AES", "TKIP"};

        webBrowserText = (TextView)findViewById(R.id.webBrowserText);
        youtubeText = (TextView)findViewById(R.id.youtubeText);
        hideText = (TextView)findViewById(R.id.hideText);
        connectionButton = (Button)findViewById(R.id.connectionButton);
        webBowserButton = (Button)findViewById(R.id.webBrowserButton);
        youtubeButton = (Button)findViewById(R.id.youtubeButton);
        securitySpinner = (Spinner)findViewById(R.id.securitySpinner);
        authSpinner = (Spinner)findViewById(R.id.authSpinner);
        inputSpinner = (Spinner)findViewById(R.id.inputSpinner);
        keyLengthSpinner = (Spinner)findViewById(R.id.keyLengthSpinner);
        defaultKeySpinner = (Spinner)findViewById(R.id.defaultKeySpinner);
        encryptionSpinner = (Spinner)findViewById(R.id.encryptionSpinner);
        keyEditText = (EditText)findViewById(R.id.keyEditText);
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        ssidEditText = (EditText)findViewById(R.id.ssidEditText);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        
//		dialog = new ProgressDialog(this);
//		dialog.setTitle("connecting...");
//		dialog.setMessage("Please wait 30 seconds.");
//		dialog.setCancelable(false);

        connectionButton.setOnClickListener(new connectionListener());
        webBowserButton.setOnClickListener(new webListener());
        youtubeButton.setOnClickListener(new youtubeListener());
        securitySpinner.setOnItemSelectedListener(new securitySpinnerListener());
        authSpinner.setOnItemSelectedListener(new securitySpinnerListener());
        inputSpinner.setOnItemSelectedListener(new securitySpinnerListener());
        keyLengthSpinner.setOnItemSelectedListener(new securitySpinnerListener());
        defaultKeySpinner.setOnItemSelectedListener(new securitySpinnerListener());
        encryptionSpinner.setOnItemSelectedListener(new securitySpinnerListener());
        keyEditText.setOnFocusChangeListener(new editTextOnChange());
        ssidEditText.setOnFocusChangeListener(new editTextOnChange());
        usernameEditText.setOnFocusChangeListener(new editTextOnChange());
        passwordEditText.setOnFocusChangeListener(new editTextOnChange());
        
        mainLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainLayout.requestFocus();
				
			}
        	
        });
        
        spinnerSet(securitySpinner, securityList);
        spinnerSet(authSpinner, authList);
        spinnerSet(inputSpinner, inputList);
        spinnerSet(keyLengthSpinner, keyLengthList);
        spinnerSet(defaultKeySpinner, defaultKeyList);
        spinnerSet(encryptionSpinner, encryptionList);
        
        webBrowserText.setText("Web:");
        youtubeText.setText("Youtube:");
        
    }
    
    class connectionListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			WifiManager wifi;
			int netId;

			mainLayout.requestFocus();
	        hideText.setText("NULL");
			//dialog.show();

			
	        wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	        
	        if (!wifi.isWifiEnabled()){
	        	wifi.setWifiEnabled(true);
	        }
	        
	        wifi.disconnect();
/*	        
			wifiConfigurationList = wifi.getConfiguredNetworks();
			for(WifiConfiguration result : wifiConfigurationList ){
				wifi.removeNetwork(result.networkId);
			}
*/	        
	        WifiConfiguration wifiConfig = CreateWifiInfo();
	        
			try{
				netId = wifi.addNetwork(wifiConfig);
				wifi.enableNetwork(netId, true);
			}catch (Exception e){
				System.out.println(e);
			}
			
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					for(int i=0;i<30;i++){
						netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
						System.out.println(netInfo.isConnected());
						if(netInfo.isConnected())
							break;
						try{
							Thread.sleep(1000);
						}catch (Exception e){
							System.out.println(e);
						}
					}
					
					Message msg = handler.obtainMessage();
		    		msg.obj = netInfo.isConnected();
		    		handler.sendMessage(msg);
					
				}
				
			}).start();
			
/*			
			mainText.setText("Connecting...");
			
			for(int i=0;i<30;i++){
				connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if(netInfo.isConnected())
					break;
				try{
					Thread.sleep(1000);
				}catch (Exception e){
					System.out.println(e);
				}
			}
			
			if(netInfo.isConnected()){
				isConnected=true;
			}else{
				mainText.setText("Connect to "+wifiConfig.SSID+" fail");
			}
*/
		}
    	
    }
    
    class webListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent webBrowserIntent = new Intent();
			Bundle webBrowserBundle = new Bundle();
			webBrowserBundle.putString("item", "Open Web Browser");
			webBrowserIntent.setClass(MainActivity.this, WebBrowserActivity.class);
			webBrowserIntent.putExtras(webBrowserBundle);
			MainActivity.this.startActivityForResult(webBrowserIntent, 0);
			
		}
    	
    }
    
    class youtubeListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent youtubeIntent = new Intent();
			Bundle youtubeBundle = new Bundle();
			youtubeBundle.putString("item", "Open Youtube");
			youtubeIntent.setClass(MainActivity.this, YoutubeActivity.class);
			youtubeIntent.putExtras(youtubeBundle);
			MainActivity.this.startActivityForResult(youtubeIntent, 1);
			
		}
    	
    }
    
    public void spinnerSet(Spinner name, String[] list){
    	listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
    	name.setAdapter(listAdapter);
    }
    
    class securitySpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int securityNum, long arg3) {
			// TODO Auto-generated method stub
			
			if(arg0.getId()==R.id.securitySpinner){
				
				switch(securityNum){
					case 0:
						securityType = 0;
						authSpinner.setVisibility(8);
						inputSpinner.setVisibility(8);
						keyLengthSpinner.setVisibility(8);
						defaultKeySpinner.setVisibility(8);
						encryptionSpinner.setVisibility(8);
						keyEditText.setVisibility(8);
						usernameEditText.setVisibility(8);
						passwordEditText.setVisibility(8);
						break;
					case 1:
						securityType = 1;
						authSpinner.setVisibility(0);
						inputSpinner.setVisibility(0);
						keyLengthSpinner.setVisibility(0);
						defaultKeySpinner.setVisibility(0);
						encryptionSpinner.setVisibility(8);
						keyEditText.setVisibility(0);
						usernameEditText.setVisibility(8);
						passwordEditText.setVisibility(8);
						break;
					case 2:
					case 3:
						securityType = 2;
						authSpinner.setVisibility(8);
						inputSpinner.setVisibility(8);
						keyLengthSpinner.setVisibility(8);
						defaultKeySpinner.setVisibility(8);
						encryptionSpinner.setVisibility(0);
						keyEditText.setVisibility(0);
						usernameEditText.setVisibility(8);
						passwordEditText.setVisibility(8);
						break;
					case 4:
					case 5:
						securityType = 3;
						authSpinner.setVisibility(8);
						inputSpinner.setVisibility(8);
						keyLengthSpinner.setVisibility(8);
						defaultKeySpinner.setVisibility(8);
						encryptionSpinner.setVisibility(0);
						keyEditText.setVisibility(8);
						usernameEditText.setVisibility(0);
						passwordEditText.setVisibility(0);
						break;
				}
			}
			
			mainLayout.requestFocus();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    class editTextOnChange implements OnFocusChangeListener{

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			System.out.println("is On Focus: "+hasFocus);
			if(!hasFocus){
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(keyEditText.getWindowToken(), 0);
			}else{
				EditText et = (EditText) v;
				et.getText().clear();
			}
		}
    }
    
    @SuppressLint("NewApi")
    public WifiConfiguration CreateWifiInfo(){
    	
		String SSID, key;
		
		SSID = ssidEditText.getText().toString().matches("")?"":ssidEditText.getText().toString();
		key = keyEditText.getText().toString().matches("")?"":keyEditText.getText().toString();
		
    	WifiConfiguration config = new WifiConfiguration();
    	
    	config.allowedAuthAlgorithms.clear();
    	config.allowedGroupCiphers.clear();
    	config.allowedKeyManagement.clear();
    	config.allowedPairwiseCiphers.clear();
    	config.allowedProtocols.clear();
		config.status = WifiConfiguration.Status.ENABLED;
    	config.SSID = "\"" + SSID + "\"";
        
    	
    	//none
    	if(securityType == 0){
        	config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        	//config.wepTxKeyIndex = 0;
        	//config.wepKeys[0] = "";
    	}
    	
    	//WEP
    	if(securityType == 1){
    		System.out.println(SSID);
    		System.out.println(key);
    		System.out.println(authSpinner.getSelectedItem().toString());
    		System.out.println(keyLengthSpinner.getSelectedItem().toString());
    		System.out.println(defaultKeySpinner.getSelectedItemPosition());
    		config.hiddenSSID = true;
    		if(inputSpinner.getSelectedItem().toString().equals("ASCII"))
    			config.wepKeys[defaultKeySpinner.getSelectedItemPosition()]= "\""+key+"\"";
    		else
    			config.wepKeys[defaultKeySpinner.getSelectedItemPosition()]= key;
    		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    		config.wepTxKeyIndex = defaultKeySpinner.getSelectedItemPosition();
    		if(authSpinner.getSelectedItem().toString().equals("Shared"))
    			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
    		else
    			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//    		if(keyLengthSpinner.getSelectedItem().toString().equals("40/64-bit"))
    			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//    		else
    			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
    	}
    	
    	//PSK
		if(securityType == 2){
    		config.hiddenSSID = true;
    		config.preSharedKey = "\""+key+"\"";
    		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
    		if(securitySpinner.getSelectedItem().toString().equals("WPA-PSK"))
    			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
    		else
    			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
    		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
    		if(encryptionSpinner.getSelectedItem().toString().equals("TKIP")){
    			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
    			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
    		}else{
    			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    		}
		}
		
		//Enterprise
		if(securityType == 3){
			WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
    		config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
    		if(securitySpinner.getSelectedItem().toString().equals("WPA-Enterprise"))
    			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
    		else
    			config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
    		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
    		if(encryptionSpinner.getSelectedItem().toString().equals("TKIP")){
    			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
    			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
    		}else{
    			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    		}
    		enterpriseConfig.setIdentity("sqa");
    		enterpriseConfig.setPassword("sqa");
    		enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP); 
    		config.enterpriseConfig=enterpriseConfig;
		}
		
		return config;     
    }
    

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("In onActivityResult");

		String result;
		Bundle bundle = data.getExtras();
		
		if(requestCode==0){
			result = bundle.getString("webBrowserResult");
			webBrowserText.setText("Web: "+result);
		}else{
			result = bundle.getString("youtubeResult");
			youtubeText.setText("Youtube: "+result);
		}
	}
	
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			//dialog.dismiss();
			hideText.setText(msg.obj+"");
			System.out.println(hideText.getText());
		}
    	
    };
}
