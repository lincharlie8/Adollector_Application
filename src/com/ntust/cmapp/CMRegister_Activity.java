package com.ntust.cmapp;







import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ntust.cmapp.CMBrowse_Fragment.brandADDownLoader;
import com.ntust.cmapp.CMSetting_Activity.EnterBeaconID;
import com.ntust.cmapp.CMSetting_Activity.brandItems_listview;
import com.ntust.cmapp.CMSetting_Activity.isBrandItem;
import com.ntust.cmapp.CMSetting_Activity.uploadItem_listview;
import com.ntust.cmapp.CMSetting_Activity.brandItems_listview.imgDownLoader;
import com.ntust.cmapp.R.color;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CMRegister_Activity extends Activity implements BluetoothAdapter.LeScanCallback,GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{
	 private BluetoothGattService tar_service;
	    private BluetoothGattCharacteristic tar_characteristic,characteristic;
	public Map<String, String> customerID = new HashMap<String, String>();
	public Map<String, Customer> itemInfo = new HashMap<String,Customer>();
	public Map<String, Customer> customerInfo = new HashMap<String,Customer>();
	public ArrayList<String> customerItemID = new ArrayList<String>();	
	public Map<String, String> usedBeacon=new HashMap<String, String>();	
	public Map<String, String> couponBrand=new HashMap<String, String>();	
	public ArrayList<String> couponCode=new ArrayList<String>();	
	public Map<String, String> couponDate=new HashMap<String, String>();	
	
	public Map<String, ArrayList<String>> customerItemListMap = new HashMap<String, ArrayList<String>>();
	
	public HashMap<String, Map> allMap =new HashMap<String,Map>();
	Map<String,Bitmap> Download_brandAD = new HashMap<String,Bitmap>();
	public boolean setAD=false;
	private Dialog dlg,prgdlg,coupondlg;
	private LinearLayout mainLayout;
	public String GoogleUserID="",UserID="",UserBeaconMac="",AdID="",buffer="";
	public LinearLayout cmregister_parent_layout;
	public Uri mPictureUri; 
	public Bitmap bmp;
	public ImageView[] navbar=new ImageView[4];
	public TextView[] navbarLine=new TextView[4];
	public LinearLayout[] navbackground=new LinearLayout[4];
	private Fragment[] navFragment= new Fragment[4];
	private int unselectColor,selectColor;
	private Map<String, String> beaconMAC =new HashMap<String, String>();
	private Map<String, String> beaconUUID =new HashMap<String, String>();
	private Boolean isFragmentCreated=false;
	private int FragmentNow=0,OnceShowAmount=12;
	private boolean isRefresh=true,isLogOut=false;
	private int timeOfSearchBeacon=0;
	private AnimationDrawable anim;
	/*BLE*///////////
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private ProgressDialog Progressdlg;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
	
	 /** BLE 機器スキャンタイムアウト (ミリ秒) */
    private static final long SCAN_PERIOD = 5000;
    private static final String TAG = "BLESample";
    private BleStatus mStatus = BleStatus.DISCONNECTED;
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;
    private TextView mStatusText;
   
    String[] customerphotoName ;
	
	
	
	/*BLE*///////////
    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }
	@Override
	protected  void onStart() {
		stopService(new Intent(CMRegister_Activity.this, getBluetoothAround.class));
		super.onStart();
       
	}
	public void startService(){
		boolean isServiceExit=false;
		
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	    	Log.d("mylog",service.service.getClassName() );
	        if ("com.ntust.cmapp.getBluetoothAround".equals(service.service.getClassName())) {
	        	isServiceExit=true;
	        	break;
	        }
	    }
        if(!isServiceExit){
        	startService(new Intent(CMRegister_Activity.this, getBluetoothAround.class));
        }
	}
	public void findBeacon(boolean setAD,String buffer){
		this.buffer=buffer;
		this.setAD=setAD;
		stopService(new Intent(CMRegister_Activity.this, getBluetoothAround.class));
		Log.d("mylog", "findBeacon");
		 	mBluetoothManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
	        mBluetoothAdapter = mBluetoothManager.getAdapter();
			if(mBluetoothAdapter==null){
				Log.d("mylog", "NO Bluetooth Can Use!");
				finish();
			}else{
				
				if(!mBluetoothAdapter.isEnabled()){
					
					new AlertDialog.Builder(CMRegister_Activity.this)
					.setTitle("Turn On Bluetooth")
					.setMessage("Do You Want to Turn On BlueTooth?")
					.setPositiveButton("OK", new OnClickListener(){
						public void onClick(DialogInterface DialogInterface,int i ){
							mBluetoothAdapter.enable();
							
						}
					})
					.setNegativeButton("Cancel", new OnClickListener(){
						public void onClick(DialogInterface DialogInterface,int i ){
							finish();
						}
					})
					.show();
				}else{
					timeOfSearchBeacon=1;
				}
			}
		 mHandler = new Handler() {
	            @Override
	            public void handleMessage(Message msg) {
	                mStatusText.setText(((BleStatus) msg.obj).name());
	            }
	        };
	        Progressdlg.cancel();
	        showprogressDialog("searching");
	        connect();
	        
	}
	 private void connect() {
	    	mBluetoothManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
	        mBluetoothAdapter = mBluetoothManager.getAdapter();
	        mHandler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	//showMAC();
	            	if(beaconMAC.size()==0 && timeOfSearchBeacon<3){
	            		timeOfSearchBeacon++;
	            		 mBluetoothAdapter.stopLeScan(CMRegister_Activity.this);
	            		 mBluetoothAdapter.startLeScan(CMRegister_Activity.this);
	            		 
	            		connect();
	            	}else{
	            		timeOfSearchBeacon=0;
	            		new GetItemInfo().execute();
	            		if(prgdlg!=null)
	            		prgdlg.cancel();
	            		
	            		showprogressDialog("cycling");
	            		mBluetoothAdapter.stopLeScan(CMRegister_Activity.this);
	            	}
	            	//傳出BeaconID收回Item資訊
	                
	                if (BleStatus.SCANNING.equals(mStatus)) {
	                    setStatus(BleStatus.SCAN_FAILED);
	                }
	            }
	        }, SCAN_PERIOD);
	        
	      
	        mBluetoothAdapter.stopLeScan(this);
	        mBluetoothAdapter.startLeScan(this);
	        
	        //setStatus(BleStatus.SCANNING);
	    }
	 
	 @Override
	    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
	       // Log.d(TAG, "device found: " + device.getName());
	        if(!setAD){
	        	
		      
		        int startByte = 2;
		        boolean patternFound = false;
		        while (startByte <= 5) {
		            if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
		                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
		                patternFound = true;
		                break;
		            }
		            startByte++;
		        }
		        
		        if (patternFound) {
		            //Convert to hex String
		            byte[] uuidBytes = new byte[16];
		            System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
		            String hexString = bytesToHex(uuidBytes);
		            
		            //Here is your UUID
		            String uuid =  hexString.substring(0,8) + "-" + 
		                    hexString.substring(8,12) + "-" + 
		                    hexString.substring(12,16) + "-" + 
		                    hexString.substring(16,20) + "-" + 
		                    hexString.substring(20,32);
		            Log.d("mylog",device.getAddress()+ uuid);
		            //Here is your Major value
		            int major = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);
		            Log.d("mylog",device.getAddress()+ major);
		            //Here is your Minor value
		            int minor = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);
		            Log.d("mylog",device.getAddress()+ minor);
		            if(major==44289 && minor==7879){
		            	beaconMAC.put(device.getAddress(), device.getAddress());
		            	beaconUUID.put(device.getAddress(),uuid);
		            	  Log.d("mylog",device.getAddress()+ device.getName());
		            }
		        }
	        }else{
	        	
	        	if(device.getAddress().equals(UserBeaconMac)){
		        	 if(mBluetoothGatt!=null){
		        		 mBluetoothGatt.disconnect();
		        	 }
		        	
		        	 mBluetoothAdapter.stopLeScan(this);
		        	
		 	            mBluetoothGatt = device.connectGatt(this, false, mBluetoothGattCallback);
		 	          
		 	     
		        }
	        }
	        
	        
	        
	       
	        //    mBluetoothGatt = device.connectGatt(this, false, mBluetoothGattCallback);
	        
	    }
	    
	    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	    private static String bytesToHex(byte[] bytes) {
	        char[] hexChars = new char[bytes.length * 2];
	        for ( int j = 0; j < bytes.length; j++ ) {
	            int v = bytes[j] & 0xFF;
	            hexChars[j * 2] = hexArray[v >>> 4];
	            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	        }
	        return new String(hexChars);
	    }
	    public static byte[] HexStringToBytes(String s) {
	        int len = s.length();
	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                    + Character.digit(s.charAt(i + 1), 16));
	        }
	        return data;
	    }
	    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

	        @Override
	        public void onCharacteristicRead(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
	            super.onCharacteristicRead(gatt, characteristic, status);
	            byte[] data = characteristic.getValue();
	            final String out = "0x" + bytesToHex(data);
	            
	        }

	        @Override
	        public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
	            super.onCharacteristicWrite(gatt, characteristic, status);
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                   // Toast.makeText(CMRegister_Activity.this, "Write " + bytesToHex(characteristic.getValue()), Toast.LENGTH_SHORT).show();
	                    String trunon_buffer="01";
	                    BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString("0000ffa0-0000-1000-8000-00805f9b34fb"));
	                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("0000ffbf-0000-1000-8000-00805f9b34fb"));
	 	               
	 	            	characteristic.setValue(HexStringToBytes(trunon_buffer));
	 	                mBluetoothGatt.writeCharacteristic(characteristic);
	                }
	            });
	           
	        }

	        @Override
	        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
	            super.onConnectionStateChange(gatt, status, newState);
	            if (newState == BluetoothProfile.STATE_CONNECTED) {
	                runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    }
	                });
	                mBluetoothGatt.discoverServices();
	               
	                
	            }
	            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
	                runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    //	Toast.makeText(CMRegister_Activity.this, "Please Turn On or Restart Your Beacon !", Toast.LENGTH_SHORT).show();
	                    }
	                });
	            }
	            if (newState == BluetoothProfile.STATE_CONNECTING) {
	                runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	
	                    }
	                });
	            }
	        }

	        @Override
	        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
	            if (status == BluetoothGatt.GATT_SUCCESS) {
	                for (final BluetoothGattService service : gatt.getServices()) {
	                    runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                          
	                        }
	                    });
	                }
	                
	                BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString("0000ffa0-0000-1000-8000-00805f9b34fb"));
	            	characteristic = service.getCharacteristic(UUID.fromString("0000ffb2-0000-1000-8000-00805f9b34fb"));
	            	
	            	
	            
	                
	                
	                /*if(!buffer.matches("[\\da-fA-F]+")){
	                    buffer = "";
	                }*/
	                characteristic.setValue(HexStringToBytes(buffer));
	                mBluetoothGatt.writeCharacteristic(characteristic);
	                
	              
	             
	            }
	        }
	    };
	    private void setStatus(BleStatus status) {
	        mStatus = status;
	        mHandler.sendMessage(status.message());
	    }
	    private enum BleStatus {
	        DISCONNECTED,
	        SCANNING,
	        SCAN_FAILED,
	        DEVICE_FOUND,
	        SERVICE_NOT_FOUND,
	        SERVICE_FOUND,
	        CHARACTERISTIC_NOT_FOUND,
	        NOTIFICATION_REGISTERED,
	        NOTIFICATION_REGISTER_FAILED,
	        CLOSED
	        ;
	        public Message message() {
	            Message message = new Message();
	            message.obj = this;
	            return message;
	        }
	    }
	    
	    /////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmregister_layout);
       
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//禁止畫面翻轉
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止畫面翻轉
	    
	    stopService(new Intent(CMRegister_Activity.this, getBluetoothAround.class));
	    
        mainLayout =(LinearLayout)findViewById(R.id.main_layout);//抓取主要畫面位置
        cmregister_parent_layout=(LinearLayout)findViewById(R.id.cmregister_parent_layout);//最外層顯示位置
       
        
        navbar[0] =(ImageView)findViewById(R.id.home);
        navbar[1] =(ImageView)findViewById(R.id.picture);
        navbar[2] =(ImageView)findViewById(R.id.search);
        navbar[3] =(ImageView)findViewById(R.id.more);
        navFragment[0]=(Fragment)new CMBrowse_Fragment(false);
        navFragment[1]=(Fragment)new GetPhoto_Fragment();
        navFragment[2]=(Fragment)new Search_Activity(false);
        navFragment[3]=(Fragment)new Information_Fragment();
        navbar[0].setOnClickListener(navbarListener);
        navbar[1].setOnClickListener(navbarListener);
        navbar[2].setOnClickListener(navbarListener);
        navbar[3].setOnClickListener(navbarListener);
        navbarLine[0] =(TextView)findViewById(R.id.homeText);
        navbarLine[1] =(TextView)findViewById(R.id.pictureText);
        navbarLine[2] =(TextView)findViewById(R.id.searchText);
        navbarLine[3] =(TextView)findViewById(R.id.moreText);
        navbackground[0] =(LinearLayout)findViewById(R.id.homenav);
        navbackground[1] =(LinearLayout)findViewById(R.id.picturenav);
        navbackground[2] =(LinearLayout)findViewById(R.id.searchnav);
        navbackground[3] =(LinearLayout)findViewById(R.id.morenav);
        unselectColor=getResources().getColor(R.color.navbarcolor);
        selectColor=getResources().getColor(R.color.select);
        Progressdlg= new ProgressDialog(CMRegister_Activity.this);
        // Build GoogleApiClient with access to basic profile
        SignInButton signinButton=(SignInButton)findViewById(R.id.sign_in_button);
        signinButton.setOnClickListener(new Button.OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSignInClicked();
			}
        	
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
        mGoogleApiClient.connect();
     
        
    }
    
    public void reLoad(Boolean isRefresh){
    	this.isRefresh=isRefresh;
    	if(isRefresh){
    		beaconMAC.clear();
    		usedBeacon.clear();
    		showprogressDialog("searching");
    		connect();
    	}else{
    		showprogressDialog("cycling");
    		new GetItemInfo().execute();
    	}
    }
   
    public void showProgressDialog(){
		
		Progressdlg.setTitle("Loading Now");
		Progressdlg.setMessage("Please Wait a Second..");
		Progressdlg.setCancelable(false);
		Progressdlg.setIndeterminate(true);
	}
    
	@Override
	public void onBackPressed() {
	    // your code.
		new AlertDialog.Builder(CMRegister_Activity.this)
		.setTitle("Exit")
		.setMessage("Sure to Exit ADollector？")
		.setPositiveButton("OK", new OnClickListener(){
			public void onClick(DialogInterface DialogInterface,int i ){
				 ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				    	Log.d("mylog",service.service.getClassName() );
				        if ("com.ntust.cmapp.getBluetoothAround".equals(service.service.getClassName())) {
				        	
				        	stopService(new Intent(CMRegister_Activity.this, getBluetoothAround.class));
				        }
				    }
				    startService();

				finish();
			}
		})
		.setNegativeButton("Cancel", new OnClickListener(){
			public void onClick(DialogInterface DialogInterface,int i ){
				
			}
		})
		.show();
	}
	
    private ImageView.OnClickListener navbarListener =new ImageView.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			for(int i=0;i<4;i++){
				
				navbarLine[i].setBackgroundColor(unselectColor);
				navbackground[i].setBackgroundColor(unselectColor);
				if (v ==navbar[i]){
					navbarLine[i].setBackgroundColor(selectColor);
					navbackground[i].setBackgroundColor(getResources().getColor(R.color.selectednavbarcolor));
					if(i==FragmentNow){break;}
					FragmentNow=i;
					replaceFragment(navFragment[i]);
				}
			}
		}
    	
    	
    };

    
    private void creatFragment(Fragment fragment){//顯示Fragment
    	if(fragment==navFragment[0]|| fragment==navFragment[2]){
	    	Bundle extras=new Bundle();
	    	allMap.put("customerID", customerID);
	    	allMap.put("customerItemListMap", customerItemListMap);
	    	allMap.put("itemInfo", itemInfo);
	    	allMap.put("customerInfo", customerInfo);
	    	allMap.put("customerBeaconID", beaconMAC);
	    	extras.putSerializable("allMap", allMap);
	    	extras.putString("UserID", UserID);
	    	fragment.setArguments(extras);
    	}
    	FragmentManager fm=getFragmentManager();
		FragmentTransaction ft =fm.beginTransaction();
		ft.add(R.id.main_layout, fragment);//顯示GetPhoto Fragment
		ft.commit();

    }
    
    public void replaceFragment(Fragment fragment){//替換Fragment
    	
    	if(fragment==navFragment[0] ||fragment==navFragment[2]){
	    	Bundle extras=new Bundle();
	    	allMap.put("customerID", customerID);
	    	allMap.put("customerItemListMap", customerItemListMap);
	    	allMap.put("itemInfo", itemInfo);
	    	allMap.put("customerInfo", customerInfo);
	    	allMap.put("customerBeaconID", beaconMAC);
	    	
	    	extras.putSerializable("allMap", allMap);
	    	extras.putString("UserID", UserID);
	    	fragment.setArguments(extras);
    	}
    	if(fragment==navFragment[1]){
    		Bundle extras=new Bundle();
    		extras.putString("BeaconID", UserBeaconMac);
    		extras.putString("UserID", UserID);
    		extras.putString("AdID", AdID);
    		
    		fragment.setArguments(extras);
    	}
    	
    	FragmentManager fm=getFragmentManager();
		FragmentTransaction ft =fm.beginTransaction();
		
		ft.replace(R.id.main_layout, fragment);//替換GetPhoto Fragment
		ft.commit();
    }
    
    class GetItemInfo extends AsyncTask<String, String, String> {
    	int total = 0;
    	 JSONObject ReturnInfoJsonobject = new JSONObject();

		@Override
		protected String doInBackground(String... args) {
			
			JSONObject UploadBeaconIDObject = new JSONObject();
			JSONArray BeaconJsonarray = new JSONArray();
			ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
	
				beaconMAC.put(UserBeaconMac, UserBeaconMac);
		        //beaconMAC.put( "20:C3:8F:F0:EF:36",  "20:C3:8F:F0:EF:36");
		       for(int i=0;i<=31;i++){
		        	String newMac="";
		        	if(i<10){
		        		newMac="00:00:00:00:00:0"+i;
		        	}else{
		        		newMac="00:00:00:00:00:"+i;
		        	}
		        	beaconMAC.put(newMac, newMac);
		        }
		        
			if(!isRefresh){
				total=0;
				for(String key: beaconMAC.keySet()){
					if(total>=OnceShowAmount){
						break;
					}
					if (usedBeacon.get(key)==null){
					//	Log.d("mylog", key);
						usedBeacon.put(key, key);
						BeaconJsonarray.put(key);
						total++;
					}else{
						continue;
					}
				}
			}else{
				total=0;
				customerID.clear();
				customerItemListMap.clear();
				
				itemInfo.clear();
				usedBeacon.clear();
				customerInfo.clear();
				
				for(String key: beaconMAC.keySet()){
					Log.d("mylog", key);
					if(total>=OnceShowAmount){
						break;
					}
					if (usedBeacon.get(key)==null){
						//Log.d("mylog", key);
						usedBeacon.put(key, key);
						BeaconJsonarray.put(key);
						total++;
					}else{
						continue;
					}
				}
			}
			
			if(total!=0){
				try {
					UploadBeaconIDObject.put("Beacon", BeaconJsonarray);//格式
					UploadBeaconIDObject.put("CustomerID", UserID);//格式
					UploadBeaconIDObject.put("isLike", "0");//格式

				} catch (JSONException e) {
					e.printStackTrace();
					Progressdlg.cancel();
					if(prgdlg!=null)
					prgdlg.cancel();
				}
				
				
				jsonarray.add(new BasicNameValuePair("Beacon", UploadBeaconIDObject.toString()));// 重要！！
				Log.d("mylog", jsonarray.toString());
				try {
					// Note that create product url accepts POST method

					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/get_user_Ad.php");
					httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
					HttpResponse httpResponse = httpClient.execute(httpPost); 
					HttpEntity httpEntity = httpResponse.getEntity();
					//Log.d("mylog", "ENTER");
					try {
						String json = EntityUtils.toString(httpEntity);
						ReturnInfoJsonobject = new JSONObject(json);
						
					} catch (JSONException ex) {
						Progressdlg.cancel();
						if(prgdlg!=null)
						prgdlg.cancel();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("mylog", "error");
					Progressdlg.cancel();
					if(prgdlg!=null)
					prgdlg.cancel();
				}
				return null;
			}else{
				prgdlg.cancel();
			}
			
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			if(total!=0){
				String ReturnInfo = ReturnInfoJsonobject.toString();
				
				try {
					JSONArray jsonarray = ReturnInfoJsonobject.getJSONArray("AllAd");
					JSONObject AllItemjsonobject = new JSONObject(); 
					int length1 = jsonarray.length(); 
					
					customerphotoName=new String[length1];
					
					int customerSize=customerID.size();
					for(int i=0;i<length1;i++){
						AllItemjsonobject=jsonarray.getJSONObject(i);//找第i條
						
						String echoUserID = AllItemjsonobject.getString("UserID");
						String echoAdID = AllItemjsonobject.getString("AdID");//抓userID, AdID
						if(echoUserID.equals(UserID)){
							AdID=echoAdID;
						}
						
						int echoSex = Integer.parseInt( AllItemjsonobject.getString("Sex"));
						
						
						
						
						customerphotoName[i]=echoUserID+echoAdID;
						JSONArray jsonarray2 = AllItemjsonobject.getJSONArray("Item");
						JSONObject PartItemjsonobject = new JSONObject(); 
						int length2 = jsonarray2.length(); 
						
						String echoUserName = AllItemjsonobject.getString("UserName");
						String echoBeaconId = AllItemjsonobject.getString("BeaconID");
						String website = AllItemjsonobject.getString("website");
						int LikeClick =Integer.parseInt(AllItemjsonobject.getString("LikeClick"));
						Customer customer = new Customer(echoUserID,echoAdID, echoUserName,echoSex,echoBeaconId,LikeClick,website);
						if(customerInfo.get(echoUserID+echoAdID)!=null){
							continue;
						}
							customerInfo.put(echoUserID+echoAdID, customer);
						
							customerID.put(String.valueOf(customerSize+i+1), echoUserID+echoAdID);
							Log.d("mylog", echoUserID);
							Log.d("mylog", echoAdID);
							Log.d("mylog", "Like:"+LikeClick);
							Log.d("mylog", "_____");
							for(int j=0;j<length2;j++){
								PartItemjsonobject=jsonarray2.getJSONObject(j);//找第j條
								//Log.d("mylog", PartItemjsonobject.toString());
								
								//String UserID = PartItemjsonobject.getString("UserID");
								//String AdID = PartItemjsonobject.getString("AdID");
								int ItemIndex = (int)Integer.parseInt(PartItemjsonobject.getString("ItemIndex"));
								float ItemWidth = Float.parseFloat(PartItemjsonobject.getString("ItemWidth"));
								
								float ItemHeight = Float.parseFloat((PartItemjsonobject.getString("ItemHeight")));
								float ItemTop = Float.parseFloat(PartItemjsonobject.getString("ItemTop"));
								float ItemLeft = Float.parseFloat(PartItemjsonobject.getString("ItemLeft"));
								int ItemType = Integer.parseInt(PartItemjsonobject.getString("ItemType"));
								int ItemColor = Integer.parseInt(PartItemjsonobject.getString("ItemColor"));
								int ItemPrice = Integer.parseInt(PartItemjsonobject.getString("ItemPrice"));
								String ItemBrand = PartItemjsonobject.getString("ItemBrand");
								String ItemUrl = PartItemjsonobject.getString("BrandSite");
								int isBrandItem = Integer.parseInt(PartItemjsonobject.getString("isBrandItem"));
								
								String CustomerItemID = echoUserID + ( ItemIndex+1<10 ? ("0"+ItemIndex+1) : ItemIndex+1);
								Customer customerItem = new Customer(ItemWidth, ItemHeight, ItemTop, ItemLeft, ItemBrand, ItemType, ItemColor, echoSex,ItemPrice,ItemUrl,ItemIndex,isBrandItem);
								
								customerItemID.add(CustomerItemID);
					            itemInfo.put(CustomerItemID, customerItem);
					            
							}
							
							customerItemListMap.put(echoUserID+echoAdID,customerItemID);
				            customerItemID = new ArrayList<String>();
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Progressdlg.cancel();
					if(prgdlg!=null)
					prgdlg.cancel();
				}
				
				if(!isFragmentCreated){
					creatFragment(navFragment[FragmentNow]);//顯示GetPhoto Fragment
					isFragmentCreated=true;
					
			    	
					
				}else if(FragmentNow==0 || FragmentNow==2){
					
					FragmentManager fm = getFragmentManager();
					if(FragmentNow==0){
						CMBrowse_Fragment fragment = (CMBrowse_Fragment)fm.findFragmentById(R.id.main_layout);
						
						
						//if you added fragment via layout xml
						if(usedBeacon.size()>=beaconMAC.size()){
				    		fragment.setIsNoNewAder(true);
				    	}else{
				    		fragment.setIsNoNewAder(false);
				    	}
						fragment.refresh();
					}else{
						Search_Activity fragment = (Search_Activity)fm.findFragmentById(R.id.main_layout);
						
						
						//if you added fragment via layout xml
						if(usedBeacon.size()>=beaconMAC.size()){
				    		fragment.setIsNoNewAder(true);
				    	}else{
				    		fragment.setIsNoNewAder(false);
				    	}
						fragment.refresh();
					}
				}
				RelativeLayout welcomeLayour =(RelativeLayout)findViewById(R.id.welcomeLayout);
				LinearLayout.LayoutParams welcome_params =(LinearLayout.LayoutParams) welcomeLayour.getLayoutParams();
				welcome_params.height=0;
				
		    	
				welcomeLayour.setLayoutParams(welcome_params);
				Progressdlg.cancel();
				if(prgdlg!=null)
				prgdlg.cancel();
			}
			if(prgdlg!=null)
			prgdlg.cancel();
			//showDetail(UploadSuccessInput);
			
		}

	}
    
    class LogInCheck extends AsyncTask<String, String, String> {
    	private String thisGoogleUserID="";
    	JSONObject ReturnUserIDJsonobject = new JSONObject();
    	 public LogInCheck(String userID) {
			// TODO Auto-generated constructor stub
    		 this.thisGoogleUserID=userID;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
		}

		/**
		 * Entering BeaconID
		 * */
		@Override
		protected String doInBackground(String... args) {
			Log.d("mylog", "CheckUserNow:");
			JSONObject UserIDObject = new JSONObject();
			ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
			try {
				UserIDObject.put("GoogleUserID", this.thisGoogleUserID);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
				jsonarray.add(new BasicNameValuePair("GoogleUserID", UserIDObject.toString()));// 重要！！
				Log.d("mylog", "output:"+jsonarray.toString());
				try {
					// Note that create product url accepts POST method

					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/UserLogin.php");
					httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
					HttpResponse httpResponse = httpClient.execute(httpPost); 
					HttpEntity httpEntity = httpResponse.getEntity();
					
					try {
						String json = EntityUtils.toString(httpEntity);					
						ReturnUserIDJsonobject = new JSONObject(json);
						Log.d("mylog", "CheckUser:"+ReturnUserIDJsonobject.toString());
					} catch (JSONException ex) {
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("mylog", "UserLogInError");
				}
				return null;
				
			
		}

		protected void onPostExecute(String file_url) {
				
				try {
					String isCreated = ReturnUserIDJsonobject.getString("isCreated");
					Log.d("mylog", "isCreate:"+isCreated);
					if(isCreated.equals("0")){
						if(UserID==""){
							showDialog();
						}else{
							onSignOutClicked() ;
						}
					}else if(isCreated.equals("1")){
						if(UserID==""){
							CMRegister_Activity.this.UserID= ReturnUserIDJsonobject.getString("UserID");
							UserBeaconMac= ReturnUserIDJsonobject.getString("BeaconID");
							findBeacon(false,"");
						}else{
							RelativeLayout welcomeLayour =(RelativeLayout)findViewById(R.id.welcomeLayout);
							LinearLayout.LayoutParams welcome_params =(LinearLayout.LayoutParams) welcomeLayour.getLayoutParams();
							welcome_params.height=0;
							
					    	
							welcomeLayour.setLayoutParams(welcome_params);
							Progressdlg.cancel();
							if(prgdlg!=null)
							prgdlg.cancel();
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
				
			}
			
			 
		

	}
    class Create_Account extends AsyncTask<String, String, String> {
    	private String thisGoogleUserID="",thisUserName="",thisUserWebsite="";
    	private int thisUserSex=0;
    	JSONObject ReturnUserIDJsonobject = new JSONObject();
    	 public Create_Account(String GoogleUserID,String userName,int Sex,String Website) {
			// TODO Auto-generated constructor stub
    		 this.thisGoogleUserID=GoogleUserID;
    		 this.thisUserName=userName;
    		 this.thisUserWebsite=Website;
    		 this.thisUserSex=Sex;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
		}

		/**
		 * Entering BeaconID
		 * */
		@Override
		protected String doInBackground(String... args) {
			Log.d("mylog", "CreateUserNow:");
			JSONObject UserIDObject = new JSONObject();
			
			ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
			
			
			try {
				UserIDObject.put("GoogleUserID", this.thisGoogleUserID);
				UserIDObject.put("UserName", this.thisUserName);
				UserIDObject.put("UserSex", this.thisUserSex);
				UserIDObject.put("website", this.thisUserWebsite);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
				
				
				jsonarray.add(new BasicNameValuePair("CreateNewAccount", UserIDObject.toString()));// 重要！！
				try {
					// Note that create product url accepts POST method

					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/CreateNewUser.php");
					httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
					HttpResponse httpResponse = httpClient.execute(httpPost); 
					HttpEntity httpEntity = httpResponse.getEntity();
					
					try {
						String json = EntityUtils.toString(httpEntity);					
						ReturnUserIDJsonobject = new JSONObject(json);
						Log.d("mylog", "CreateUser:"+ReturnUserIDJsonobject.toString());
					} catch (JSONException ex) {
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("mylog", "UserCreateError");
				}
				return null;
			
			
		}

		protected void onPostExecute(String file_url) {
				
				try {
					String isCreated = ReturnUserIDJsonobject.getString("isCreated");
					if(isCreated.equals("0")){
						Toast toast =Toast.makeText(CMRegister_Activity.this,"Create Account Fail", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}else if(isCreated.equals("1")){
						Toast toast =Toast.makeText(CMRegister_Activity.this,"Create Account Scuess", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						new LogInCheck(GoogleUserID).execute();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
    }
    
    int[] rbInt=new int[2];
    int checkSex=0;
    public void showDialog(){
    	Log.d("mylog", "showDialogNow");
		dlg= new Dialog(CMRegister_Activity.this);
		dlg.setTitle("Create New Account");
		dlg.setContentView(R.layout.create_account);
		dlg.setCancelable(false);
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		
		Button connectOK=(Button)dlg.findViewById(R.id.createOK);
		Button connectCancle=(Button)dlg.findViewById(R.id.createCancle);
		RadioButton[] rb =new RadioButton[2]; 
		 rb[0]=(RadioButton)dlg.findViewById(R.id.rdMale);
		 rb[1]=(RadioButton)dlg.findViewById(R.id.rdFemale);
		
		for(int i=0;i<2;i++)
			rbInt[i]=rb[i].getId();
		RadioGroup rg=(RadioGroup)dlg.findViewById(R.id.SexRadiogroup);
		
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				for(int i=0;i<2;i++){
					if(checkedId==rbInt[i]){
						checkSex=i;
					}
				}
				
			}
			
			
		});
		connectOK.setOnClickListener(buttonCreateUser_click);
		connectCancle.setOnClickListener(buttonCreateUser_click);
		dlg.show();
	}
    private Button.OnClickListener buttonCreateUser_click =new  Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			
			if(v.getId()==R.id.createOK){
				TextView newUserName =(TextView)dlg.findViewById(R.id.NewUserName);
				
				TextView newUserBirth =(TextView)dlg.findViewById(R.id.NewUserBirth);
				new Create_Account(GoogleUserID,String.valueOf(newUserName.getText()),checkSex,String.valueOf(newUserBirth.getText())).execute();		
				dlg.cancel();
				
			}else if(v.getId()==R.id.createCancle){
				isLogOut=true;
			    if (mGoogleApiClient.isConnected()) {
			        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			        mGoogleApiClient.disconnect();
			    }
				dlg.cancel();
			}
		}
		
	};
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		SignInButton signinButton=(SignInButton)findViewById(R.id.sign_in_button);
		signinButton.setVisibility(View.VISIBLE);
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null && isLogOut==false) {
		    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		    String personName = currentPerson.getDisplayName();
		    GoogleUserID=currentPerson.getId();
		    Log.d("mylog", "ID:"+GoogleUserID);
		    showProgressDialog();
		    new LogInCheck(GoogleUserID).execute();
		    signinButton.setVisibility(View.INVISIBLE);
		    
		  }else{
			  onSignOutClicked();
			  Progressdlg.cancel();
			  if(prgdlg!=null)
			  prgdlg.cancel();
			 
		  }
	    mShouldResolve = false;
	}
	
	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	   
	    Log.d(TAG, "onConnectionFailed:" + connectionResult);

	    if (!mIsResolving && mShouldResolve) {
	        if (connectionResult.hasResolution()) {
	            try {
	                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
	                mIsResolving = true;
	            } catch (IntentSender.SendIntentException e) {
	                Log.e(TAG, "Could not resolve ConnectionResult.", e);
	                mIsResolving = false;
	                mGoogleApiClient.connect();
	            }
	        } else {
	          
	        	Progressdlg.cancel();
	        	if(prgdlg!=null)
	        	prgdlg.cancel();
	        }
	    } 
	}
	
	
	private void onSignInClicked() {
	    // User clicked the sign-in button, so begin the sign-in process and automatically
	    // attempt to resolve any errors that occur.
		isLogOut=false;
	    mShouldResolve = true;
	    mGoogleApiClient.connect();

	}
	private void onSignOutClicked() {
	    // Clear the default account so that GoogleApiClient will not automatically
	    // connect in the future.
		isLogOut=true;
	    if (mGoogleApiClient.isConnected()) {
	        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	        mGoogleApiClient.disconnect();
	        SignInButton signinButton=(SignInButton)findViewById(R.id.sign_in_button);
	        signinButton.setVisibility(View.VISIBLE);
	        RelativeLayout welcomeLayour =(RelativeLayout)findViewById(R.id.welcomeLayout);
	        LinearLayout.LayoutParams welcome_params =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	    	
			welcomeLayour.setLayoutParams(welcome_params);
	    }

	   
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

	    if (requestCode == RC_SIGN_IN) {
	        // If the error resolution was not successful we should not resolve further.
	        if (resultCode != RESULT_OK) {
	            mShouldResolve = false;
	        }

	        mIsResolving = false;
	        mGoogleApiClient.connect();
	        
	    }
	}
	public void showprogressDialog(String status){
		if(prgdlg!=null ){
			prgdlg.cancel();
		}
		
		prgdlg= new Dialog(CMRegister_Activity.this);
		prgdlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		prgdlg.setContentView(R.layout.progressdlg);
		prgdlg.setCancelable(false);
		
		prgdlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		ImageView prgimg=(ImageView)prgdlg.findViewById(R.id.progress_img);
		if(status.equals("cycling")){
			prgimg.setImageResource(R.drawable.cycling);
		}else{
			prgimg.setImageResource(R.drawable.searching);			
		}
		anim=(AnimationDrawable)prgimg.getDrawable();
		prgimg.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				anim.start();
			}
		});
		prgdlg.show();
		
	}
	ListView coupon_View;
	couponlist couponlist_Adapter;
	public void showcouponlistDialog(String status){
		
		coupondlg= new Dialog(this);
		if(status.equals("Coupon")){
			coupondlg.setTitle("Coupon List");
		}else{
			coupondlg.setTitle("Commission List");
		}
		coupondlg.setTitle("Coupon List");
		coupondlg.setContentView(R.layout.couponlist_dialog);
		coupondlg.setCancelable(false);
		coupondlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		coupon_View=(ListView)coupondlg.findViewById(R.id.couponlist_Listview);
		couponlist_Adapter= new couponlist(this,this,"0");
		TextView infocoupon = (TextView)coupondlg.findViewById(R.id.couponinfotxt);
		
		TextView datecoupon = (TextView)coupondlg.findViewById(R.id.coupondatetxt);
		if(status.equals("Coupon")){
			coupon_View.setOnItemClickListener(lvListener);
			infocoupon.setText("Coupon Code");
			datecoupon.setText("Deadline");
		}else{
			coupon_View.setOnItemClickListener(null);
			infocoupon.setText("Coupon");
			datecoupon.setText("Date");
		}
		
		
		Button couponOK=(Button)coupondlg.findViewById(R.id.couponlistOK);
		
		
		couponOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				coupondlg.cancel();
				
			}
		});
		new getCoupon(UserID).execute(status);
		coupondlg.show();
	}
	private ListView.OnItemClickListener lvListener =new ListView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			final Dialog coupon_dlg = new Dialog(CMRegister_Activity.this);
			coupon_dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);  
			coupon_dlg.setContentView(R.layout.coupon_dialog);
			
			coupon_dlg.setCanceledOnTouchOutside(true);
			coupon_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
			ImageView mimgforsale = (ImageView)coupon_dlg.findViewById(R.id.forsale);
			String code=couponCode.get(position);

    		
			if(Download_brandAD.get(couponBrand.get(code))==null){
				brandADDownLoader brandADDownLoader =new brandADDownLoader(mimgforsale,couponBrand.get(code));
				brandADDownLoader.execute();
			}else{
				mimgforsale.setImageBitmap(Download_brandAD.get(couponBrand.get(code)));
			}
			
			Button mbtnOK = (Button)coupon_dlg.findViewById(R.id.btnOK);
			mbtnOK.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					coupon_dlg.cancel();
					
				}
			});
			ImageView mimgcoupon = (ImageView)coupon_dlg.findViewById(R.id.coupon);
			TextView mtxtcoupon = (TextView)coupon_dlg.findViewById(R.id.txtcoupon);
			
			String barcode_data;
	
				barcode_data = couponCode.get(position);
				
				mtxtcoupon.setText(barcode_data);
				Bitmap bitmap = null;

 	               try {

 	                   bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
 	                  mimgcoupon.setImageBitmap(bitmap);

 	               } catch (WriterException e) {
 	                   e.printStackTrace();
 	               }
				
 	               
				ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE); 
				ClipData clip = ClipData.newPlainText("label", barcode_data);
				clipboard.setPrimaryClip(clip);
				Toast toast =Toast.makeText(CMRegister_Activity.this,"已複製Coupon代碼到剪貼簿", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				coupon_dlg.show();
				
			
		}

		
	};
	public class couponlist extends BaseAdapter {
    	
    	
    	Activity myactivity;//主要呼叫的Activity
    	private LayoutInflater myInflater;
    	private Context mycontext;
    	private int length=10;//預設長度10
    	private String itemid="";
    	private Drawable[] drawablekind;
    	private String[] brandName;
    	public couponlist(Activity activity,Context c,String itemid){
    		myInflater =LayoutInflater.from(c);
    		myactivity=activity;
    		mycontext=c;
    		int length=getResources().getInteger(R.integer.brandSum);
			TypedArray colorimg = getResources().obtainTypedArray(R.array.brandkind_images);//抓顏色陣列
			drawablekind = new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]=colorimg.getDrawable(i);
				
			}
			brandName=new String[getResources().getInteger(R.integer.brandSum)];
			brandName=getResources().getStringArray(R.array.brandName);
    	}
    	
    	public void setphotoID(String itemid){
    		this.itemid=itemid;
    	}
    	@Override
    	public int getCount() {
    			return couponCode.size();
    	}

    	@Override
    	public Object getItem(int position) {
    		
    		return position;
    	}

    	@Override
    	public long getItemId(int position) {
    		return position;
    	}

    	@Override
    	public View getView(final int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		convertView =myInflater.inflate(R.layout.couponlistview,null);//設定Layout
    		TextView couponBrandtxt =(TextView)convertView.findViewById(R.id.couponbrand);//coupon brand img
    		
    		TextView couponCodetxt=(TextView)convertView.findViewById(R.id.couponcode);//coupon Code
    		TextView couponDatetxt=(TextView)convertView.findViewById(R.id.coupondate);//coupon date
    		String code=couponCode.get(position);
    		couponCodetxt.setText(code);
    		couponDatetxt.setText(couponDate.get(code).substring(5));
    		couponBrandtxt.setText(couponBrand.get(code));
    	
    		return convertView;

    	}
    	

    }
	public class getCoupon extends AsyncTask<String, Integer, String> {

		private String UserID;//點擊者,廣告者
		JSONObject ReturnCodeJsonobject = new JSONObject();
		
		 public getCoupon(String UserID) {
		        super();
		      
		        this.UserID=UserID;
		       
		    }

		@Override
		protected String doInBackground(String... args) {
			Log.d("mylog", "CreateUserNow:");
			JSONObject UserIDObject = new JSONObject();
			ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
			try {
				UserIDObject.put("UserID",this.UserID);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
				jsonarray.add(new BasicNameValuePair("getCoupon", UserIDObject.toString()));// 重要！！
				try {
					int status=0;
					if(args[0].equals("Coupon")){
						status=0;
					}else{
						status=1;
					}
					String[] couponurl={"Get_Coupons.php","Get_UsedCoupons.php"};
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/"+couponurl[status]);
					httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
					HttpResponse httpResponse = httpClient.execute(httpPost); 
					HttpEntity httpEntity = httpResponse.getEntity();
					
					try {
						String json = EntityUtils.toString(httpEntity);					
						ReturnCodeJsonobject = new JSONObject(json);
						Log.d("mylog", "Coupon:"+ReturnCodeJsonobject.toString());
					} catch (JSONException ex) {
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("mylog", "CouponError");
				}
				return null;
			
			
		}

		protected void onPostExecute(String file_url) {
			couponBrand.clear();	
			couponCode.clear();	
			couponDate.clear();
	    	
			String barcode_data;
			try {
				JSONArray jsonarray = ReturnCodeJsonobject.getJSONArray("Coupons");
				JSONObject AllItemjsonobject = new JSONObject(); 
			
				for(int i=0;i<jsonarray.length();i++){
					AllItemjsonobject=jsonarray.getJSONObject(i);//找第i條
					
					String returnCouponBrandName = AllItemjsonobject.getString("BrandName");
					String returnCouponCode = AllItemjsonobject.getString("Code");//抓userID, AdID
					String returnCouponDate = AllItemjsonobject.getString("Date");//抓userID, AdID
					couponCode.add(returnCouponCode);
			    	couponBrand.put(returnCouponCode,returnCouponBrandName);
			    	couponDate.put(returnCouponCode,returnCouponDate);
					
				}
				
				coupon_View.setAdapter(couponlist_Adapter);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
		}
		
	
	}
	 public class brandADDownLoader extends AsyncTask<String, Integer, Bitmap> {
			
			private ImageView imgView;
			private String brandName;
			
			
			 public brandADDownLoader(ImageView imgView,String brandName) {
			        super();
			        this.imgView=imgView;
			        this.brandName=brandName;
			       
			        
			    }
			protected void onPreExecute() {
			}
			@Override
			protected Bitmap doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				try {
					
						Bitmap outBitmap;
						URL url=new URL("http://cmapp.nado.tw/AD/"+this.brandName.replaceAll(" ", "%20")+"_AD.png");
						HttpURLConnection httpCon =(HttpURLConnection) url.openConnection();
						httpCon.connect();
						if(httpCon.getResponseCode()!=200){
							throw new Exception("Failed to Connect!");
						}
						InputStream is =httpCon.getInputStream();
						outBitmap = BitmapFactory.decodeStream(is);   
					
					return outBitmap;
					
				} catch (Exception e) {
					Log.d("mylog","Faild to load Img!");
					// TODO: handle exception
				}
				return null;
			}
			 @Override
			    protected void onProgressUpdate(Integer... progress) {
			        //progressDialog_.incrementProgressBy(progress[0]);
			    }
			
			protected void onPostExecute(Bitmap img) {
				Download_brandAD.put(this.brandName,img);
				imgView.setImageBitmap(img);
			
				
			}
			
		
	}
	 private static final int WHITE = 0xFFFFFFFF;
	    private static final int BLACK = 0xFF000000;

	    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
	    String contentsToEncode = contents;
	    if (contentsToEncode == null) {
	        return null;
	    }
	    Map<EncodeHintType, Object> hints = null;
	    String encoding = guessAppropriateEncoding(contentsToEncode);
	    if (encoding != null) {
	        hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
	        hints.put(EncodeHintType.CHARACTER_SET, encoding);
	    }
	    MultiFormatWriter writer = new MultiFormatWriter();
	    BitMatrix result;
	    try {
	        result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
	    } catch (IllegalArgumentException iae) {
	        // Unsupported format
	        return null;
	    }
	    int width = result.getWidth();
	    int height = result.getHeight();
	    int[] pixels = new int[width * height];
	    for (int y = 0; y < height; y++) {
	        int offset = y * width;
	        for (int x = 0; x < width; x++) {
	        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
	        }
	    }

	    Bitmap bitmap = Bitmap.createBitmap(width, height,
	        Bitmap.Config.ARGB_8888);
	    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	    return bitmap;
	    }

	    private static String guessAppropriateEncoding(CharSequence contents) {
	    // Very crude at the moment
	    for (int i = 0; i < contents.length(); i++) {
	        if (contents.charAt(i) > 0xFF) {
	        return "UTF-8";
	        }
	    }
	    return null;
	    }
	    public void logout(){
			new AlertDialog.Builder(CMRegister_Activity.this)
			.setTitle("Log out")
			.setMessage("YourID:"+UserID+"\n"+"Your BeaconMAC:"+UserBeaconMac+"\n"+"Do you want to Log OUT？")
			.setPositiveButton("OK", new OnClickListener(){
				public void onClick(DialogInterface DialogInterface,int i ){
					onSignOutClicked(); 
				}
			})
			.setNegativeButton("Cancel", new OnClickListener(){
				public void onClick(DialogInterface DialogInterface,int i ){
					
				}
			})
			.show();
			
		}

}
