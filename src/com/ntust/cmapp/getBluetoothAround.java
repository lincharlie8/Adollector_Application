package com.ntust.cmapp;





import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class getBluetoothAround extends Service implements LeScanCallback {
	 private static final String TAG = "BLESample";
	 private Map<String, String> beaconMAC =new HashMap<String, String>();
	 private Handler mHandler;
	 private ArrayList<String> removeMac;
	 private ArrayList<String> isUsed;
	 private BluetoothAdapter mBluetoothAdapter;
	 private BluetoothManager mBluetoothManager;
	 private BluetoothGatt mBluetoothGatt;
	 private TextView mStatusText;
	 private Boolean repeat=true;
	 private int seconds;
	
	 private static  int MY_NOTIFICATION_ID=1;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	 @Override
	    public void onCreate() {
	        Log.d("mylog", "onCreate");
	        //Toast.makeText(this, "MyService#onCreate", Toast.LENGTH_SHORT).show();
	        mHandler = new Handler();
	        isUsed=new ArrayList<String>();
	        removeMac=new ArrayList<String>();
	        Calendar c = Calendar.getInstance(); 
	        seconds = c.get(Calendar.SECOND);
	        beaconMAC .clear();
	    }
	 
	    @Override
	    public int onStartCommand(final Intent intent, int flags, final int startId) {
	    	getbluetooth();
	        //明示的にサービスの起動、停止が決められる場合の返り値
	        return START_STICKY;
	    }
	 
	    @Override
	    public void onDestroy() {
	    	mBluetoothAdapter.stopLeScan(getBluetoothAround.this);
	    	 Log.d("mylog", "onDestroy");
	        //Toast.makeText(this, "MyService#onDestroy", Toast.LENGTH_SHORT).show();
	        repeat=false;
	        super.onDestroy();
	    }
	    public void getbluetooth(){
	    	String mac="";
	    	 isUsed=new ArrayList<String>();
	    	for (Map.Entry<String,String> entry : beaconMAC.entrySet()) {
	    			isUsed.add(entry.getKey());
	    			removeMac.add(entry.getKey());
	    			
	    			continue;
	    	
	        }	
	    	
	    	
	    	for( int i =0 ;i<removeMac.size();i++){
	    		if(beaconMAC.containsKey(removeMac.get(i)))
	    		beaconMAC.remove(removeMac.get(i));
	    	}
	    	
	    	
	    	mHandler.postDelayed(new Runnable() {
	    		
	            @Override
	            public void run() {
	            	//showMAC();
	            	if(repeat){
	            		if(isUsed.size()>0){
	            			 Context context = getApplicationContext();
	 	     	            
	            			 Intent myIntent = new Intent(context,  Ofsearch_Activity.class);
		     	             myIntent.putExtra("likeclick", 0);
		     	             myIntent.putExtra("Beacon",isUsed);
		     	             myIntent.putExtra("UserID", "");
		     	             PendingIntent pendingIntent = PendingIntent.getActivity(
		     	            		 context, 
		     	                    0, 
		     	                    myIntent, 
		     	                   PendingIntent.FLAG_UPDATE_CURRENT);
		     	            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notify_icon_large);
		     	        	 Notification myNotification = new NotificationCompat.Builder(context)
		     	               .setContentTitle("ADollector" )
		     	               .setContentText("There are some ADers around you!")
		     	               .setTicker("Found new ADs!")
		     	               .setWhen(System.currentTimeMillis())
		     	               .setContentIntent(pendingIntent)
		     	               .setDefaults(Notification.DEFAULT_SOUND)
		     	               .setAutoCancel(true)
		     	               .setSmallIcon(R.drawable.notify_icon)
		     	               .setLargeIcon(bm)
		     	               .build();
		     	             
		     	        	 NotificationManager notificationManager = 
		     	               (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		     	             notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
	            		}
	            		
		            		checkble();
		            		// Log.d("mylog", "onStartCommand Received start id " );
		            	 //toast.show();
		            	 mBluetoothAdapter.stopLeScan(getBluetoothAround.this);
		        		 mBluetoothAdapter.startLeScan(getBluetoothAround.this);
		            	 getbluetooth();	
	            	}
	            }
	        }, 5000);
	    	checkble();
	    	mBluetoothAdapter.stopLeScan(getBluetoothAround.this);
	    	mBluetoothAdapter.startLeScan(getBluetoothAround.this);
	    	
	    }
	    
	    public void checkble(){
	    		mBluetoothManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
		        mBluetoothAdapter = mBluetoothManager.getAdapter();
				if(mBluetoothAdapter==null){
					onDestroy();
				}else{
					
					if(!mBluetoothAdapter.isEnabled()){
						onDestroy();
					}
				}
	    }
	    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
	         if(!removeMac.contains( device.getAddress() ) ){
	        	 beaconMAC.put(device.getAddress(), "0");
	         }
	         
	         
	     }
}
