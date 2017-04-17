package com.ntust.cmapp;



import eu.janmuller.android.simplecropimage.*;

import com.ntust.cmapp.AndroidMultiPartEntity.ProgressListener;
import com.ntust.cmapp.CMBrowse_Fragment.imgDownLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.DragShadowBuilder;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class CMSetting_Activity extends Activity implements BluetoothAdapter.LeScanCallback{
	//CharacteristicPage
    private BluetoothGattService tar_service;
    private BluetoothGattCharacteristic tar_characteristic,characteristic;
	 private static final long SCAN_PERIOD = 5000;
	    private static final String TAG = "BLESample";
	    private BleStatus mStatus = BleStatus.DISCONNECTED;
	    private Handler mHandler;
	    private BluetoothAdapter mBluetoothAdapter;
	    private BluetoothManager mBluetoothManager;
	    private BluetoothGatt mBluetoothGatt;
	    private TextView mStatusText;
	    private Map<String, String> beaconMAC =new HashMap<String, String>();
	    private boolean findbeacon=false;
	private ArrayList<String> photoID = new ArrayList<String>();
	private ArrayList<Customer> brandItems = new ArrayList<Customer>();
	private ArrayList<String> branditemID = new ArrayList<String>();
	
	private Map<String, ImageView> photoView =new HashMap<String, ImageView>();//新增物件view
	private Map<String, Customer> photoCustomer =new HashMap<String, Customer>();
	private Map<String, ArrayList> brandArrayList =new HashMap<String, ArrayList>();
	private Map<String, ImageView> photoCloth =new HashMap<String, ImageView>();//新增物件種類 上衣cloth、下著pants、鞋子shoes
	private Map<String, ImageView> photoColor =new HashMap<String, ImageView>();
	private Map<String, ImageView> photoBrand =new HashMap<String, ImageView>();
	private Map<String, ImageView> photoPrice =new HashMap<String, ImageView>();
	private Map<String, String> branditemIndex =new HashMap<String, String>();
	private Map<String, Bitmap> branditemBitmap =new HashMap<String, Bitmap>();
	private Map<String, String> isBrandItem =new HashMap<String, String>();

	public static final  String RETURN_DATA            = "return-data";
    public static final  String RETURN_DATA_AS_BITMAP  = "data";
	JSONObject UploadSuccessJsonobject = new JSONObject();
	String BeaconInput ="";//傳出去的Beacon值
	final int PIC_CROP = 1;
	long totalSize = 0;
	private AnimationDrawable anim;
	private ImageView imageview;//照片ImgView
	private ImageView trashview;//照片ImgView
	private Dialog dlg,branddlg,prgdlg;
	brandItems_listview BranditemView_Adapter;
	ListView branditems_View;
	private ProgressDialog Progressdlg;
	private int viewId=1; //新增物間之ID
	private String UserBaconMac="",UserID="",buffer="";
	
	String photoURI;//相片的URI
	String photoPath;//相片的URI
	String howtogetPhoto;//相片的取得方式
	String thisADID;//相片的取得方式
	RelativeLayout parentview;//最外層layout
	
	String[] colorName;//顏色陣列名稱
	TypedArray colors ;//抓顏色陣列用的轉換陣列
	int[] color;//抓顏色陣列
	private Drawable[] drawablekind;//類型圖片
	String[] brandName;//顏色陣列名稱
	String[] PriceName;//顏色陣列名稱
	ClothListView_BaeAdater adapter;//服飾listview adapter
	selector_listview selectorAdapter;
	ArrayAdapter<String> adapterSelect;
	
	ListView selectorView;
	public int screenWidth=0;
	public int itemattrWidth=0;
	public ImageView[] navbar=new ImageView[4];
	public TextView[] navbarLine=new TextView[4];
	
	private String[] kind =new String[]{"cloth","colors","brand","price"};
	private int unselectColor,selectColor,pageNow=0;
	private Bitmap uploadBMP=null,uploadSmallBMP=null;
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		stopService(new Intent(CMSetting_Activity.this, getBluetoothAround.class));
		super.onStart();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		stopService(new Intent(CMSetting_Activity.this, getBluetoothAround.class));
		photoCustomer.clear();
		viewId=1;
		setContentView(R.layout.cmsetting_layout);
		imageview= (ImageView) findViewById(R.id.CMPhoto);//相片位置
		trashview= (ImageView) findViewById(R.id.trash);//垃圾桶位置
		parentview = (RelativeLayout)findViewById(R.id.imageview);//背景位置 新增物件用
		parentview.setOnTouchListener(parentTouchListener);
		selectorView=(ListView)findViewById(R.id.selector);
		//adapterSelect= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0);
		selectorAdapter= new selector_listview(CMSetting_Activity.this,this);
		selectorView.setAdapter(selectorAdapter);
		
		navbar[0] =(ImageView)findViewById(R.id.shirt);
        navbar[1] =(ImageView)findViewById(R.id.color);
        navbar[2] =(ImageView)findViewById(R.id.brand);
        navbar[3] =(ImageView)findViewById(R.id.price);

        
        navbar[0].setOnClickListener(navbarListener);
        navbar[1].setOnClickListener(navbarListener);
        navbar[2].setOnClickListener(navbarListener);
        navbar[3].setOnClickListener(navbarListener);
        
        navbarLine[0] =(TextView)findViewById(R.id.shirtText);
        navbarLine[1] =(TextView)findViewById(R.id.colorText);
        navbarLine[2] =(TextView)findViewById(R.id.brandText);
        navbarLine[3] =(TextView)findViewById(R.id.priceText);
        unselectColor=getResources().getColor(R.color.unselect);
        selectColor=getResources().getColor(R.color.select);
        
        
        ImageView backButton=(ImageView)findViewById(R.id.back);
        ImageView rightBarButton=(ImageView)findViewById(R.id.rightbar);
        backButton.setOnClickListener(actionBarListener);
        rightBarButton.setOnClickListener(actionBarListener);
        
		Intent intent=this.getIntent();
		Bundle bundle =intent.getExtras();
		photoURI =bundle.getString("data");//圖片來源
		howtogetPhoto=bundle.getString("type");//圖片抓取方式
		photoPath=bundle.getString("path");
		UserBaconMac=bundle.getString("BeaconID");
		UserID=bundle.getString("UserID");
		
		
		colorName=new String[getResources().getInteger(R.integer.colorSum)];
		colorName=getResources().getStringArray(R.array.colorName);
		
		brandName=new String[getResources().getInteger(R.integer.brandSum)];
		brandName=getResources().getStringArray(R.array.brandName);
		PriceName=new String[getResources().getInteger(R.integer.PriceSum)];
		PriceName=getResources().getStringArray(R.array.Price_interval);
		
		int length=getResources().getInteger(R.integer.clothesSum);//抓取clothSum數值 類型個數
		TypedArray images = getResources().obtainTypedArray(R.array.clothes_s_images);//抓取圖片陣列
		drawablekind=new Drawable[length];//重置Drawable陣列
		for(int i=0;i<length;i++){
			drawablekind[i]= images.getDrawable(i);//存入drawable陣列
		}
		runCropImage();
		
	}
	
	@Override
	public void onBackPressed() {
	    // your code.
		new AlertDialog.Builder(CMSetting_Activity.this)
		.setTitle("離開")
		.setMessage("是否要放棄目前的設定？")
		.setPositiveButton("OK", new OnClickListener(){
			public void onClick(DialogInterface DialogInterface,int i ){
				finish();
			}
		})
		.setNegativeButton("Cancel", new OnClickListener(){
			public void onClick(DialogInterface DialogInterface,int i ){
				
			}
		})
		.show();
	}
	public void showDialog(){
		dlg= new Dialog(CMSetting_Activity.this);
		dlg.setTitle("連接Beacon裝置");
		dlg.setContentView(R.layout.connect_layout);
		dlg.setCancelable(false);
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		final EditText BeaconInput= (EditText) dlg.findViewById(R.id.beaconMac);
		if(UserBaconMac!=""){
			
			BeaconInput.setText(UserBaconMac);
			BeaconInput.setEnabled(false);
			BeaconInput.setTextColor(getResources().getColor(R.color.gray));
			
		}
		final CheckBox editBeacon =(CheckBox)dlg.findViewById(R.id.isBeancon_Edit);
		editBeacon.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(editBeacon.isChecked()){
				BeaconInput.setEnabled(true);
				BeaconInput.setTextColor(getResources().getColor(R.color.selectednavbarcolor));
				BeaconInput.requestFocus();
			}else{
				BeaconInput.setEnabled(false);
				BeaconInput.setTextColor(getResources().getColor(R.color.gray));
			}
		}
		
		
		});
		
		ListView upLoaditemView=(ListView)dlg.findViewById(R.id.upload_Listview);
		uploadItem_listview upLoaditemView_Adapter= new uploadItem_listview(CMSetting_Activity.this,this);
		upLoaditemView.setAdapter(upLoaditemView_Adapter);
		Button connectOK=(Button)dlg.findViewById(R.id.connectOK);
		Button connectCancle=(Button)dlg.findViewById(R.id.connectCancel);
		
		connectOK.setOnClickListener(buttonConnect_click);
		connectCancle.setOnClickListener(buttonConnect_click);
		dlg.show();
	}
	
	public void showProgressDialog(){
		Progressdlg= new ProgressDialog(CMSetting_Activity.this);
		Progressdlg.setTitle("上傳中");
		Progressdlg.setMessage("請稍等....");
		Progressdlg.setCancelable(false);
		Progressdlg.setIndeterminate(true);
	
		Progressdlg.show();
	}
	@Override
	 public void onWindowFocusChanged(boolean hasFocus) {//讀取完成後執行的function
	  // TODO Auto-generated method stub
	  super.onWindowFocusChanged(hasFocus);
	  	final Display display = getWindowManager().getDefaultDisplay(); 
	  	screenWidth=display.getWidth();
	  	itemattrWidth=screenWidth/14;
	  	//imageview.setImageBitmap(showPhoto(photoURI,howtogetPhoto));	//設定顯示之圖片（路徑,來源方式）
	  	
	
		
	 }
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private ImageView.OnDragListener imgDragListener = new ImageView.OnDragListener(){

			@Override
			public boolean onDrag(View v, DragEvent dragevent) {
				// TODO Auto-generated method stub
				switch (dragevent.getAction()) {
				
				
				case DragEvent.ACTION_DRAG_ENTERED:
					
					break;
				
				case DragEvent.ACTION_DROP:
					ClipData data = dragevent.getClipData();
					for (int j = 0; j < data.getItemCount(); j++) {
						ClipData.Item item = data.getItemAt(j);
						int Index=Integer.parseInt(item.getText().toString());
						int length=0;
						Drawable[] drawablekind;
						String thisViewId=String.valueOf(v.getId());
						
						if(selectorAdapter.getSort().equals("cloth")){
							length=getResources().getInteger(R.integer.clothesSum);//抓取clothSum數值 類型個數
							TypedArray images = getResources().obtainTypedArray(R.array.clothes_s_images);//抓取圖片陣列
							drawablekind=new Drawable[length];//重置Drawable陣列
							for(int i=0;i<length;i++){
								drawablekind[i]= images.getDrawable(i);//存入drawable陣列
							}
							
							photoCustomer.get(thisViewId).setType(Index);
							photoCloth.get(thisViewId).setImageDrawable(drawablekind[Index]);
						
						}else if(selectorAdapter.getSort().equals("colors")){
							length=getResources().getInteger(R.integer.colorSum);
							TypedArray colorimg = getResources().obtainTypedArray(R.array.color_images);//抓顏色陣列
							drawablekind = new Drawable[length];
							for(int i=0;i<length;i++){
								drawablekind[i]=colorimg.getDrawable(i);
								
							}
							photoColor.get(thisViewId).setImageDrawable(drawablekind[Index]);
							photoCustomer.get(thisViewId).setColor(Index);
							
						}else if(selectorAdapter.getSort().equals("brand")){
							length=getResources().getInteger(R.integer.brandSum);
							TypedArray colorimg = getResources().obtainTypedArray(R.array.brandkind_images);//抓顏色陣列
							drawablekind = new Drawable[length];
							for(int i=0;i<length;i++){
								drawablekind[i]=colorimg.getDrawable(i);
								
							}
							photoBrand.get(thisViewId).setImageDrawable(drawablekind[Index]);
							photoCustomer.get(thisViewId).setBrand(brandName[Index]);
						}else if(selectorAdapter.getSort().equals("price")){
							length=getResources().getInteger(R.integer.PriceSum);
							TypedArray colorimg = getResources().obtainTypedArray(R.array.price_images);//抓顏色陣列
							drawablekind = new Drawable[length];
							for(int i=0;i<length;i++){
								drawablekind[i]=colorimg.getDrawable(i);
								
							}
							photoPrice.get(thisViewId).setImageDrawable(drawablekind[Index]);
							photoCustomer.get(thisViewId).setPrice(Index);;
						}
						
					}
					break;

				default:
					break;
				}
				return true;
			}
	    	
	    };
	    
	private ImageView.OnClickListener navbarListener =new  ImageView.OnClickListener(){

		@Override
		public void onClick(View v) {
			
			for(int i=0;i<4;i++){
				
				navbarLine[i].setBackgroundColor(unselectColor);
				if (v == navbar[i] ){
					navbarLine[i].setBackgroundColor(selectColor);
					selectorAdapter.setKind(kind[i]);
					//adapter.setKind(kind[i]);
					selectorView.setAdapter(selectorAdapter);
			
				}
			}
			
		}
		
	};
	
	private Button.OnClickListener buttonConnect_click =new  Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			Boolean isRightBeaconID=true;
			if(v.getId()==R.id.connectOK){
					EditText BeaconInput= (EditText) dlg.findViewById(R.id.beaconMac);
				
					if(BeaconInput.getText().length()!=17){
						isRightBeaconID=false;
					}
				
				if(isRightBeaconID){

			        mBluetoothManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
			        mBluetoothAdapter = mBluetoothManager.getAdapter();
					findBeacon();
					dlg.cancel();
					//showProgressDialog();
					//Toast toast =Toast.makeText(CMSetting_Activity.this,"Uploading", Toast.LENGTH_LONG);
					//toast.setGravity(Gravity.CENTER, 0, 0);
					//toast.show();
					
					
				}else{
					Toast toast =Toast.makeText(CMSetting_Activity.this,"Please Enter correct Beacon Mac", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					
				}
				
				
			}else if(v.getId()==R.id.connectCancel){
				dlg.cancel();
			}
		}
		
	};
	
	private ImageView.OnClickListener actionBarListener =new  ImageView.OnClickListener(){

		@Override
		public void onClick(View v) {
			
			if(v.getId()==R.id.back){
				new AlertDialog.Builder(CMSetting_Activity.this)
				.setTitle("Exit")
				.setMessage("Sure to abandon this AD？")
				.setPositiveButton("OK", new OnClickListener(){
					public void onClick(DialogInterface DialogInterface,int i ){
						finish();
					}
				})
				.setNegativeButton("Cancel", new OnClickListener(){
					public void onClick(DialogInterface DialogInterface,int i ){
						
					}
				})
				.show();
			}else if(v.getId()==R.id.rightbar){
				showDialog();

			}
	
		}
		
	};
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////
	 public ImageView.OnTouchListener parentTouchListener =new ImageView.OnTouchListener(){
		
		int pointX=0;
		int pointY=0;
		ImageView newview= null;//新增Image物件
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
		
			 
			// TODO Auto-generated method stub
			final int x = (int)event.getX();
			final int y = (int)event.getY();
		   
		    int oldTopMargin=0;
		    int oldLeftMargin=0;
			  switch (event.getAction() & MotionEvent.ACTION_MASK) {
	            case MotionEvent.ACTION_DOWN:
	            	
	            	RelativeLayout.LayoutParams touchParams = new RelativeLayout.LayoutParams(100,100);//新增Cloth物件之顯示資訊（寬,高）
	            	pointX=x;
	            	pointY=y;
	            	
	            	if(pointX>=parentview.getRight()-100 || pointY>=parentview.getBottom()-100 || pointY<=60 ||pointX<=100){
	            		
	            		return true;
	            	
	            	}
	            	
	            	newview = new ImageView(CMSetting_Activity.this);//新增Image物件
					oldTopMargin=y - 50;
					oldLeftMargin=x - 50;
					touchParams.leftMargin =oldLeftMargin;//物件新增位置左右
					touchParams.topMargin = oldTopMargin;//物件新增位置上下
					newview.setLayoutParams(touchParams);//設定Image顯示資訊
				    newview.setImageDrawable(getResources().getDrawable(R.drawable.circle));//設定Image顯示圖片				    
				    newview.setId(viewId);
				    parentview.addView(newview);//新增Image物件到畫面上
				    
	                break;
	            case MotionEvent.ACTION_UP:
	            	//while(newview==null);
	            	
	            	if(pointX>=parentview.getRight()-100 || pointY>=parentview.getBottom()-100 || pointY<=60 ||pointX<=100){
	            		
	            		return true;
	            	
	            	}
	            	newview=(ImageView)findViewById(viewId);
	            	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) newview.getLayoutParams();
	            	photoID.add(String.valueOf(viewId));//將新增之Image ID新增至Map
	            	
	            	if(layoutParams.topMargin<0){
	            		layoutParams.topMargin=0;
	            	}
	            	if(layoutParams.leftMargin<0){
	            		layoutParams.leftMargin=0;
	            	}
	            	if(layoutParams.bottomMargin<0){
	            		layoutParams.bottomMargin=0;
	            	}
	            	if(layoutParams.rightMargin<0){
	            		layoutParams.rightMargin=0;
	            	}
	            	if(layoutParams.height<0){
	            		layoutParams.height=screenWidth;
	            	}
	            	if(layoutParams.width<0){
	            		layoutParams.width=screenWidth;
	            	}
	            	newview.setLayoutParams(layoutParams);
	            	Customer thisCloth =new Customer(String.valueOf(viewId-1));
	            	thisCloth.setAttr(layoutParams.width, layoutParams.height, layoutParams.topMargin, layoutParams.leftMargin);
				    photoView.put(String.valueOf(viewId),newview);//將新增之Image物件新增至Map
				    photoCustomer.put(String.valueOf(viewId), thisCloth);
				    
				    RelativeLayout.LayoutParams clothParams = new RelativeLayout.LayoutParams(itemattrWidth,itemattrWidth);//新增Color物件之顯示資訊（寬,高）
				    clothParams.topMargin=layoutParams.topMargin;//物件新增位置上下 與Cloth同高
				    clothParams.leftMargin=layoutParams.leftMargin;//物件新增位置左右 在Cloth左邊
				    clothParams.rightMargin=-250;
				    ImageView clothview = new ImageView(CMSetting_Activity.this);//新增Color物件
				    clothview.setLayoutParams(clothParams);//設定Color顯示資訊
				    parentview.addView(clothview);//新增Color到換面上
				    photoCloth.put(String.valueOf(viewId),clothview);
				   
				    RelativeLayout.LayoutParams colorParams = new RelativeLayout.LayoutParams(itemattrWidth,itemattrWidth);//新增Color物件之顯示資訊（寬,高）
				    colorParams.topMargin=layoutParams.topMargin+((itemattrWidth+5)*1);//物件新增位置上下 與Cloth同高
				    colorParams.leftMargin=layoutParams.leftMargin;//物件新增位置左右 在Cloth左邊
				    colorParams.rightMargin=-250;
				    ImageView colorview = new ImageView(CMSetting_Activity.this);//新增Color物件
				    //colorview.setBackgroundColor(color[viewId%3]);//設定Color顏色
				    colorview.setLayoutParams(colorParams);//設定Color顯示資訊
				    parentview.addView(colorview);//新增Color到換面上
				    photoColor.put(String.valueOf(viewId),colorview);//將Colorview新增到Map
				    
				    RelativeLayout.LayoutParams brandParams = new RelativeLayout.LayoutParams(itemattrWidth,itemattrWidth);//新增Color物件之顯示資訊（寬,高）
				    brandParams.topMargin=layoutParams.topMargin+((itemattrWidth+5)*2);//物件新增位置上下 與Cloth同高
				    brandParams.leftMargin=layoutParams.leftMargin;//物件新增位置左右 在Cloth左邊
				    brandParams.rightMargin=-250;
				    ImageView brandview = new ImageView(CMSetting_Activity.this);//新增Color物件
				    //colorview.setBackgroundColor(color[viewId%3]);//設定Color顏色
				    brandview.setLayoutParams(brandParams);//設定Color顯0示資訊
				    parentview.addView(brandview);//新增Color到換面上
				    photoBrand.put(String.valueOf(viewId),brandview);//將Colorview新增到Map
				   
				    RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(itemattrWidth,itemattrWidth);//新增Color物件之顯示資訊（寬,高）
				    priceParams.topMargin=layoutParams.topMargin+((itemattrWidth+5)*3);//物件新增位置上下 與Cloth同高
				    priceParams.leftMargin=layoutParams.leftMargin;//物件新增位置左右 在Cloth左邊
				    priceParams.rightMargin=-250;
				    ImageView priceView = new ImageView(CMSetting_Activity.this);//新增Color物件
				    priceView.setLayoutParams(priceParams);//設定Color顯示資訊
				    parentview.addView(priceView);//新增Color到換面上
				    photoPrice.put(String.valueOf(viewId),priceView);//將Colorview新增到Map
				 
				    newview.setOnTouchListener(touchListener);//Image觸碰偵測
		          	newview.setOnDragListener(imgDragListener);
	          
	            	
	            	
	            	viewId++;
	                break;
	            
	            case MotionEvent.ACTION_MOVE:
	            	 if(pointX>=parentview.getRight()-100 || pointY>=parentview.getBottom()-100 || pointY<=60 ||pointX<=100){
		            		
		            		return true;
		            	
		            	}
	            	float destanceNow= FloatMath.sqrt( (event.getX()-pointX)*(event.getX()-pointX) + (event.getY()-pointY)*(event.getY()-pointY));
                    if (destanceNow>=30f && event.getPointerCount() == 1) {
                        
                    			newview=(ImageView)findViewById(viewId);
                    			layoutParams = (RelativeLayout.LayoutParams) newview.getLayoutParams();
                    		
		                        layoutParams.width=(int) (100* destanceNow/30);
		                        layoutParams.height=(int) (100* destanceNow/30);
		                        layoutParams.topMargin= (int)((pointY- 50) - (((100 * destanceNow/30)-100)/2));
		                        layoutParams.leftMargin=(int)((pointX- 50) - (((100 * destanceNow/30)-100)/2));
		                        newview.setLayoutParams(layoutParams);
                        
                    }
	                break;
	        
			  }
			  	
				return true;
		}
		 
		 
	 };
	 public ImageView.OnTouchListener touchListener =new ImageView.OnTouchListener(){
		 	public int xPadding;
		 	public int yPadding;
		    // these matrices will be used to move and zoom image
		 	public Matrix matrix = new Matrix();
		 	public Matrix savedMatrix = new Matrix();
		    // we can be in one of these 3 states
		    private static final int NONE = 0;
		    private static final int DRAG = 1;
		    private static final int ZOOM = 2;
		    private int mode = NONE;
		    // remember some things for zooming
		    private PointF start = new PointF();
		    private PointF mid = new PointF();
		    private float oldDist = 1f;
		    private float d = 0f;
		    private float newRot = 0f;
		    private float[] lastEvent = null;

			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final int X = (int) event.getRawX();
			    final int Y = (int) event.getRawY();
				 ImageView view = (ImageView) v;
				 String thisId =String.valueOf(v.getId());
			        switch (event.getAction() & MotionEvent.ACTION_MASK) {
			            case MotionEvent.ACTION_DOWN:
			            	RelativeLayout.LayoutParams lParams =  (RelativeLayout.LayoutParams) view.getLayoutParams();
				            xPadding = X - lParams.leftMargin;
				            yPadding = Y - lParams.topMargin;
			                savedMatrix.set(matrix);
			                start.set(event.getX(), event.getY());
			                mode = DRAG;
			                lastEvent = null;
			               // Log.d("mylog","imgTouch");
			                break;
			            case MotionEvent.ACTION_POINTER_DOWN:
			            	
			            	
			                oldDist = spacing(event);
			                if (oldDist > 10f) {
			                    savedMatrix.set(matrix);
			                    midPoint(mid, event);
			                    mode = ZOOM;
			                }
			                lastEvent = new float[4];
			                lastEvent[0] = event.getX(0);
			                lastEvent[1] = event.getX(1);
			                lastEvent[2] = event.getY(0);
			                lastEvent[3] = event.getY(1);
			               
			                break;
			            case MotionEvent.ACTION_UP:
			            	int trashLeft    = trashview.getLeft() + trashview.getWidth()/2;
			                int trashTop     = trashview.getTop()  + trashview.getHeight()/2;
			                RelativeLayout.LayoutParams viewlayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			                int targetRight  = v.getLeft() +viewlayoutParams.width*2/3;
			                int targetBottom = v.getTop() + viewlayoutParams.height*2/3;
			                
			                if (targetRight > trashLeft && targetBottom > trashTop) {
			                	String thisViewId =String.valueOf(v.getId());
			                	
			                	photoID.remove(thisViewId);
			                	parentview.removeView(v);
			                	
			                	parentview.removeView(photoCloth.get(thisViewId));
			                	parentview.removeView(photoColor.get(thisViewId));
			                	parentview.removeView(photoBrand.get(thisViewId));
			                	parentview.removeView(photoPrice.get(thisViewId));
			                	photoView.remove(thisViewId);
			                	photoCloth.remove(thisViewId);
			                	photoColor.remove(thisViewId);
			                	photoBrand.remove(thisViewId);
			                	photoPrice.remove(thisViewId);
			                	  return true;
			                }else{
			                	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			                	if(layoutParams.topMargin<0) layoutParams.topMargin=0;
				            	if(layoutParams.leftMargin<0)layoutParams.leftMargin=0;
				            	if(layoutParams.topMargin>screenWidth-layoutParams.height) layoutParams.topMargin=screenWidth-layoutParams.height;
				            	if(layoutParams.leftMargin>screenWidth-layoutParams.width) layoutParams.leftMargin=screenWidth-layoutParams.width;
				            	if(layoutParams.height<0) layoutParams.height=screenWidth;
				            	if(layoutParams.width<0)layoutParams.width=screenWidth;
				            	
				            	view.setLayoutParams(layoutParams);
				            	  
			                }
			                break;
			            case MotionEvent.ACTION_POINTER_UP:
			                mode = NONE;
			                lastEvent = null;
			                break;
			            case MotionEvent.ACTION_MOVE:
			                if (mode == DRAG) {
			                	
			                	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			                	
				                	layoutParams.leftMargin = X - xPadding;
						            layoutParams.topMargin = Y - yPadding;
						            layoutParams.rightMargin = -250;
						            layoutParams.bottomMargin = -250;
			                
					            view.setLayoutParams(layoutParams);
			                    
			                } else if (mode == ZOOM) {
			                    float newDist = spacing(event);
			                    if (newDist > 30f && event.getPointerCount() == 2) {
			                        float scale = (newDist / oldDist);
			                   
			                        
			                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			                        if(scale>=1){
			                        	if(layoutParams.width< screenWidth && layoutParams.height< screenWidth){
					                        layoutParams.width=(int) (layoutParams.width+ scale);
					                        layoutParams.height=(int) (layoutParams.height +  scale);
					                        layoutParams.topMargin= (int)(layoutParams.topMargin-scale/2);
					                        layoutParams.leftMargin=(int)( layoutParams.leftMargin-scale/2);
					                        view.setLayoutParams(layoutParams);
			                        	}
			                       }else if(scale<1 && layoutParams.width>=150 && layoutParams.height>=150){
			                    	   layoutParams.width=(int) (layoutParams.width- scale);
			                    	   layoutParams.height=(int) (layoutParams.height - scale);
			                    	   layoutParams.topMargin=(int)( layoutParams.topMargin+scale/2);
				                        layoutParams.leftMargin=(int)( layoutParams.leftMargin+scale/2);
			                    	   view.setLayoutParams(layoutParams);
			                    	   
			                       }
			                      
			                        
			                    }
			                
			                }
			                break;
			        }

			        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
			        
			        RelativeLayout.LayoutParams clothParams = (RelativeLayout.LayoutParams) (photoCloth.get(thisId)).getLayoutParams();
			        clothParams.leftMargin=layoutParams.leftMargin; //變更顏色物件位置
			        clothParams.topMargin=layoutParams.topMargin;//變更顏色物件位置
			        photoCloth.get(thisId).setLayoutParams(clothParams);//變更顏色物件位置
			        
			        RelativeLayout.LayoutParams colorParams = (RelativeLayout.LayoutParams) (photoColor.get(thisId)).getLayoutParams();
			        colorParams.leftMargin=layoutParams.leftMargin; //變更顏色物件位置
			        colorParams.topMargin=layoutParams.topMargin+((itemattrWidth+5)*1);//變更顏色物件位置
			        photoColor.get(thisId).setLayoutParams(colorParams);//變更顏色物件位置
			        
			        RelativeLayout.LayoutParams brandParams = (RelativeLayout.LayoutParams) (photoBrand.get(thisId)).getLayoutParams();
			        brandParams.leftMargin=layoutParams.leftMargin; //變更顏色物件位置
			        brandParams.topMargin=layoutParams.topMargin+((itemattrWidth+5)*2);//變更顏色物件位置
			        photoBrand.get(thisId).setLayoutParams(brandParams);//變更顏色物件位置
			        
			        RelativeLayout.LayoutParams catalogParams = (RelativeLayout.LayoutParams) (photoPrice.get(thisId)).getLayoutParams();
			        catalogParams.leftMargin=layoutParams.leftMargin; //變更顏色物件位置
			        catalogParams.topMargin=layoutParams.topMargin+((itemattrWidth+5)*3);//變更顏色物件位置
			        photoPrice.get(thisId).setLayoutParams(catalogParams);//變更顏色物件位置
			        Customer thisCloth =photoCustomer.get(thisId);
			        thisCloth.setAttr(layoutParams.width, layoutParams.height, layoutParams.topMargin, layoutParams.leftMargin);
			        photoCustomer.put(thisId,thisCloth);//update變更後的物件資訊
			        photoView.put(thisId,view);//update變更後的物件資訊
			      
			        return true;
			}
	    	
	    	
	    };

	
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		private int Size=0;
		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			
			super.onPreExecute();
		}
		public UploadFileToServer(int size){
			this.Size=size;
		}
		

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			if (uploadBMP == null)
				return null;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if(Size==0){
				if(uploadBMP.getWidth()>300){
					
	        		float press= (1f/((float)(uploadBMP.getWidth()/300)))*100f;
	        		  Log.d("mylog", "press :"+press);
	        		  uploadBMP.compress(Bitmap.CompressFormat.JPEG, (int)press, stream);
	        		
	        	}else{
	        		  uploadBMP.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        	}	
			}else{
				if(uploadSmallBMP.getWidth()>100){
					float press= (1f/((float)(uploadBMP.getWidth()/100)))*100f;
	      		  	Log.d("mylog", "press :"+press);
	      		  uploadSmallBMP.compress(Bitmap.CompressFormat.JPEG, (int)press, stream);
				}
				else{
					uploadSmallBMP.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					
				}
			}
			
			
			InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream
			
			
			String responseString = null;
			InputStream out=null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://cmapp.nado.tw/android_connect_user/uploadimg.php");

			try {
				MultipartEntity reqEntity = new MultipartEntity();
				if(Size==0){
					reqEntity.addPart("image",
							BeaconInput, in);
				}else{
					reqEntity.addPart("image",
							BeaconInput+"@", in);
				}
				
				
				httppost.setEntity(reqEntity);
				
				File sourceFile = new File(photoPath);

			
				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
				if(Progressdlg!=null)
				Progressdlg.cancel();
			} catch (IOException e) {
				responseString = e.toString();
				if(Progressdlg!=null)
				Progressdlg.cancel();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("mylog", "Response from server: " + result);
			//Toast toast =Toast.makeText(CMSetting_Activity.this,"圖片上傳成功"+"\n"+"Response from server: " + result, Toast.LENGTH_LONG);
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
			// showing the server response in an alert dialog
			if(Size==0){
				new UploadFileToServer(1).execute();
			}else{
				showBranditemDialog();
				if(Progressdlg!=null){
					Progressdlg.cancel();
				}
				if(prgdlg!=null)
					prgdlg.cancel();
			}
			
			//showDetail();
			super.onPostExecute(result);
			
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	
	private void runCropImage() {

	    // create explicit intent
	    Intent intent = new Intent(this, CropImage.class);
	    if(howtogetPhoto.equals("Gallery")){
		    String[] projection = { MediaColumns.DATA };
		    Uri mPictureUri= Uri.parse(photoURI);
	  		Cursor cursor = managedQuery(mPictureUri, projection, null, null,
	  				null);
	  		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	  		cursor.moveToFirst();
	
	  		String selectedImagePath = cursor.getString(column_index);
	  		photoPath=selectedImagePath;
  		}
	    String filePath = photoPath;
	    
	    intent.putExtra(CropImage.IMAGE_PATH, filePath);
	    intent.putExtra(RETURN_DATA, true);
	    intent.putExtra(CropImage.SCALE, true);

	    intent.putExtra(CropImage.ASPECT_X, 5);
	    intent.putExtra(CropImage.ASPECT_Y, 5);

	    startActivityForResult(intent, PIC_CROP);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (resultCode != RESULT_OK) {
	    	finish();
	        return;
	    }  

	    switch (requestCode) {
	    	
	        case PIC_CROP:
	        	
	        	if(data.getExtras()!=null ){
	        		
		        		String path = data.getStringExtra(CropImage.IMAGE_PATH);
	
			            if (path == null) {
	
			                return;
			            }
			            try {
			                // Decode image size
			            	// Uri uri = Uri.parse(path);
			                 //InputStream in = null;
			                 //in = getContentResolver().openInputStream(uri);
			                 File imgFile = new  File(path);
			               
			                BitmapFactory.Options o = new BitmapFactory.Options();
			                o.inJustDecodeBounds = true;
			                BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o);

			                // The new size we want to scale to
			                final int REQUIRED_SIZE=250,REQUIRED_SMALL_SIZE=100;

			                // Find the correct scale value. It should be the power of 2.
			                int scale = 1;
			                while(o.outWidth / scale / 2 >= REQUIRED_SIZE && 
			                      o.outHeight / scale / 2 >= REQUIRED_SIZE) {
			                    scale *= 2;
			                }
			                

			                // Decode with inSampleSize
			                BitmapFactory.Options o2 = new BitmapFactory.Options();
			                o2.inSampleSize = scale;
			              //  in =  getContentResolver().openInputStream(uri);
			                uploadBMP = BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o2);
			                while(o.outWidth / scale / 2 >= REQUIRED_SMALL_SIZE && 
				                      o.outHeight / scale / 2 >= REQUIRED_SMALL_SIZE) {
				                    scale *= 2;
				                }
			                BitmapFactory.Options o3 = new BitmapFactory.Options();
			                o3.inSampleSize = scale;
			                uploadSmallBMP = BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o3);
			                Log.d("mylog","set img");
			                imageview.setImageBitmap(uploadBMP);
			            } catch (Exception e) {
			            	 Bitmap bitmap = BitmapFactory.decodeFile(path);
					         uploadBMP=bitmap;
					         uploadSmallBMP=bitmap;
					         imageview.setImageBitmap(uploadBMP);
			            }
			        //   
			            
	        		
		            break;
	        	}
	            break;
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	 
	}	
	
	
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

   
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

   
    class EnterBeaconID extends AsyncTask<String, String, String> {

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
			
			
			JSONObject UploadObject = new JSONObject();
			
			JSONArray ItemJsonarray = new JSONArray();
			Display display = getWindowManager().getDefaultDisplay();
			int display_width=display.getWidth();
			DecimalFormat formatter = new DecimalFormat("0.000");
			Log.d("mylog", String.valueOf(display.getWidth()));
			for (String id : photoID){
				JSONObject ItemObject = new JSONObject();
				try {
					Customer thisCloth =photoCustomer.get(id);
					ItemObject.put("ItemID", id);
					ItemObject.put("Width", formatter.format((float)((float)thisCloth.itemWidth/(float)display_width)));
					ItemObject.put("Height", formatter.format((float)((float)thisCloth.itemHeight/(float)display_width)));
					ItemObject.put("Top", formatter.format((float)((float)thisCloth.itemTopMargin/(float)display_width)));
					ItemObject.put("Left", formatter.format((float)((float)thisCloth.itemLeftMargin/(float)display_width)));
					ItemObject.put("Type", thisCloth.itemType);
					ItemObject.put("Color", thisCloth.itemColor);
					ItemObject.put("Brand", thisCloth.itemBrand);
					ItemObject.put("Price",thisCloth.itemPrice);
					ItemJsonarray.put(ItemObject);
					Log.d("mylog", "ItemObject.put");
				} catch (JSONException e) {
					e.printStackTrace();
					Log.d("mylog", "error3");
					if(Progressdlg!=null)
					Progressdlg.cancel();
				}
			}	
			Log.d("mylog", "EditText");

			
			EditText BeaconInput1 = (EditText) dlg.findViewById(R.id.beaconMac);
			
			BeaconInput = String.valueOf(BeaconInput1.getText());
			try {
				UploadObject.put("BeaconID", BeaconInput);
				UploadObject.put("Item", ItemJsonarray);
				UploadObject.put("UUID", buffer);
				UploadObject.put("UserID", UserID);
				Log.d("mylog", "UploadObject.put");
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d("mylog", "error2");
				if(Progressdlg!=null)
				Progressdlg.cancel();
			}
			

			
			ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
			

			jsonarray.add(new BasicNameValuePair("Upload", UploadObject.toString()));// 重要！！
			Log.d("mylog",UploadObject.toString() );
			try {
				// Note that create product url accepts POST method

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/upload_item.php");
				httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
				HttpResponse httpResponse = httpClient.execute(httpPost); 
				HttpEntity httpEntity = httpResponse.getEntity();
				
				try {
					String json = EntityUtils.toString(httpEntity);
					UploadSuccessJsonobject = new JSONObject(json);
					Log.d("mylog", json);
					
				} catch (JSONException ex) {
					Log.d("mylog", "error1");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("mylog", "error");
				if(Progressdlg!=null)
				Progressdlg.cancel();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			
			try {

				String echoSuccess = UploadSuccessJsonobject.getString("Success");
				Log.d("mylog", "Success:"+echoSuccess);
				if(echoSuccess.equals("1")){
					
					
					JSONArray Branditem_array = UploadSuccessJsonobject.getJSONArray("BrandItem");
					JSONObject PartItemjsonobject = new JSONObject(); 
					String itmeid="";
					thisADID=UploadSuccessJsonobject.getString("AdID");
					Customer this_brandItem;
					for(int i=0;i<Branditem_array.length();i++){
						PartItemjsonobject=Branditem_array.getJSONObject(i);
						itmeid=PartItemjsonobject.getString("ItemID");
						String itmeindex=PartItemjsonobject.getString("ItemIndex");
						branditemIndex.put(itmeid, itmeindex);
						branditemID.add(itmeid);
						JSONArray Branditems_array = PartItemjsonobject.getJSONArray("Item");
						JSONObject PartItems_jsonobject = new JSONObject();
						brandItems = new ArrayList<Customer>();
						for(int j=0;j<Branditems_array.length();j++){
							PartItems_jsonobject=Branditems_array.getJSONObject(j);
							String BrandItemID=PartItems_jsonobject.getString("id");
							String BrandItemName=PartItems_jsonobject.getString("name");
							String BrandItemBrand=PartItems_jsonobject.getString("brand");
							int BrandItemPrice=PartItems_jsonobject.getInt("price");
							Log.d("mylog",BrandItemID+BrandItemName+BrandItemBrand+BrandItemPrice);
							this_brandItem =new Customer(BrandItemID, BrandItemName, BrandItemPrice,BrandItemBrand);
							
							brandItems.add(this_brandItem);
						}
						brandArrayList.put(itmeid, brandItems);
					}
					;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(Progressdlg!=null)
				Progressdlg.cancel();
				
			}
			
			new UploadFileToServer(0).execute();
			
			super.onPostExecute(file_url);
		}

	}
    
    public class uploadItem_listview extends BaseAdapter {
    	
    	
    	Activity myactivity;//主要呼叫的Activity
    	private LayoutInflater myInflater;
    	private Context mycontext;
    	private int length=10;//預設長度10
    	
    	public uploadItem_listview(Activity activity,Context c){
    		myInflater =LayoutInflater.from(c);
    		myactivity=activity;
    		mycontext=c;
    		
			
			
    		
    	}
    	
    	
    	@Override
    	public int getCount() {
    		return photoID.size();
    		
    	}

    	@Override
    	public Object getItem(int position) {
    		
    		return null;
    	}

    	@Override
    	public long getItemId(int position) {
    		return position;
    	}

    	@Override
    	public View getView(final int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		convertView =myInflater.inflate(R.layout.uploaditem_listlayout, null);//設定Layout
    		ImageView img =(ImageView)convertView.findViewById(R.id.itemtype_img);
    		
    		TextView txtColor=(TextView)convertView.findViewById(R.id.itemcolor_txt);
    		TextView txtBrand=(TextView)convertView.findViewById(R.id.itembrand_txt);
    		TextView txtPrice=(TextView)convertView.findViewById(R.id.itemprice_txt);
    		//final CheckBox isOftenUse=(CheckBox)convertView.findViewById(R.id.isItem_oftenUse);
    		final Customer thisCloth=photoCustomer.get(photoID.get(position));
    		img.setImageDrawable(drawablekind[thisCloth.itemType]);
    		
    		txtColor.setText(colorName[thisCloth.itemColor]);
    		txtBrand.setText(thisCloth.itemBrand);
    		if(thisCloth.itemPrice==6){
    			txtPrice.setText(PriceName[thisCloth.itemPrice]+" up");
    		}else{
    			txtPrice.setText(PriceName[thisCloth.itemPrice]+"~"+PriceName[thisCloth.itemPrice+1]);
    		}
    		
    		
    		/*isOftenUse.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener(){

    		@Override
    		public void onCheckedChanged(CompoundButton buttonView,
    				boolean isChecked) {
    			if(isOftenUse.isChecked()){
    				thisCloth.setIsOftenUse(1);
    			}else{
    				thisCloth.setIsOftenUse(0);
    			}
    		}
    		
    		
    		});*/
    		
    		return convertView;

    	}

    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showBranditemDialog(){
    	if(branditemID.size()==0){
    		
    		if(branddlg!=null){
    			branddlg.cancel();
    		}
    		
    		//Toast toast =Toast.makeText(CMSetting_Activity.this,"Upload Scucess", Toast.LENGTH_LONG);
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
    		 if(prgdlg!=null ){
 				prgdlg.cancel();
 			}
			finish();
    		return;
    	}
    	pageNow=0;
		branddlg= new Dialog(CMSetting_Activity.this);
		branddlg.setTitle("Match to product.");
		branddlg.setContentView(R.layout.branditem_dialog);
		branddlg.setCancelable(false);
		branddlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		branditems_View=(ListView)branddlg.findViewById(R.id.branditem_Listview);
		BranditemView_Adapter= new brandItems_listview(CMSetting_Activity.this,this,branditemID.get(pageNow));
		branditems_View.setAdapter(BranditemView_Adapter);
		branditems_View.setOnItemClickListener(lvListener);
		
		ImageView img =(ImageView)branddlg.findViewById(R.id.branditemtype_img);
		
		TextView txtColor=(TextView)branddlg.findViewById(R.id.branditemcolor_txt);
		TextView txtBrand=(TextView)branddlg.findViewById(R.id.branditembrand_txt);
		TextView txtPrice=(TextView)branddlg.findViewById(R.id.branditemprice_txt);
		
		Customer thisCloth=photoCustomer.get(branditemID.get(pageNow));
		img.setImageDrawable(drawablekind[thisCloth.itemType]);
		
		txtColor.setText(colorName[thisCloth.itemColor]);
		txtBrand.setText(thisCloth.itemBrand);
		if(thisCloth.itemPrice==6){
			txtPrice.setText(PriceName[thisCloth.itemPrice]+" up");
		}else{
			txtPrice.setText(PriceName[thisCloth.itemPrice]+"~"+PriceName[thisCloth.itemPrice+1]);
		}
		final Button BrandItemOK=(Button)branddlg.findViewById(R.id.BranditemOK);
		Button BrandItemSKIP=(Button)branddlg.findViewById(R.id.branditemSkip);
		if(pageNow>=branditemID.size()-2){
			BrandItemOK.setText("OK");
		}else{
			BrandItemOK.setText("NEXT");
		}
		BrandItemOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pageNow>=branditemID.size()-2){
					BrandItemOK.setText("OK");
				}else{
					BrandItemOK.setText("NEXT");
				}
				if(pageNow>=branditemID.size()-1){
					new isBrandItem().execute();
					showprogressDialog("searching");
					branddlg.cancel();
				}else{
					pageNow++;
					ImageView img =(ImageView)branddlg.findViewById(R.id.branditemtype_img);
					
					TextView txtColor=(TextView)branddlg.findViewById(R.id.branditemcolor_txt);
					TextView txtBrand=(TextView)branddlg.findViewById(R.id.branditembrand_txt);
					TextView txtPrice=(TextView)branddlg.findViewById(R.id.branditemprice_txt);
					Customer thisCloth=photoCustomer.get(branditemID.get(pageNow));
					img.setImageDrawable(drawablekind[thisCloth.itemType]);
					
					txtColor.setText(colorName[thisCloth.itemColor]);
					txtBrand.setText(thisCloth.itemBrand);
					if(thisCloth.itemPrice==6){
						txtPrice.setText(PriceName[thisCloth.itemPrice]+" up");
					}else{
						txtPrice.setText(PriceName[thisCloth.itemPrice]+"~"+PriceName[thisCloth.itemPrice+1]);
					}
					BranditemView_Adapter.setphotoID(branditemID.get(pageNow));
					
					branditems_View.setAdapter(BranditemView_Adapter);
					branditems_View.setOnItemClickListener(lvListener);
				}
			}
		});
		BrandItemSKIP.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast toast =Toast.makeText(CMSetting_Activity.this,"Upload Scucess", Toast.LENGTH_LONG);
				//toast.setGravity(Gravity.CENTER, 0, 0);
				//toast.show();
				 if(prgdlg!=null ){
     				prgdlg.cancel();
     			}
					branddlg.cancel();
					
					finish();
			}
		});
		branddlg.show();
	}
    
    private ListView.OnItemClickListener lvListener =new ListView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			isBrandItem.put(String.valueOf(branditemID.get(pageNow)) ,String.valueOf(position));
			
			Log.d("mylog",branditemID.get(pageNow)+ " " + String.valueOf(position));
			//lvText.setText(((TextView)view).getText().toString());
			BranditemView_Adapter.notifyDataSetChanged();
		}

		
	};
    
  
    /////////////////////////////
    public class brandItems_listview extends BaseAdapter {
    	
    	
    	Activity myactivity;//主要呼叫的Activity
    	private LayoutInflater myInflater;
    	private Context mycontext;
    	private int length=10;//預設長度10
    	private String itemid="";
    	
    	public brandItems_listview(Activity activity,Context c,String itemid){
    		myInflater =LayoutInflater.from(c);
    		myactivity=activity;
    		mycontext=c;
    		this.itemid=itemid;
    		for(int i=0;i<branditemID.size();i++){
				
				isBrandItem.put(String.valueOf(branditemID.get(i)) ,"0");
			}
    	}
    	
    	public void setphotoID(String itemid){
    		this.itemid=itemid;
    	}
    	@Override
    	public int getCount() {
    		ArrayList<Customer> branditems=brandArrayList.get(itemid);
    		if(branditems!=null){
    			length=branditems.size();
    			return length+1;
    		}else{
    			return 1;
    		}
    		
    		
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
    		convertView =myInflater.inflate(R.layout.branditem_listview_items,null);//設定Layout
    		ImageView img =(ImageView)convertView.findViewById(R.id.branditem_img);
    		RelativeLayout branditem_main_listview=(RelativeLayout)convertView.findViewById(R.id.branditem_main_listview);
    		TextView txtName=(TextView)convertView.findViewById(R.id.branditem_name_txt);
    		TextView txtPrice=(TextView)convertView.findViewById(R.id.branditem_price_txt);
    		img.setImageResource(R.drawable.loading);
    		if(isBrandItem.get(itemid).equals(String.valueOf(position))){
    			branditem_main_listview.setBackgroundColor(getResources().getColor(R.color.select));
    		}else{
    			branditem_main_listview.setBackgroundColor(getResources().getColor(R.color.white));
    			
    		}
    		if(position==0){
    			img.setImageResource(R.drawable.nomatch);
    			txtName.setText("Not belong to any products.");
    		}else{
    			ArrayList<Customer> thisBrandItem =brandArrayList.get(itemid);
        		final Customer thisCloth=thisBrandItem.get(position-1);
        		Log.d("mylog", thisCloth.BrandItemID);
        		if(branditemBitmap.get(thisCloth.BrandItemID)==null){
        			imgDownLoader id =new imgDownLoader(img,thisCloth.BrandItemID,thisCloth.itemBrand);
 					id.execute(thisCloth.BrandItemID);
        		}else{
        			img.setImageBitmap(branditemBitmap.get(thisCloth.BrandItemID));
        		}
        		
        		
        		
        		txtName.setText(thisCloth.BrandItemName);
        		txtPrice.setText("$"+thisCloth.itemPrice);
        		
        		
    		}
    		
    		
    		return convertView;

    	}
    	
    	public class imgDownLoader extends AsyncTask<String, Integer, Bitmap> {
			
			private ImageView imgView;
			private String BrandItemID,Brand;
			
			
			 public imgDownLoader(ImageView imgView,String BrandItemID,String Brand) {
			        super();
			        this.imgView=imgView;
			        this.BrandItemID=BrandItemID;
			        this.Brand=Brand;
			        
			    }
			protected void onPreExecute() {
			}
			@Override
			protected Bitmap doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				try {
					
						Bitmap outBitmap;
						URL url =new URL("http://cmapp.nado.tw/uniqlo/"+BrandItemID+".jpeg");
						if(Brand.equals("UNIQLO")){
							url=new URL("http://cmapp.nado.tw/uniqlo/"+BrandItemID+".jpeg");
						}else if(Brand.equals("MUJI")){
							url=new URL("http://cmapp.nado.tw/muji/"+BrandItemID+".jpg");
						}else{
							return null;
						}
						
						HttpURLConnection httpCon =(HttpURLConnection) url.openConnection();
						httpCon.connect();
						if(httpCon.getResponseCode()!=200){
							throw new Exception("Failed to Connect!");
						}
						InputStream is =httpCon.getInputStream();
						outBitmap = BitmapFactory.decodeStream(is);   
					
					
					
					
					return outBitmap;
					
				} catch (Exception e) {
					Log.d("mylog","Faild to load Img!"+BrandItemID);
					// TODO: handle exception
				}
				return null;
			}
			 @Override
			    protected void onProgressUpdate(Integer... progress) {
			        //progressDialog_.incrementProgressBy(progress[0]);
			    }
			
			protected void onPostExecute(Bitmap img) {
				if(img !=null){
					branditemBitmap.put(BrandItemID,img);
					imgView.setImageBitmap(img);
				}
					
				
			
				
			}
			
		
    	}
    }
    public class isBrandItem extends AsyncTask<String, Integer, String> {
		
		private ImageView imgView;
		private String BrandItemID,Brand;
		
		
		
		protected void onPreExecute() {
		}
		@Override
		protected String doInBackground(String... params) {
			JSONObject UploadObject = new JSONObject();
			
			JSONArray ItemJsonarray = new JSONArray();
			
			for (String id : branditemID){
				JSONObject ItemObject = new JSONObject();
				if(!isBrandItem.get(id).equals("0")){
					try {
						
						ItemObject.put("UserID", UserID);
						ItemObject.put("AdID", thisADID);
						ItemObject.put("ItemIndex", branditemIndex.get(id));
						ArrayList<Customer> thisCustomer = brandArrayList.get(id);
						Customer thisBrandItem = thisCustomer.get( Integer.parseInt(isBrandItem.get(id))-1);
						ItemObject.put("BrandItem", thisBrandItem.BrandItemID);
						
						ItemJsonarray.put(ItemObject);
						
					} catch (JSONException e) {
						e.printStackTrace();
						Log.d("mylog", "error3");
						if(Progressdlg!=null)
						Progressdlg.cancel();
					}
				}
				
			}	
		
			try {
				
				UploadObject.put("Item", ItemJsonarray);
				Log.d("mylog",UploadObject.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d("mylog", "error2");
				if(Progressdlg!=null)
				Progressdlg.cancel();
			}
			

			
			ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
			

			jsonarray.add(new BasicNameValuePair("isBrandItem", UploadObject.toString()));// 重要！！
			try {
				// Note that create product url accepts POST method

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/isBrandItem.php");
				httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
				HttpResponse httpResponse = httpClient.execute(httpPost); 
				HttpEntity httpEntity = httpResponse.getEntity();
				
				
					String json = EntityUtils.toString(httpEntity);
					//UploadSuccessJsonobject = new JSONObject(json);
					Log.d("mylog", json);
					return "";
					
					
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("mylog", "error");
				if(Progressdlg!=null)
				Progressdlg.cancel();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			//Toast toast =Toast.makeText(CMSetting_Activity.this,"Upload Scucess", Toast.LENGTH_LONG);
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
			 if(prgdlg!=null ){
 				prgdlg.cancel();
 			}
			finish();
		}

	
	}
    
    public void findBeacon(){
		stopService(new Intent(CMSetting_Activity.this, getBluetoothAround.class));
		Log.d("mylog", "findBeacon");

			if(mBluetoothAdapter==null){
				Log.d("mylog", "NO Bluetooth Can Use!");
				finish();
			}else{
				
				if(!mBluetoothAdapter.isEnabled()){
					
					new AlertDialog.Builder(CMSetting_Activity.this)
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
				}
			}
		 mHandler = new Handler() {
	            @Override
	            public void handleMessage(Message msg) {
	                mStatusText.setText(((BleStatus) msg.obj).name());
	            }
	        };
	      
	        showprogressDialog("searching");
	        connect();
	        
	}
	 private void connect() {
		 	mBluetoothAdapter.stopLeScan(this);
	        mBluetoothAdapter.startLeScan(this);
	        mHandler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	//showMAC();
	            	if(!findbeacon){
	            		Toast.makeText(CMSetting_Activity.this, "Please Turn On or Restart Your Beacon !", Toast.LENGTH_SHORT).show();
	            		if(prgdlg!=null ){
	        				prgdlg.cancel();
	        			}
	            		mBluetoothAdapter.stopLeScan(CMSetting_Activity.this);
	            		if(Progressdlg!=null){
	            			Progressdlg.cancel();
	            		}
	            	}
	            		
	            	
	            	//傳出BeaconID收回Item資訊
	                
	                if (BleStatus.SCANNING.equals(mStatus)) {
	                    setStatus(BleStatus.SCAN_FAILED);
	                }
	            }
	        }, SCAN_PERIOD);
	        
	      
	     
	        
	        //setStatus(BleStatus.SCANNING);
	    }
	 
	 @Override
	    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
	        Log.d(TAG, "device found: " + device.getName());
	        if(device.getAddress().equals(UserBaconMac)){
	        	 if(mBluetoothGatt!=null){
	        		 mBluetoothGatt.disconnect();
	        	 }
	        	
	        	 mBluetoothAdapter.stopLeScan(this);
	        	
	 	            mBluetoothGatt = device.connectGatt(this, false, mBluetoothGattCallback);
	 	          
	 	     
	        }
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
	                   // Toast.makeText(CMSetting_Activity.this, "Success", Toast.LENGTH_SHORT).show();
	                   
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
	                    	//Toast.makeText(CMSetting_Activity.this, "Please Turn On or Restart Your Beacon !", Toast.LENGTH_SHORT).show();
	                    	/*if(prgdlg!=null ){
	            				prgdlg.cancel();
	            			}*/
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
	            	
	            	 buffer = "000"+photoID.size();
	            	for(int i=0;i<7;i++){
	            		if(i+1>photoID.size()){
	            			
	            			for(int j=0;j<=6-i;j++){
	            				buffer+="0000";
	            			}
	            			break;
	            		}
	            		String id=photoID.get(i);
	            		Customer thisItem =photoCustomer.get(id);
	            		buffer+= String.valueOf(thisItem.itemType);
	            		int color=thisItem.itemColor;
	            		if(color<10){
	            			buffer+="0";
	            			buffer+=String.valueOf(color);
	            		}else{
	            			buffer+=String.valueOf(color);
	            		}
	            		buffer+= String.valueOf(thisItem.itemPrice);
	            		
	            	}
	                
	                
	                /*if(!buffer.matches("[\\da-fA-F]+")){
	                    buffer = "";
	                }*/
	                characteristic.setValue(HexStringToBytes(buffer));
	                mBluetoothGatt.writeCharacteristic(characteristic);
	               
	                findbeacon=true;
	               upload();
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
	    private void upload(){
	    	 new EnterBeaconID().execute();

				
				
	    }
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
	    public void showprogressDialog(String status){
			if(prgdlg!=null ){
				prgdlg.cancel();
			}
			
			
			prgdlg= new Dialog(CMSetting_Activity.this);
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
}
