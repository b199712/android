package com.shin.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private Button scanButton;
	private TextView ssidText;
	WifiManager wifi;
    List<ScanResult> wifiScanResult;
    List<WifiConfiguration> wifiConfigurationList;
    WifiInfo wifiInfo;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

    	wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        
        scanButton = (Button)findViewById(R.id.scanButton);
        ssidText = (TextView)findViewById(R.id.ssidText);
        scanButton.setOnClickListener(new scanListener(this));

    }

    class scanListener implements OnClickListener{
    	
    	private MainActivity activity;
    	SimpleAdapter listAdapter = null;
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>() ;
    	
    	public scanListener(MainActivity activity) {
    		this.activity = activity;
    	}
    	
    	public void onClick(View v) {
    		
    		if(listAdapter!=null){
    			list.clear();
    			listAdapter.notifyDataSetChanged();
    		}
            
    		wifi.startScan();
    		wifiScanResult = wifi.getScanResults();
    		System.out.println(wifiScanResult.size());
			for(ScanResult result : wifiScanResult ){
				HashMap<String,String> info = new HashMap<String,String>();
				info.put("ssid", result.SSID);
				info.put("seucrity", checkSecurity(result.capabilities));
				info.put("channel", "Ch"+checkChannel(result.frequency));
				info.put("rssi", result.level+"");
            	list.add(info);
			}
			try{
	            listAdapter = new SimpleAdapter(activity, list, R.layout.scanresult,new String[]{"ssid", "seucrity", "channel", "rssi"},new int[]{R.id.ssid, R.id.security, R.id.channel, R.id.rssi});
	            setListAdapter(listAdapter);
			}catch (Exception e){
				System.out.println(e);
			}
    	}
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		//get list ssid value
		String theSSID = ((TextView) v.findViewById(R.id.ssid)).getText().toString();
		String security = ((TextView) v.findViewById(R.id.security)).getText().toString();
		String channel = ((TextView) v.findViewById(R.id.channel)).getText().toString();

		System.out.println(checkChannel(Integer.parseInt(channel)));
		ssidText.setText("SSID : "+theSSID+" Security : "+security+" Channel : "+channel);
		
	}
	
	protected String checkSecurity(String security){
		
		String result="";
		
		if(security.equals("[ESS]")){
			result="none";
		}else if(security.contains("WEP")){
			result="WEP";
		}else{
			if(security.contains("WPA-")){
				result="WPA";
			}
			
			if(security.contains("WPA2-")){
				if(!"".equals(result)){
					result+="/";
				}
				result+="WPA2";
			}
			
			if(security.contains("PSK")){
				result+=" - PSK";
			}
			
			if(security.contains("EAP")){
				result+=" - Enterprise";
			}
			
			if(security.contains("CCMP")){
				result+=" AES";
			}
			
			if(security.contains("TKIP")){
				if(result.contains("AES")){
					result+="+";
				}
				result+="TKIP";
			}
		}
		
		return result;
	}
	
	protected int checkChannel(int channel){
		
		int result=0;
		int band1Max=5240, band2Max=5320, band3Max=5700, band4Max=5825;
		int startCh=0, temp=0;
		
		if(channel<5000){
			result=(channel-2412)/5+1;
		}else{
			if(channel<band1Max){
				startCh=48;
				temp=band1Max-channel;
			}else if(channel<band2Max){
				startCh=64;
				temp=band2Max-channel;
			}else if(channel<band3Max){
				startCh=140;
				temp=band3Max-channel;
			}else if(channel<band4Max){
				startCh=165;
				temp=band4Max-channel;
			}
			result=startCh-(4*(temp/20));
		}
		
		return result;
		
	}
}
