package com.ntust.cmapp;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ntust.cmapp.CMRegister_Activity.LogInCheck;
import com.ntust.cmapp.CMSetting_Activity.brandItems_listview;
import com.ntust.cmapp.CMSetting_Activity.isBrandItem;
import com.ntust.cmapp.CMSetting_Activity.brandItems_listview.imgDownLoader;
import com.ntust.cmapp.cmappTest_Activity.CMAdapeter;

import android.R.bool;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class CMBrowse_Fragment extends Fragment implements Serializable ,OnScrollListener{
	   Map<String,String> customerId = new HashMap<String,String>(); //建立一個Map介面為CustomerId
	    //服飾Item
	  //  Map<String,String> itemId = new HashMap<String,String>();
	    ArrayList<String> customerItemId = new ArrayList<String>();
	    Map<String,ArrayList<String>> customerItemListMap = new HashMap<String,ArrayList<String>>();
	    Map<String, Customer> itemInfo = new HashMap<String,Customer>();
	    public Map<String, Customer> customerInfo = new HashMap<String,Customer>();
	    Map<String,Bitmap> customerphoto = new HashMap<String,Bitmap>();
	    Map<String,Bitmap> Download_customerphoto = new HashMap<String,Bitmap>();
	    Map<String,Bitmap> Download_brandAD = new HashMap<String,Bitmap>();
	    public ArrayList<String> MessageText_Array=new ArrayList<String>();	
		public ArrayList<String> MessageID_Array=new ArrayList<String>();	
		public ArrayList<String> MessageName_Array=new ArrayList<String>();	
	    private String thisUserID="";
	    private ListView mListView;
	    private boolean isSearch=false,refreshNow=false;
	    int screen_width,mLastFirstVisibleItem,mLastVisibleItemCount;
	    ListView message_View;
		message_listview messageView_Adapter;
	    int UserID = 0;
	    Dialog coupon_dlg,messagedlg;
	    ImageView mimgcoupon,mimgforsale ;
		TextView mtxtcoupon ;
	    Activity mainActivity;
	    Boolean scrolldown=false,isLoad=false;;
	    String[] Brand; //品牌
	    String[] Type;//種類(上身/下著等)
	    String[] Color;//顏色(十六進位)
	    String[] Price; //
	    String[] Sex={"MEN","WOMEN"};
	    Drawable[] drawableType,drawableTypes;
	    CMAdapeter adapter;
	    String customerPhotoID;
	    String CustomerItemId;
	    RelativeLayout firstnav ;
	    ListView mainlistview; 
	    boolean isNoNewADer=false;
	    private SwipeRefreshLayout mSwipeRefreshLayout; 
	    public CMBrowse_Fragment(Boolean isSearch){
	    	this.isSearch=isSearch;
	    }
	    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
			View view =inflater.inflate(R.layout.cmapptest_layout,container,false);//設定Layout
			return view;
		}

		@Override
		
	 public void onActivityCreated(Bundle savedInstanceState){
		
		super.onActivityCreated(savedInstanceState);
		try {
			
			Bundle args = getArguments();
			//int iPosition = args.getInt("POSITION");
			//Log.d("mylog", String.valueOf(args.getInt("POSITION")));
			Bundle extras = getArguments();
			
			if(extras!=null){
				HashMap<String, Map> allMap= new HashMap<String, Map>();
				Serializable result =extras.getSerializable("allMap");
				allMap = (HashMap<String, Map>)result;
				
				customerId=allMap.get("customerID");
				
				customerItemListMap=allMap.get("customerItemListMap");
				itemInfo=allMap.get("itemInfo");	
				customerInfo=allMap.get("customerInfo");
			//	customerphoto=allMap.get("customerphoto");
				thisUserID=extras.getString("UserID");
				extras.clear();
				allMap.clear();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Mylog", "onActivityCreated Error");
		}
		
		Brand=new String[getResources().getInteger(R.integer.brandSum)];
		Brand=getResources().getStringArray(R.array.brandName);
		
		Color=new String[getResources().getInteger(R.integer.colorSum)];
		Color=getResources().getStringArray(R.array.colorName);
		
		Type=new String[getResources().getInteger(R.integer.clothesSum)];
		Type=getResources().getStringArray(R.array.clothes);
		
		Price= new String[getResources().getInteger(R.integer.PriceSum)];
		Price = getResources().getStringArray(R.array.Price_interval);
		
		TypedArray TypeArray = getResources().obtainTypedArray(R.array.clothes_images);//抓顏色陣列
		drawableType = new Drawable[TypeArray.length()];
		for(int i=0;i<TypeArray.length();i++){
			drawableType[i]=TypeArray.getDrawable(i);
		}
		
		TypedArray TypesArray = getResources().obtainTypedArray(R.array.clothes_s_images);//抓顏色陣列
		drawableTypes = new Drawable[TypesArray.length()];
		for(int i=0;i<TypesArray.length();i++){
			drawableTypes[i]=TypesArray.getDrawable(i);
		}
		
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.refresh);
		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
		mSwipeRefreshLayout.setColorScheme(R.color.select, R.color.unselect, R.color.selectednavbarcolor, R.color.navbarcolor);
		adapter=new CMAdapeter(getActivity(),0,getActivity());//類型ListView adapter
		mainlistview =(ListView)getActivity().findViewById(R.id.cmlistview);
		firstnav =(RelativeLayout)getActivity().findViewById(R.id.topnav);
		mainlistview.setAdapter(adapter);//設定類型ListView
		mainlistview.setOnScrollListener(this);
	//	firstnav.setVisibility(View.GONE);
		
		   		
	}

	public void refresh(){
		adapter.notifyDataSetChanged();
		refreshNow=false;
		if(!isLoad){
			//mainlistview.setAdapter(adapter);
			
		}else{
			isLoad=false;
		}
	}	
	public void setIsNoNewAder(Boolean isNoNewADer){
		this.isNoNewADer=isNoNewADer;
	}
	 private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
			    @Override
			    public void onRefresh() {
			      // 3秒待機
			    	if(!isSearch){
			    		refreshNow=true;
			    		((CMRegister_Activity)getActivity()).reLoad(true);
			    	}
			      new Handler().postDelayed(new Runnable() {
			        @Override
			        public void run() {
			        	
			        	isLoad=false;
			          mSwipeRefreshLayout.setRefreshing(false);
			        }
			      }, 4000);
			    }
			  };
	 @Override
	 public void onPause(){
		 super.onPause();
		 //getFragmentManager().beginTransaction().remove(this).commit();
	 }
	 @Override
	 public void onStop(){
		 super.onStop();
	 }
	  public class CMAdapeter extends BaseAdapter{//建立adapter類別
		  private Map<String,String> urlNow;
		  private Map<String,String> indexNow;
		  private Map<String,String> brandNow;
		  private Map<String,String> UserNow;
		  private Map<String,String> AdIdNow;
		  
	        private LayoutInflater CMInflater;//固定
	        private Activity ParentActivity ;

	        public CMAdapeter(Context c,int layoutResource,Activity activity){
	            CMInflater =LayoutInflater.from(c);//指定主layout來源
	            ParentActivity=activity;
	            urlNow=new HashMap<String,String>();
	            brandNow=new HashMap<String,String>();
	            UserNow=new HashMap<String,String>();
	            indexNow=new HashMap<String,String>();
	            AdIdNow=new HashMap<String,String>();
	        }

	        @Override
	        public Object getItem(int position){
	            return null;
	        }
	        @Override
	        public long getItemId(int position){
	            return position;
	        }
	        @Override
	         public int getCount() {
	        	//if(!isSearch){
	        		//return customerId.size()+1;
		    	//}else{
		    		return customerId.size();
		    	//}
	        }
	        @Override
	        public View getView(final int position,View convertView,ViewGroup parent){
	        	try {
	        		final String customerindex = String.valueOf(position+1);
	 	           convertView =CMInflater.inflate(R.layout.cmapp_listview, null);//指定listview的layout
	 	           RelativeLayout main_layout =(RelativeLayout)convertView.findViewById(R.id.cmmain_layout);
	 	           ImageView mImgPhoto =(ImageView)convertView.findViewById(R.id.photo);//顯示使用者照片
	 	           final ImageView likeButton =(ImageView)convertView.findViewById(R.id.likeButton);
	 	           final ImageView messageButton =(ImageView)convertView.findViewById(R.id.messageButton);
	 	          
	 	           final RelativeLayout info_layout = (RelativeLayout)convertView.findViewById(R.id.info_layout);
	 	           final ImageView barcode = (ImageView)convertView.findViewById(R.id.barbode);
	 	           String thiscustomerID="";
	 	           final Customer thisCustomer;
	 	           if(position == customerId.size()){

	 	            	RelativeLayout nameLine =(RelativeLayout)convertView.findViewById(R.id.user_name);
	 	            	nameLine.setVisibility(View.GONE);
	 	            	info_layout.setVisibility(View.GONE);
	 	            	likeButton.setVisibility(View.GONE);
	 	            	if(!isNoNewADer && position!=0){
		 	            	mImgPhoto.setImageResource(R.drawable.waiting);
		 	            	
	 	            	}else if(!isNoNewADer && position==0){
	 	            		
	 	            		mImgPhoto.setImageResource(R.drawable.oops);
	 	            
	 	            	}
	 	            }else{
	 	            	TextView userName=(TextView)convertView.findViewById(R.id.username);
	 		 	        TextView userSex=(TextView)convertView.findViewById(R.id.usersex);
	 		 	         thiscustomerID=customerId.get(customerindex);
	 		 	        thisCustomer=customerInfo.get(thiscustomerID);
	 	            	
	 	            	
	 	            	String Name=thisCustomer.customerName;
	 	            	int thisSex=thisCustomer.itemSex;
	 	            	userName.setText(Name);
	 	            	userSex.setText(Sex[thisSex]);
	 	            	
	 	            	if(!thisCustomer.customerwesite.equals("")){
	 	            			userName.setOnClickListener(new View.OnClickListener(){
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									 Intent intentwebsite = new Intent();  
									 intentwebsite.setAction(Intent.ACTION_VIEW);  
									 intentwebsite.addCategory(Intent.CATEGORY_BROWSABLE);  
									 intentwebsite.setData(Uri.parse(thisCustomer.customerwesite));  
							         startActivity(intentwebsite);  
								}
							});
	 	            	}
		 	            mImgPhoto.setImageResource(R.drawable.loading);
		 	            if(customerphoto.get(thiscustomerID)==null){
		 	            	
		 	            	imgDownLoader id =new imgDownLoader(mImgPhoto,thiscustomerID);
		 					id.execute(thiscustomerID);
		 	            }else{
		 	            	mImgPhoto.setImageBitmap(customerphoto.get(thiscustomerID));
		 	            }
		 	            mImgPhoto.setId(position);

		 	            ArrayList<String> thisList =customerItemListMap.get(thiscustomerID);
		 	            LinearLayout logo_layout =(LinearLayout)convertView.findViewById(R.id.logo_layout);
		 	            
		 	            final Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		 	            
		 	            int icon_width = (display.getWidth()/5);
		 	            RelativeLayout.LayoutParams frame_icon = new RelativeLayout.LayoutParams(icon_width,200);
		 	            Customer thisItem;
		 	            
		 	           final int display_width=display.getWidth();
		 	            for(int i=0;i<thisList.size();i++){
		 	                final String listitem =thisList.get(i);
		 	                final ArrayList<String> thisArray=thisList;
		 	                ImageView[] icon=new ImageView[thisList.size()]; 
		 	                thisItem=itemInfo.get(listitem);
		 	                RelativeLayout.LayoutParams margin_main = new RelativeLayout.LayoutParams((int)(thisItem.itemWidth*display_width), (int)(thisItem.itemHeight*display_width));  //設定背景長寬
		 	                final ImageView mImgClothes = new ImageView(ParentActivity); //Item
		 	                
		 	                
		 	                margin_main.topMargin=(int)(thisItem.itemTopMargin*display_width);
		 	                margin_main.leftMargin=(int)(thisItem.itemLeftMargin*display_width);
		 	                mImgClothes.setId(Integer.parseInt(listitem));
		 	                mImgClothes.setLayoutParams(margin_main);
		 	                main_layout.addView(mImgClothes);
		 	                
		 	                final  ImageView mLogoIcon =new ImageView(ParentActivity);//LogoIcon
		 	                final  LinearLayout logo_icon_layout = (LinearLayout) convertView.findViewById(R.id.logo_layout);
		 	                String iconID=listitem+( (i<10) ? "0"+i : i);
		 	                
		 	                 mLogoIcon.setId(Integer.parseInt(iconID));
		 	                 mLogoIcon.setRotation(0);
		 	                 mLogoIcon.setImageDrawable(drawableType[thisItem.itemType]);
		 	                 mLogoIcon.setPadding(5, 0, 5, 0);
		 	                 mLogoIcon.setLayoutParams(frame_icon);  //line140
		 	               
		 	               
		 	                final TextView info_txt = (TextView)convertView.findViewById(R.id.info_txt);
		 	                final ImageView imgshop = (ImageView)convertView.findViewById(R.id.shop);
		 	                final ImageView mimgshop = (ImageView)convertView.findViewById(R.id.shop);
		 	               
		 	                 info_layout.setVisibility(View.GONE);
		 	                 barcode.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									coupon_dlg = new Dialog(getActivity());
									coupon_dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);  
									coupon_dlg.setContentView(R.layout.coupon_dialog);
									coupon_dlg.setCanceledOnTouchOutside(true);
									coupon_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
									
									Button mbtnOK = (Button)coupon_dlg.findViewById(R.id.btnOK);
									
									mimgforsale = (ImageView)coupon_dlg.findViewById(R.id.forsale);
									mimgcoupon = (ImageView)coupon_dlg.findViewById(R.id.coupon);
									mtxtcoupon = (TextView)coupon_dlg.findViewById(R.id.txtcoupon);
								//	 Log.d("mylog", brandNow.get(position));
									if(Download_brandAD.get(brandNow.get(String.valueOf(position)))==null){
										brandADDownLoader id =new brandADDownLoader(mimgforsale,brandNow.get(String.valueOf(position)));
					 					id.execute();
									}else{
										mimgforsale.setImageBitmap(Download_brandAD.get(brandNow.get(String.valueOf(position))));
									}
									
									new BarCode(UserNow.get(String.valueOf(position)),thisUserID,brandNow.get(String.valueOf(position)),mimgcoupon,mtxtcoupon,AdIdNow.get(String.valueOf(position))).execute();
									
									coupon_dlg.show();
									mbtnOK.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											coupon_dlg.cancel();
										}
									});
									
								}
							});
			 	            mImgPhoto.setOnClickListener(new View.OnClickListener(){
			 	                	public void onClick(View v) {
			 	                		
			 	                		barcode.setVisibility(View.GONE);
			 	                		info_layout.setVisibility(View.GONE);
			 	                		for(int j=0;j<thisArray.size();j++){
			 	                			ImageView thiscloth=(ImageView)getActivity().findViewById( Integer.parseInt( thisArray.get(j) ) );
			 	                			ImageView thisicon=(ImageView)getActivity().findViewById( Integer.parseInt( thisArray.get(j)+( (j<10) ? "0"+j : j) ) );
			 	                			
			 	                			thiscloth.setVisibility(View.GONE);
			 	                			
			 	                			thisicon.setImageDrawable(drawableType[itemInfo.get(thisArray.get(j)).itemType]);
			 	                		}
			 	                		
			 	                	}
			 	                 });
			 	           final String id = thisCustomer.customerId + thisCustomer.AdId;
				 	       final Customer outputCustomer =customerInfo.get(id);  

			 	           mimgshop.setOnClickListener(new View.OnClickListener(){
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									 Intent intent = new Intent();  
									 new Click(outputCustomer.customerId,thisUserID,outputCustomer.AdId,Integer.parseInt(indexNow.get(String.valueOf(position))),"EnterShop").execute();
							         intent.setAction(Intent.ACTION_VIEW);  
							         intent.addCategory(Intent.CATEGORY_BROWSABLE);  
							         intent.setData(Uri.parse(urlNow.get(String.valueOf(position))));  
							         startActivity(intent);  
								}
							});
			 	           
			 	         
		 	                  mLogoIcon.setOnClickListener(new View.OnClickListener() {
		 	                    @Override
		 	                    public void onClick(View v) {
			 	                   	info_layout.setVisibility(View.GONE);
			 	                   
		 	                		for(int j=0;j<thisArray.size();j++){
		 	                			ImageView thiscloth=(ImageView)getActivity().findViewById( Integer.parseInt( thisArray.get(j) ) );
		 	                			ImageView thisicon=(ImageView)getActivity().findViewById( Integer.parseInt( thisArray.get(j)+( (j<10) ? "0"+j : j) ) );
		 	                			
		 	                			thiscloth.setVisibility(View.GONE);
		 	                			
		 	                			thisicon.setImageDrawable(drawableType[itemInfo.get( thisArray.get(j)).itemType]);
		 	                		}
		 	                    	mImgClothes.setImageDrawable(getResources().getDrawable(R.drawable.circle_dash));
		 	                    	mLogoIcon.setImageDrawable(drawableTypes[itemInfo.get(listitem).itemType]);
		 	                    	
		 	                    	new Click(outputCustomer.customerId,thisUserID,outputCustomer.AdId,itemInfo.get(listitem).itemIndex,"ClickItem").execute();
		 	                    	
		 	                    	if(itemInfo.get(listitem).isBrandItem == 0){
		 	                    		if(itemInfo.get(listitem).itemPrice == 6){
			 	                    		info_txt.setText(itemInfo.get(listitem).itemBrand +"\n"   
			                    					  + Color[itemInfo.get(listitem).itemColor] +"\n"
			                    					  + Price[itemInfo.get(listitem).itemPrice]+ "~" +"\n" 
			                    					  + "-");
			 	                    	}else{
			 	                    		info_txt.setText(itemInfo.get(listitem).itemBrand +"\n"   
			                    					  + Color[itemInfo.get(listitem).itemColor] +"\n"
			                    					  + Price[itemInfo.get(listitem).itemPrice]+ "~" +"\n" 
			                    					  + Price[(itemInfo.get(listitem).itemPrice)+1]);
			 	                    	}
		 	                    		barcode.setVisibility(View.GONE);
		 	                    	}else{
		 	                    			info_txt.setText(itemInfo.get(listitem).itemBrand +"\n"   
		                    					  + Color[itemInfo.get(listitem).itemColor] +"\n"
		                    					  + "$" + itemInfo.get(listitem).itemPrice);
		 	                    			
		 	                    	}
		 	                    	barcode.setVisibility(View.VISIBLE);
 	                    			barcode.bringToFront();
 	                    			brandNow.put(String.valueOf(position),  itemInfo.get(listitem).itemBrand);
 	                    			UserNow.put(String.valueOf(position), outputCustomer.customerId);
 	                    			AdIdNow.put(String.valueOf(position), outputCustomer.AdId);
		 	                    	
		 	                    	mImgClothes.setVisibility(View.VISIBLE);
		 	                    	info_layout.setVisibility(View.VISIBLE);
		 	                    	info_layout.bringToFront();
		 	                    	urlNow.put(String.valueOf(position),  itemInfo.get(listitem).itemUrl);
		 	                    	indexNow.put(String.valueOf(position),String.valueOf(itemInfo.get(listitem).itemIndex));
		 	                    }
		 	                 
		 	                    
		 	                    
		 	                });//icon click
		 	                  
		 	                  if(thisCustomer.likeClike==0){
		 	                	 likeButton.setImageResource(R.drawable.dislike);
		 	                  }else{
		 	                	 likeButton.setImageResource(R.drawable.like);
		 	                  }
		 	                  
		 	               
		 	                 
		 	                 likeButton.setOnClickListener(new View.OnClickListener(){
		 	                	 
		 	                	
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									
									new LikeClick(outputCustomer,likeButton,outputCustomer.customerId,thisUserID,outputCustomer.AdId).execute();
								}
		 	                	   
		 	                   });
		 	                 	messageButton.setOnClickListener(new View.OnClickListener(){
		 	                	 
		 	                	
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									showMessageDialog(outputCustomer.customerId,outputCustomer.AdId);
									//new LikeClick(outputCustomer,likeButton,outputCustomer.customerId,thisUserID,outputCustomer.AdId).execute();
								}
		 	                	   
		 	                   });
		 	             
		 	                 logo_layout.addView(mLogoIcon);
		 	            }
		 	            
	 	            }
	 	                
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("Mylog", "getView Error");
				}
	            return convertView;//回傳給adapter
	        }
	    }
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		  if (mLastFirstVisibleItem > firstVisibleItem ) {
              //Log.d("mylog", "scrolling up"+visibleItemCount);
              scrolldown=false;

          } else if (mLastFirstVisibleItem < firstVisibleItem ) {
              //Log.d("mylog", "scrolling down "+visibleItemCount);
              scrolldown=true;
             
          }
		  
          mLastFirstVisibleItem = firstVisibleItem;
          mLastVisibleItemCount = visibleItemCount;
          if( firstVisibleItem + visibleItemCount >= totalItemCount){
			  if(!isLoad){
				  isLoad=true;
				  if(!isSearch && !refreshNow){
					  ((CMRegister_Activity)getActivity()).reLoad(false);
				  }
				  
			  }
	      }
          for(int i=firstVisibleItem;i<firstVisibleItem+1;i++){
        	  if(i >= customerId.size()-1){
        		  break;
        	  }
        	  if(Download_customerphoto.get(String.valueOf(i)) == null){
        		  
        	  }
          }
        
		
	}
	
	 public class imgDownLoader extends AsyncTask<String, Integer, Bitmap> {
			
			private ImageView imgView;
			private String customerID;
			
			
			 public imgDownLoader(ImageView imgView,String customerID) {
			        super();
			        this.imgView=imgView;
			        this.customerID=customerID;
			      
			        
			    }
			protected void onPreExecute() {
			}
			@Override
			protected Bitmap doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				try {
					
						Bitmap outBitmap;
						URL url=new URL("http://cmapp.nado.tw/photo/"+params[0]+".jpg");
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
				customerphoto.put(customerID,img);
				imgView.setImageBitmap(img);
			
				
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
						//Log.d("mylog", "http://cmapp.nado.tw/AD/"+brandName+"_AD.png");
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
	 public class SendMessage extends AsyncTask<String, Integer, String> {

			private String customerID,UserID,AdID,messageText;//點擊者,廣告者
			JSONObject ReturnLikeJsonobject = new JSONObject();
			
			 public SendMessage(String messageText ,String UserID,String customerID,String AdID) {
			        super();

			        this.UserID=UserID;
			        this.messageText=messageText;
			        this.customerID=customerID;
			        this.AdID=AdID;
			        
			    }

			@Override
			protected String doInBackground(String... args) {
				Log.d("mylog", "CreateUserNow:");
				JSONObject UserIDObject = new JSONObject();
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				try {
					UserIDObject.put("UserID", this.UserID);
					UserIDObject.put("CustomerID", this.customerID);
					UserIDObject.put("AdID", this.AdID);
					UserIDObject.put("messageText", this.messageText);
					
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
					
					jsonarray.add(new BasicNameValuePair("message", UserIDObject.toString()));// 重要！！
					Log.d("mylog", UserIDObject.toString());
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/uploadMessage.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						try {
							String json = EntityUtils.toString(httpEntity);					
							ReturnLikeJsonobject = new JSONObject(json);
							Log.d("mylog", "uploadMessage:"+ReturnLikeJsonobject.toString());
						} catch (JSONException ex) {
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "uploadMessageError");
					}
					return null;
				
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				new getMessage(this.UserID,this.AdID).execute();
				
					
			}
			
		
		}
	 public class getMessage extends AsyncTask<String, Integer, String> {

			private String UserID,AdID;//點擊者,廣告者
			JSONObject ReturnMessageJsonobject = new JSONObject();
			
			 public getMessage(String UserID,String AdID) {
			        super();
			       
			        this.UserID=UserID;
			        this.AdID=AdID;
			       
			    }

			@Override
			protected String doInBackground(String... args) {
			
				JSONObject UserIDObject = new JSONObject();
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				try {
					UserIDObject.put("UserID",this.UserID);
					UserIDObject.put("AdID",this.AdID);
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
					jsonarray.add(new BasicNameValuePair("GetMessage", UserIDObject.toString()));// 重要！！
					try {
						
						
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/Get_Message.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						try {
							String json = EntityUtils.toString(httpEntity);					
							ReturnMessageJsonobject = new JSONObject(json);
							Log.d("mylog", "GetMessage:"+ReturnMessageJsonobject.toString());
						} catch (JSONException ex) {
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "getMessageError");
					}
					return null;
				
				
			}

			protected void onPostExecute(String file_url) {
				
		    	
				String barcode_data;
				try {
					JSONArray jsonarray = ReturnMessageJsonobject.getJSONArray("MessageText");
					JSONObject AllItemjsonobject = new JSONObject(); 
					MessageText_Array.clear();;
					MessageID_Array.clear();
					MessageName_Array.clear();
					for(int i=0;i<jsonarray.length();i++){
						AllItemjsonobject=jsonarray.getJSONObject(i);//找第i條
						
						String returnmessage = AllItemjsonobject.getString("message");
						String returnCustomerID = AllItemjsonobject.getString("CustomerID");//抓userID, AdID
						String returnUserName = AllItemjsonobject.getString("UserName");//抓userID, AdID
						MessageText_Array.add(returnmessage);
						MessageID_Array.add(returnCustomerID);
						MessageName_Array.add(returnUserName);
						
					}
					
					message_View.setAdapter(messageView_Adapter);
					//messageView_Adapter.notifyDataSetChanged();
					//message_View.scrollTo(0, message_View.getHeight());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
			}
			
		
		}
	 public class LikeClick extends AsyncTask<String, Integer, String> {
		 	private ImageView imgView;
		 	private Customer thisCustomer;
			private String customerID,UserID,AdID;//點擊者,廣告者
			JSONObject ReturnLikeJsonobject = new JSONObject();
			
			 public LikeClick(Customer thisCustomer,ImageView imgView,String UserID,String customerID,String AdID) {
			        super();
			        this.thisCustomer=thisCustomer;
			        this.imgView=imgView;
			        this.UserID=UserID;
			        this.customerID=customerID;
			        this.AdID=AdID;
			        
			    }

			@Override
			protected String doInBackground(String... args) {
				Log.d("mylog", "CreateUserNow:");
				JSONObject UserIDObject = new JSONObject();
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				try {
					UserIDObject.put("UserID", this.UserID);
					UserIDObject.put("CustomerID", this.customerID);
					UserIDObject.put("AdID", this.AdID);
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
					jsonarray.add(new BasicNameValuePair("LikeClick", UserIDObject.toString()));// 重要！！
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/LikeClick.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						try {
							String json = EntityUtils.toString(httpEntity);					
							ReturnLikeJsonobject = new JSONObject(json);
							Log.d("mylog", "LikeClick:"+ReturnLikeJsonobject.toString());
						} catch (JSONException ex) {
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "LikeClickError");
					}
					return null;
				
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				
					try {
						int isLike = Integer.parseInt(ReturnLikeJsonobject.getString("LikeClick"));
						thisCustomer.setLikeClick(isLike);
						if(isLike==0){
							imgView.setImageResource(R.drawable.dislike);
						}else if(isLike==1){
							imgView.setImageResource(R.drawable.like);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					
					
				}
			
		
		}
	 public class Click extends AsyncTask<String, Integer, String> {

			private String customerID,UserID,AdID,itemIndex,EventNow;//點擊者,廣告者
			JSONObject ReturnLikeJsonobject = new JSONObject();
			
			 public Click(String UserID,String customerID,String AdID,int itemIndex,String EventNow) {
			        super();
			        this.itemIndex=String.valueOf(itemIndex);
			        this.UserID=UserID;
			        this.customerID=customerID;
			        this.AdID=AdID;
			        this.EventNow=EventNow;
			    }

			@Override
			protected String doInBackground(String... args) {
				Log.d("mylog", "CreateUserNow:");
				JSONObject UserIDObject = new JSONObject();
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				try {
					UserIDObject.put("UserID", this.UserID);
					UserIDObject.put("CustomerID", this.customerID);
					UserIDObject.put("AdID", this.AdID);
					UserIDObject.put("Event", this.EventNow);
					UserIDObject.put("ItemIndex", this.itemIndex);
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
					jsonarray.add(new BasicNameValuePair("Click", UserIDObject.toString()));// 重要！！
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/Click.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "LikeClickError");
					}
					return null;
				
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				
				
					
			}
			
		
		}
	 public class BarCode extends AsyncTask<String, Integer, String> {

			private String customerID,UserID,itemBrand;//點擊者,廣告者
			JSONObject ReturnBarCodeJsonobject = new JSONObject();
			private ImageView img ;
			private TextView tx;
			private String AdID;
		
			
			 public BarCode(String UserID,String customerID,String itemBrand,ImageView img,TextView tx,String AdID) {
			        super();
			        this.itemBrand=itemBrand;
			        this.UserID=UserID;
			        this.customerID=customerID;
			        this.AdID=AdID;
			       this.img=img;
			       this.tx= tx;
			    }

			@Override
			protected String doInBackground(String... args) {
				Log.d("mylog", "CreateUserNow:");
				JSONObject UserIDObject = new JSONObject();
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				try {
					UserIDObject.put("UserID", this.UserID);
					UserIDObject.put("CustomerID", this.customerID);
					UserIDObject.put("AdID", this.AdID);
					UserIDObject.put("BrandName", this.itemBrand);
					Log.d("mylog",this.itemBrand);
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
					jsonarray.add(new BasicNameValuePair("Coupon", UserIDObject.toString()));// 重要！！
					try {
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/Coupon.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						try {
							String json = EntityUtils.toString(httpEntity);					
							ReturnBarCodeJsonobject = new JSONObject(json);
							Log.d("mylog", "Coupon:"+ReturnBarCodeJsonobject.toString());
						} catch (JSONException ex) {
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "CouponError");
					}
					return null;
				
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				
				String barcode_data;
				try {
					barcode_data = ReturnBarCodeJsonobject.getString("Code");
					
					tx.setText(barcode_data);
	 	               // barcode image
	 	           Bitmap bitmap = null;

	 	               try {

	 	                   bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
	 	                  img.setImageBitmap(bitmap);

	 	               } catch (WriterException e) {
	 	                   e.printStackTrace();
	 	               }
					
	 	               
					ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE); 
					ClipData clip = ClipData.newPlainText("label", barcode_data);
					clipboard.setPrimaryClip(clip);
					Toast toast =Toast.makeText(getActivity(),"已複製Coupon代碼到剪貼簿", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
					
			}
			
		
		}
	 
	 
	  public void showMessageDialog(final String id,final String adid){
	    	
	    		
	    		if(messagedlg!=null){
	    			messagedlg.cancel();
	    		}
	    		
	    
	    	
			messagedlg= new Dialog(getActivity());
			//messagedlg.setTitle("Message.");
			MessageText_Array.clear();;
			MessageID_Array.clear();
			MessageName_Array.clear();
			messagedlg.requestWindowFeature(Window.FEATURE_NO_TITLE);  
			messagedlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			messagedlg.setContentView(R.layout.message_dialog);
			messagedlg.getWindow().getAttributes().dimAmount=0.1f;
			messagedlg.setCancelable(true);
			messagedlg.setCanceledOnTouchOutside(true);
			//messagedlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
			message_View=(ListView)messagedlg.findViewById(R.id.message_Listview);
			messageView_Adapter= new message_listview(getActivity(),getActivity(),id);
			//message_View.setAdapter(messageView_Adapter);
			new getMessage(id,adid).execute();
			
			final Button messageSend=(Button)messagedlg.findViewById(R.id.messageSend);
			Button messgaeCancel=(Button)messagedlg.findViewById(R.id.messagecancel);
			final EditText messageEdit =(EditText)messagedlg.findViewById(R.id.messageEdit);
			String message=String.valueOf(messageEdit.getText());
			messageSend.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String message=String.valueOf(messageEdit.getText());
					if(!message.equals("")){
						new SendMessage(message, id, thisUserID, adid).execute();
						messageEdit.setText("");
						if (messageEdit.isFocused()) {
						
						    InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
						    imm.hideSoftInputFromWindow(messageEdit.getWindowToken(), 0);
						}
						
						messageEdit.clearFocus();
						//InputMethodManager.HIDE_IMPLICIT_ONLY;
					}
					
				}
			});
			messgaeCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast toast =Toast.makeText(CMSetting_Activity.this,"Upload Scucess", Toast.LENGTH_LONG);
					//toast.setGravity(Gravity.CENTER, 0, 0);
					//toast.show();
					 if(messagedlg!=null ){
						 messagedlg.cancel();
	     			}else{
	     				messagedlg.cancel();
	     			}
				}
			});
			
			
			messagedlg.show();
			
		}
	    
	  
	  
	    /////////////////////////////
	    public class message_listview extends BaseAdapter {
	    	
	    	
	    	Activity myactivity;//主要呼叫的Activity
	    	private LayoutInflater myInflater;
	    	private Context mycontext;
	    	private int length=10;//預設長度10
	    	private String itemid="";
	    	private String AduserID="";
	    	
	    	public message_listview(Activity activity,Context c,String id){
	    		myInflater =LayoutInflater.from(c);
	    		myactivity=activity;
	    		mycontext=c;
	    		AduserID=id;
	    	}
	   
	    	@Override
	    	public int getCount() {
	    		if(MessageText_Array.size()==0){
	    			return 1;
	    		}else{
	    			return MessageText_Array.size();
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
	    		convertView =myInflater.inflate(R.layout.message_listview_items,null);//設定Layout
	    		RelativeLayout mainlayout =(RelativeLayout)convertView.findViewById(R.id.message_listview);
	    		mainlayout.setOnClickListener(new RelativeLayout.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						messagedlg.cancel();
					}
				});
	    		
	    		TextView messagename =(TextView)convertView.findViewById(R.id.message_name_txt);
	    		TextView messagefriend =(TextView)convertView.findViewById(R.id.message_firendname_txt);
	    		TextView messageText =(TextView)convertView.findViewById(R.id.message_txt);
	    		if(MessageText_Array.size()==0){
	    			messagename.setText("system");
	    			messagefriend.setVisibility(View.GONE);
	    			messageText.setText("請幫我留個言吧！！");
	    		}else{
	    			if(MessageID_Array.get(position).equals(AduserID)){
		    			messagename.setText(MessageName_Array.get(position));
		    			messagefriend.setVisibility(View.GONE);
		    			
		    		}else{
		    			messagefriend.setText(MessageName_Array.get(position));
		    			messagename.setVisibility(View.GONE);
		    		}
		    		messageText.setText(MessageText_Array.get(position));
	    		}
	    		
	    		
	    		
	    		  
	    		return convertView;

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

}
