package com.shin.looper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView textViewID;
	//---4---
	//private Handler handler;
	//---4---
	private int num = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textViewID = (TextView)findViewById(R.id.textViewID);
        //---4---
        //handler = new Handler();
        //---4---
 /*      
 		//---1---
        new Thread(new Runnable() {
            
            public void run(){
            	for(int i=0;i<100;i++){
	            	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	System.out.println("first");
            	}
            }
        }).start();
        
        new Thread(new Runnable() {
            
            public void run() {
            	for(int i=0;i<100;i++){
	            	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	System.out.println("secound");
            	}
            }
        }).start();
        //---1---
*/
        //---2---4---
        myThread thread1 = new myThread();
        //myThread thread2 = new myThread();
        thread1.start();
        //thread2.start();
        //---2---4---
        
        System.out.println("finish");
    }
    
    
    class myThread extends Thread{
    	
    	public void run(){
    		
    		//---2---4---5---
    		for(int i=0;i<50;i++){
	    		System.out.println("Thread"+i+"--->" + Thread.currentThread().getName());
    			//---4---
	    		//handler.post(mRefresh);
	    		//---4---
	    		//---5---
	    		Message msg = handler.obtainMessage();
	    		msg.obj = i;
	    		handler.sendMessage(msg);
	    		//---5---
	    		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		//---2---4---5---
/*    		
    		//---3---
    		myRunnable run1 = new myRunnable();
    		myRunnable run2 = new myRunnable();
    		run1.run();
    		run2.run();
    		//---3---
*/    		
    	}
    }
    
    //---3---   
    class myRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
    		for(int i=0;i<50;i++){
	    		System.out.println("Thread--->" + Thread.currentThread().getName());
	    		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}	
		}	
    }
    //---3---
    
    //---4---
    private Runnable mRefresh = new Runnable() {
        
        @Override
        public void run() {
        	textViewID.setText(num+"");
        	num++;
        }
    };
    //---4---
    
    //---5---
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			System.out.println("Thread"+msg.obj+"--->" + Thread.currentThread().getName());
			
		}
    	
    };
	//---5---
 
}
