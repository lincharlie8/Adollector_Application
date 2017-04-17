package com.ntust.cmapp;







import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.ntust.cmapp.R.color;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Ofsearch_Activity extends Activity{
	public Map<String, String> customerID = new HashMap<String, String>();
	public Map<String, Customer> itemInfo = new HashMap<String,Customer>();
	public Map<String, Customer> customerInfo = new HashMap<String,Customer>();
	public ArrayList<String> customerItemID = new ArrayList<String>();
	public Map<String,Bitmap> customerphoto = new HashMap<String,Bitmap>();
	public Map<String, String> usedBeacon=new HashMap<String, String>();
	public Map<String, String> foundBeacon=new HashMap<String, String>();
	public Map<String, ArrayList<String>> customerItemListMap = new HashMap<String, ArrayList<String>>();
	public HashMap<String, Map> allMap =new HashMap<String,Map>();
	private LinearLayout mainLayout;
	public LinearLayout cmregister_parent_layout;
	public Uri mPictureUri; 
	//public LinearLayout navbar;
	public Bitmap bmp;
	public ImageView[] navbar=new ImageView[4];
	public TextView[] navbarLine=new TextView[4];
	public LinearLayout[] navbackground=new LinearLayout[4];
	private Fragment[] navFragment= new Fragment[4];
	private int unselectColor,selectColor;
	private Map<String, String> beaconMAC =new HashMap<String, String>();
	private Boolean isFragmentCreated=false;
	private int FragmentNow=0,OnceShowAmount=15;
	private boolean isRefresh=true;
	/*BLE*///////////
	
	
	 /** BLE 機器スキャンタイムアウト (ミリ秒) */
    private static final long SCAN_PERIOD = 5000;
    /** 検索機器の機器名 */
    private static final String DEVICE_NAME = "SensorTag";
    /** 対象のサービスUUID */
    private static final String DEVICE_BUTTON_SENSOR_SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    /** 対象のキャラクタリスティックUUID */
    private static final String DEVICE_BUTTON_SENSOR_CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    /** キャラクタリスティック設定UUID */
    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
 
    private static final String TAG = "BLESample";
//    private BleStatus mStatus = BleStatus.DISCONNECTED;
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;
    private TextView mStatusText;
    JSONObject ReturnInfoJsonobject = new JSONObject();
    String[] customerphotoName ;
	public String beaconId="",thisUserID="",clickAdID="",clickUserID="";
	public int likeclick=0;
	public ArrayList<String> beaconIDs=new ArrayList<String> ();
	
	/*BLE*///////////
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopService(new Intent(Ofsearch_Activity.this, getBluetoothAround.class));
        setContentView(R.layout.ofsearch);
       
        Intent it = getIntent();
      
        	 likeclick= it.getIntExtra("likeclick",0);
        	 beaconIDs = it.getStringArrayListExtra("Beacon");
             if(likeclick!=0){
             	clickAdID = it.getStringExtra("clickAdID");
             	clickUserID = it.getStringExtra("clickUserID");
             }
             
             thisUserID = it.getStringExtra("UserID");
       
       
        new GetItemInfo().execute();
        
    }
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	stopService(new Intent(Ofsearch_Activity.this, getBluetoothAround.class));
    	super.onStart();
    }
    class GetItemInfo extends AsyncTask<String, String, String> {
    	int total = 0;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
		}

		/**
		 * Entering BeaconID
		 * */
		@Override
		protected String doInBackground(String... args) {
			
			JSONObject UploadBeaconIDObject = new JSONObject();
			
		
			
			JSONArray BeaconJsonarray = new JSONArray();
			ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				for(int i=0;i<beaconIDs.size();i++){
					BeaconJsonarray.put(beaconIDs.get(i));
				}
				
			
				customerID.clear();
				customerItemListMap.clear();
				customerphoto.clear();
				itemInfo.clear();
				usedBeacon.clear();
				customerInfo.clear();
				
			//BeaconJsonarray.put(FakeBeaconID2);
			
				try {
					if(likeclick!=0){
						
						UploadBeaconIDObject.put("UserID", clickUserID);//格式
						UploadBeaconIDObject.put("AdID", clickAdID);//格式
					}
					UploadBeaconIDObject.put("isLike", likeclick);//格式
					UploadBeaconIDObject.put("Beacon", BeaconJsonarray);//格式
					UploadBeaconIDObject.put("CustomerID", thisUserID);//格式
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				Log.d("mylog",UploadBeaconIDObject.toString());
				jsonarray.add(new BasicNameValuePair("Beacon", UploadBeaconIDObject.toString()));// 重要！！
				try {
					// Note that create product url accepts POST method

					HttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/get_user_Ad.php");
					httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
					HttpResponse httpResponse = httpClient.execute(httpPost); 
					HttpEntity httpEntity = httpResponse.getEntity();
					Log.d("mylog", "ENTER");
					try {
						String json = EntityUtils.toString(httpEntity);
						
						ReturnInfoJsonobject = new JSONObject(json);
						Log.d("mylog", ReturnInfoJsonobject.toString());
					} catch (JSONException ex) {
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("mylog", "error");
				}
				return null;
			
			
	
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			
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
						
							for(int j=0;j<length2;j++){
								PartItemjsonobject=jsonarray2.getJSONObject(j);//找第j條
								Log.d("mylog", PartItemjsonobject.toString());
								
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
								
								Log.d("mylog", echoUserID);
								Log.d("mylog", echoAdID);
								Log.d("mylog", "_____");
								String CustomerItemID = echoUserID + ( ItemIndex+1<10 ? ("0"+ItemIndex+1) : ItemIndex+1);
								Customer customerItem = new Customer( ItemWidth, ItemHeight, ItemTop, ItemLeft, ItemBrand, ItemType, ItemColor, echoSex,ItemPrice,ItemUrl,ItemIndex,isBrandItem);
								
								customerItemID.add(CustomerItemID);
					            itemInfo.put(CustomerItemID, customerItem);
					            
					            
							}
							
							customerItemListMap.put(echoUserID+echoAdID,customerItemID);
				            customerItemID = new ArrayList<String>();
						
						
					}
					creatFragment();
					//imgDownLoader id =new imgDownLoader(CMRegister_Activity.this);
					//id.execute(customerphotoName);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			
			 
			//showDetail(UploadSuccessInput);
			
		}
		
		

	}
    private void creatFragment(){//顯示Fragment
		 Fragment BrowseFragment = (Fragment)new CMBrowse_Fragment(true);	
	    
		    	Bundle extras=new Bundle();
		    	allMap.put("customerID", customerID);
		    	allMap.put("customerItemListMap", customerItemListMap);
		    	allMap.put("itemInfo", itemInfo);
		    	allMap.put("customerInfo", customerInfo);
		    	//allMap.put("customerphoto", customerphoto);
		    	extras.putSerializable("allMap", allMap);
		    	extras.putString("UserID", thisUserID);
		    	BrowseFragment.setArguments(extras);
	   
	    	FragmentManager fm=getFragmentManager();
			FragmentTransaction ft =fm.beginTransaction();
			
			ft.add(R.id.ofsearch_layout, BrowseFragment);//顯示GetPhoto Fragment
			ft.commit();
  }

}
