package com.ntust.cmapp;


import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.NetPermission;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.security.auth.PrivateCredentialPermission;

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

import com.ntust.cmapp.CMBrowse_Fragment.CMAdapeter;
import com.ntust.cmapp.CMBrowse_Fragment.imgDownLoader;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class Search_Activity extends Fragment implements Serializable ,OnScrollListener{
	private ListView listView;
		   Map<String,String> customerId = new HashMap<String,String>(); //建立一個Map介面為CustomerId
		   private Map<String, String> beaconMAC =new HashMap<String, String>();
		    
		    public Map<String, Customer> customerInfo = new HashMap<String,Customer>();
		    Map<String,Bitmap> customerphoto = new HashMap<String,Bitmap>();
		    Map<String,Bitmap> Download_customerphoto = new HashMap<String,Bitmap>();
		    private String thisUserID="";
		    private ListView mListView;
		    int screen_width,mLastFirstVisibleItem,mLastVisibleItemCount;
		    String UserID ="";
		    Activity mainActivity;
		    Boolean scrolldown=false,isLoad=false;;
		    String[] Brand,Type,Color,Price,lowerPrice,upperPrice; 
		    String[] Sex={"MEN","WOMEN"};
		    Drawable[] drawableType,drawableTypes;
		    Drawable[][][] drawableIcon;
		    Search_Adapter adapter;
		    String customerPhotoID;
		    String CustomerItemId;
		    RelativeLayout firstnav ;
		    ListView mainlistview; 
		    private int lower=0,upper=6;
		    private Boolean isSearch=false;
		    boolean isNoNewADer=false;
		    private SwipeRefreshLayout mSwipeRefreshLayout;
		    int[][] IconId;
			int[][] isClick;
		    JSONObject ReturnInfoJsonobject = new JSONObject();
		    private Customer thisCustomer;
		    ImageView mlikeButton, mTxtSearch;
		    int likeClick=0;
		    
		    private Spinner Spinner_lower,Spinner_upper;
			List<String> list_lower,list_upper;
			ImageView[] thisicon;
			int[] count ={0,0,0,0};
			ArrayAdapter<String> adapter_lower,adapter_upper;
		    public Search_Activity(Boolean isSearch){
		    	this.isSearch=isSearch;
		    }
		    public void setisSearch(Boolean isSearch){
		    	this.isSearch=isSearch;
		    }
		    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
				View view =inflater.inflate(R.layout.search_listview_layout,container,false);//設定Layout
				return view;
			}

			@Override
			
		 public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			try {
				Bundle extras = getArguments();
				if(extras!=null){
					HashMap<String, Map> allMap= new HashMap<String, Map>();
					Serializable result =extras.getSerializable("allMap");
					allMap = (HashMap<String, Map>)result;
					customerId=allMap.get("customerID");
					customerInfo=allMap.get("customerInfo");
					beaconMAC=allMap.get("customerBeaconID");
					thisUserID=extras.getString("UserID");
				
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("Mylog", "onActivityCreated Error");
			}
			IconId=new int[4][];
			TypedArray TypeArray = getResources().obtainTypedArray(R.array.SexIcon_id);//抓顏色陣列
			int[] drawableSexID= new int[TypeArray.length()];
			for(int i=0;i<TypeArray.length();i++){
				drawableSexID[i]=TypeArray.getResourceId(i, 0);
			}
			IconId[0]=drawableSexID;
			
			TypeArray = getResources().obtainTypedArray(R.array.ClothKindIcon_id);//抓顏色陣列
			int[] drawableTypeID= new int[TypeArray.length()];
			for(int i=0;i<TypeArray.length();i++){
				drawableTypeID[i]=TypeArray.getResourceId(i, 0);
			}
			IconId[1]=drawableTypeID;
			TypeArray = getResources().obtainTypedArray(R.array.ColorIcon_id);//抓顏色陣列
			int[] drawableColorID= new int[TypeArray.length()];
			for(int i=0;i<TypeArray.length();i++){
				drawableColorID[i]=TypeArray.getResourceId(i, 0);
			}
			IconId[2]=drawableColorID;
			TypeArray = getResources().obtainTypedArray(R.array.BrandIcon_id);//抓顏色陣列
			int[] drawableBrandID= new int[TypeArray.length()];
			for(int i=0;i<TypeArray.length();i++){
				drawableBrandID[i]=TypeArray.getResourceId(i, 0);
			}
			IconId[3]=drawableBrandID;
			
			mTxtSearch = (ImageView) getActivity().findViewById(R.id.txtSearch);
			mTxtSearch.setOnClickListener(SearchClick);
			
			mlikeButton = (ImageView)getActivity().findViewById(R.id.likeButton);
			
			mlikeButton.setOnClickListener(LikeButtonClick);
			
			
			mSwipeRefreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.search_refresh);
			mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
			mSwipeRefreshLayout.setColorScheme(R.color.select, R.color.unselect, R.color.selectednavbarcolor, R.color.navbarcolor);
			adapter=new Search_Adapter((CMRegister_Activity)getActivity(),0,(CMRegister_Activity)getActivity());//類型ListView adapter
			mainlistview =(ListView)getActivity().findViewById(R.id.searchlistview);
			firstnav =(RelativeLayout)getActivity().findViewById(R.id.topnav);
			mainlistview.setAdapter(adapter);//設定類型ListView
			mainlistview.setOnScrollListener(this);
			isClick = new int[4][];
			for(int i=0;i<IconId.length;i++){
				int[] thisclick =new int[IconId[i].length];
				for(int j = 0;j<IconId[i].length;j++){
					if(j==0){
						thisclick[j] =1;
					}else{
						thisclick[j] =0;
					}
					
				}
				isClick[i]=thisclick;
			}
			Log.d("mylog", " ");
		//	firstnav.setVisibility(View.GONE);
			Brand=new String[getResources().getInteger(R.integer.brandSum)];
			Brand=getResources().getStringArray(R.array.brandName);
			lower=0;
			upper=6;
			
		}
		public void refresh(){
			adapter.notifyDataSetChanged();
			if(!isLoad){
				mainlistview.setAdapter(adapter);
				
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
				    	if(likeClick==0){
				    		((CMRegister_Activity)getActivity()).reLoad(true);
				    	}else{
				    		new GetSearchResult().execute();
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
		 
		  public class Search_Adapter extends BaseAdapter{//建立adapter類別
		        private LayoutInflater CMInflater;//固定
		        private Activity ParentActivity ;

		        public Search_Adapter(Context c,int layoutResource,Activity activity){
		            CMInflater =LayoutInflater.from(c);//指定主layout來源
		            ParentActivity=activity;
		        }

		        @Override
		        public Object getItem(int position){
		            return null;
		        }
		        @Override
		        public long getItemId( int position){
		            return position;
		        }
		        @Override
		         public int getCount() {
		        	
		        	if(customerId.size()%3==0){
		        		Log.d("mylog", "size:"+customerId.size()/3);
		        		return customerId.size()/3;
		        	}else{
		        		int count =customerId.size()/3+1;
		        		Log.d("mylog", "size:"+count);
		        		return (customerId.size()/3 )+1;
		        	}
		            
		        }
		        @Override
		        public View getView(final int position,View convertView,ViewGroup parent){
		        	try {
		        		String customerindex = String.valueOf(position)+1;
		 	            convertView =CMInflater.inflate(R.layout.search_layout, null);//指定listview的layout
		 	            
		 	            LinearLayout searchphoto =(LinearLayout)convertView.findViewById(R.id.searchphoto);
		 	          
	 	            	Log.d("mylog", "ZZZZZZ");

//		 	           final RelativeLayout info_layout = (RelativeLayout)convertView.findViewById(R.id.info_layout);
		 	            if(customerId.size()==0){
		 	            	Log.d("mylog", "VVVVVVV");

		 	            	if(!isNoNewADer && position!=0){
//			 	            	mImgPhoto.setImageResource(R.drawable.waiting);
			 	            	
		 	            	}else if(!isNoNewADer && position==0){
		 	            		

		 	            
		 	            	}
		 	            }else{
		 					
		 					ImageView[] searchView=new ImageView[3];
		 					searchView[0] = (ImageView)convertView.findViewById(R.id.searchphoto1);
		 					searchView[1] = (ImageView)convertView.findViewById(R.id.searchphoto2);
		 					searchView[2] = (ImageView)convertView.findViewById(R.id.searchphoto3);
		 				
		 					//searchphoto1.setImageResource(R.drawable.loading);
		 					for(int i=0;i<3;i++){
		 						final int indexOfCustomerID=((position*3)+i+1); 
		 						
		 						if((position*3)+i>=customerId.size()) break;
		 						searchView[i].setImageResource(R.drawable.loading);
		 						if(customerphoto.get(customerId.get(String.valueOf(indexOfCustomerID)))==null){
				 	            	imgDownLoader id =new imgDownLoader(searchView[i],customerId.get(String.valueOf(indexOfCustomerID)));//load 處發執行載入
				 	            	
				 					id.execute(customerId.get(String.valueOf(indexOfCustomerID)));
				 					
				 	            }else{
				 	            		searchView[i].setImageBitmap(customerphoto.get(customerId.get(String.valueOf(indexOfCustomerID))));// not null, load photo
				 	            		
				 	            }
		 						searchView[i].setOnClickListener(new View.OnClickListener(){
		 	            			public void onClick(View v){
		 	            				Log.d("mylog", "dddddddddddddddd");
		 	            				
		 	            				 //String beaconID = customerInfo.get(customerId.get(String.valueOf(indexOfCustomerID))).BeaconId;
		 	            				ArrayList<String> beaconID=new ArrayList<String>();
		 	            				beaconID.add(customerInfo.get(customerId.get(String.valueOf(indexOfCustomerID))).BeaconId);
		 	            				Intent intent = new Intent();
		 	            	            intent.setClass(getActivity(), Ofsearch_Activity.class);
		 	            	            intent.putExtra("likeclick", likeClick);
		 	            	            if(likeClick==1){
		 	            	            	 String clickAdID =  customerInfo.get(customerId.get(String.valueOf(indexOfCustomerID))).AdId;
		 	            	            	 String clickUserID =  customerInfo.get(customerId.get(String.valueOf(indexOfCustomerID))).customerId;
		 	            	            	intent.putExtra("clickAdID", clickAdID);
		 	            	            	intent.putExtra("clickUserID", clickUserID);
		 	            	            }
		 	            	            
		 	            	            intent.putExtra("Beacon", beaconID);
		 	            	            intent.putExtra("UserID", thisUserID);
		 	            	            startActivity(intent);
		 	            	           

		 	            			}
		 	            		
		 	            		});
		 	            		Log.d("mylog", String.valueOf((position*3)+i+1));
		 					}
		 					
		 				for(int i =0;i<customerId.size();i++){
		 					
		 				}
				 	           
		 	            }
		 	                
					} catch (Exception e) {
						// TODO: handle exception
						Log.d("mylog", "getView Error");
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
	          if( firstVisibleItem + visibleItemCount >= totalItemCount-1){
				  if(!isLoad){
					  isLoad=true;
					  ((CMRegister_Activity)getActivity()).reLoad(false);
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
				private ProgressDialog progressDialog_;
				private Activity uiActivity_;
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
		 private View.OnClickListener LikeButtonClick = new View.OnClickListener(){
			 @Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				 if( likeClick ==1){
					 likeClick =0;
					 mlikeButton.setImageResource(R.drawable.dislike);
					 new GetSearchResult().execute();
				 }else{
					 mlikeButton.setImageResource(R.drawable.like);
					 likeClick =1;
					 new GetSearchResult().execute();
				 }
				
				 	
				}
		 };
		 
		 private View.OnClickListener SearchClick = new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				showDialog();
			}
		};
		Dialog dlg ;
		public void showDialog(){
			if(dlg!=null){
				dlg.cancel();
			}
				dlg = new Dialog((CMRegister_Activity)getActivity());
				
				dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);  
				dlg.setContentView(R.layout.search_dialog_layout);
				dlg.setCanceledOnTouchOutside(true);
				dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
				Button btnOK =(Button)dlg.findViewById(R.id.btnOK);
				Button mBtnClear = (Button) dlg.findViewById(R.id.btnClear);
				Button mBtnCancel = (Button) dlg.findViewById(R.id.btnCancel);
				btnOK.setOnClickListener(SearchOnClick);
				mBtnClear.setOnClickListener(ClearOnClick);
				mBtnCancel.setOnClickListener(CancelOnclick);
			
				Spinner_lower = (Spinner) dlg.findViewById(R.id.SpinerLowerPrice);
				Spinner_upper = (Spinner) dlg.findViewById(R.id.SpinerUpperPrice);
				
				lowerPrice = new String[getResources().getInteger(R.integer.PriceSum)];
				lowerPrice = getResources().getStringArray(R.array.Price_interval);
				
				list_lower = new ArrayList<String>();
				for(int i=0;i<=upper;i++){
					list_lower.add(lowerPrice[i]);
				}
				
				ArrayAdapter<String> adapter_lower = new ArrayAdapter<String> 
				((CMRegister_Activity)getActivity(),android.R.layout.simple_list_item_1, list_lower);
				Spinner_lower.setAdapter(adapter_lower);
				
				upperPrice = new String[getResources().getInteger(R.integer.PriceSum)];
				upperPrice = getResources().getStringArray(R.array.Price_Upper);
				
				list_upper = new ArrayList<String>();
				for(int i=lower;i<=6;i++){
					list_upper.add(upperPrice[i]);
				}
				
				ArrayAdapter<String> adapter_upper = new ArrayAdapter<String> 
				((CMRegister_Activity)getActivity(),android.R.layout.simple_list_item_1, list_upper);
				Spinner_upper.setAdapter(adapter_upper);
				
				Spinner_lower.setOnItemSelectedListener(lowerSelect);
				Spinner_upper.setOnItemSelectedListener(UpperSelect);
				
				Spinner_lower.setSelection(lower);
				Spinner_upper.setSelection(upper-lower);
				
				ImageView[][] Icon = new ImageView[4][];
			
				
				TypedArray TypeArray = getResources().obtainTypedArray(R.array.Sex_images);//抓顏色陣列
				Drawable[] drawableSex = new Drawable[TypeArray.length()];
				for(int i=0;i<TypeArray.length();i++){
					drawableSex[i]=TypeArray.getDrawable(i);
				}
				
				TypeArray = getResources().obtainTypedArray(R.array.Sex_images_unclick);//抓顏色陣列
				Drawable[] drawableSex_g = new Drawable[TypeArray.length()];
				for(int i=0;i<TypeArray.length();i++){
					drawableSex_g[i]=TypeArray.getDrawable(i);
				}
				
				TypeArray = getResources().obtainTypedArray(R.array.clothes_images);//抓顏色陣列
				Drawable[] drawableClothKind = new Drawable[TypeArray.length()];
				for(int i=0;i<TypeArray.length();i++){
					drawableClothKind[i]=TypeArray.getDrawable(i);
				}
				
				TypeArray = getResources().obtainTypedArray(R.array.clothes_s_images_unclick);//抓顏色陣列
				Drawable[] drawableClothKind_g = new Drawable[TypeArray.length()];
				for(int i=0;i<TypeArray.length();i++){
					drawableClothKind_g[i]=TypeArray.getDrawable(i);
				}
				
				TypeArray = getResources().obtainTypedArray(R.array.brandkind_images);//抓顏色陣列
				Drawable[] drawableBrandKind = new Drawable[TypeArray.length()];
				for(int i=0;i<TypeArray.length();i++){
					drawableBrandKind[i]=TypeArray.getDrawable(i);
				}
				
				TypeArray = getResources().obtainTypedArray(R.array.brandkind_unclick);//抓顏色陣列
				Drawable[] drawableBrandKind_g = new Drawable[TypeArray.length()];
				for(int i=0;i<TypeArray.length();i++){
					drawableBrandKind_g[i]=TypeArray.getDrawable(i);
				}
				
				TypeArray = getResources().obtainTypedArray(R.array.color_images);//抓顏色陣列
				Drawable[] drawableColorKind = new Drawable[TypeArray.length()];
				for(int i=0;i<TypeArray.length();i++){
					drawableColorKind[i]=TypeArray.getDrawable(i);
				}
				
				TypeArray = getResources().obtainTypedArray(R.array.color_images_unclick);//抓顏色陣列
				Drawable[] drawableColorKind_g = new Drawable[TypeArray.length()];
				
				for(int i=0;i<TypeArray.length();i++){
					drawableColorKind_g[i]=TypeArray.getDrawable(i);
				}
				
				drawableIcon = new Drawable[][][]{{drawableSex_g,drawableSex}, //前：未選 後：已選
													{drawableClothKind_g,drawableClothKind},
													{drawableColorKind_g,drawableColorKind},
													{drawableBrandKind_g,drawableBrandKind}};
				
													
				for(int i=1;i<IconId.length;i++){
					thisicon=new ImageView[IconId[i].length];
					thisicon[0]=(ImageView)dlg.findViewById(IconId[i][0]);
					
					if(isClick[i][0]==1){
						thisicon[0].setImageResource(R.drawable.all); //可點選 all
						
					}else{
						thisicon[0].setImageResource(R.drawable.all_g); //可點選 all
						
					}
					for(int j = 1;j<IconId[i].length;j++){
						thisicon[j] = (ImageView)dlg.findViewById(IconId[i][j]);
						
						thisicon[j].setOnClickListener(IconOnclick);

						thisicon[j].setImageDrawable(drawableIcon[i] [isClick[i][j]] [j-1]); 
						
					}
					thisicon[0].setOnClickListener(IconAllOnclick);
					Icon[i]=thisicon;
					
				}
				for(int i=0;i<1;i++){
					thisicon=new ImageView[IconId[i].length];
					thisicon[0]=(ImageView)dlg.findViewById(IconId[i][0]);
					if(isClick[i][0]==1){
						thisicon[0].setImageResource(R.drawable.all); //可點選 all
						
					}else{
						thisicon[0].setImageResource(R.drawable.all_g); //可點選 all
						
					}
					
					for(int j = 1;j<IconId[i].length;j++){
						
						thisicon[j] = (ImageView)dlg.findViewById(IconId[i][j]);
						
						thisicon[j].setOnClickListener(IconSexOnclick);
						thisicon[j].setImageDrawable(drawableIcon[i] [isClick[i][j]] [j-1]); 
						
					}
					thisicon[0].setOnClickListener(IconSexOnclick);
					Icon[i]=thisicon;
					
				}
				
				
				
				dlg.show();
			}
			private View.OnClickListener IconOnclick= new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					for(int i=0;i<IconId.length;i++){
						for(int j = 1;j<IconId[i].length;j++){
							if(v.getId() == IconId[i][j]){
								ImageView view =(ImageView)dlg.findViewById(IconId[i][j]);
								
								if(isClick[i][j]==0){
									isClick[i][j]=1;
									count[i] +=1;
								}
								else {
									isClick[i][j]=0;
									count[i] -=1;
								}
								
								
									if(count[i] == IconId[i].length-1){
										count[i]=0;
										}
								
									if(j!=0){
										view.setImageDrawable(drawableIcon[i] [isClick[i][j]] [j-1]);
											if(isClick[i][j] ==1){
											thisicon[0]=(ImageView)dlg.findViewById(IconId[i][0]);
											thisicon[0].setImageResource(R.drawable.all_g); //可點選 all
											isClick[i][0]=0;
											}
											if(count[i]==0){
												for(int k=1;k<IconId[i].length;k++){
													isClick[i][k]=0;
													thisicon[0]=(ImageView)dlg.findViewById(IconId[i][k]);
													thisicon[0].setImageDrawable(drawableIcon[i] [isClick[i][k]] [k-1]); //可點選 all
												}
												thisicon[0]=(ImageView)dlg.findViewById(IconId[i][0]);
												thisicon[0].setImageResource(R.drawable.all); //可點選 all
												isClick[i][0]=1;
											}
									}
								
							}
						}
					}
					
				}
			};
			
			

			private View.OnClickListener IconAllOnclick= new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					for(int i=0;i<IconId.length;i++){
						
							if(v.getId() == IconId[i][0]){
									ImageView view =(ImageView)dlg.findViewById(IconId[i][0]);
									
									thisicon[0]=(ImageView)dlg.findViewById(IconId[i][0]);
									thisicon[0].setImageResource(R.drawable.all); //可點選 all	
									
									if(isClick[i][0]==0){
										
										
										for(int k=1;k<IconId[i].length;k++){
											isClick[i][k] = 0;
											thisicon[0]=(ImageView)dlg.findViewById(IconId[i][k]);
											thisicon[0].setImageDrawable(drawableIcon[i] [isClick[i][k]] [k-1]);
												
										}
											count[i]=0;
											isClick[i][0]=1;
											thisicon[0]=(ImageView)dlg.findViewById(IconId[i][0]);
											thisicon[0].setImageResource(R.drawable.all); //可點選 all
										
									}
								
							}
						
					}
					
				}
			};
			
			private View.OnClickListener IconSexOnclick= new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					

							
						((ImageView)(dlg.findViewById(IconId[0][0]))).setImageResource(R.drawable.all_g); //可點選 all
						isClick[0][0]=0;
						((ImageView)(dlg.findViewById(IconId[0][1]))).setImageDrawable(drawableIcon[0] [0] [0]); 
						isClick[0][1]=0;	
						((ImageView)(dlg.findViewById(IconId[0][2]))).setImageDrawable(drawableIcon[0] [0] [1]); 	
						isClick[0][2]=0;
						
						if (v.getId()==IconId[0][0]){
							((ImageView)(dlg.findViewById(IconId[0][0]))).setImageResource(R.drawable.all); 
							isClick[0][0]=1;
						}else if (v.getId()==IconId[0][1]){
							((ImageView)(dlg.findViewById(IconId[0][1]))).setImageDrawable(drawableIcon[0] [1] [0]); 
							isClick[0][1]=1;	
						}else if (v.getId()==IconId[0][2]){
							((ImageView)(dlg.findViewById(IconId[0][2]))).setImageDrawable(drawableIcon[0] [1] [1]); 	
							isClick[0][2]=1;
						}
				
				}
			};
			
			private AdapterView.OnItemSelectedListener lowerSelect = new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView parent,View v, int position,long id){
					if(lower!= parent.getSelectedItemPosition()){
						lower = parent.getSelectedItemPosition();
		//				selectItem(lower, upper);
						
						
						ArrayList<String> list_upper = new ArrayList<String>();
						for(int i=lower;i<=6;i++){
							list_upper.add(upperPrice[i]);
						}
						
						ArrayAdapter<String> adapter_upper = new ArrayAdapter<String> 
						((CMRegister_Activity)getActivity(),android.R.layout.simple_list_item_1, list_upper);
						Spinner_upper.setAdapter(adapter_upper);
						Spinner_upper.setSelection(upper-lower);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
				
			};

			private AdapterView.OnItemSelectedListener UpperSelect = new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView parent,View v, int position,long id){
					if(upper != parent.getSelectedItemPosition()+lower){
						upper = parent.getSelectedItemPosition()+lower;
						
		//				selectItem(lower, upper);
						
						ArrayList<String> list_lower = new ArrayList<String>();
						for(int i=0;i<=upper;i++){
							list_lower.add(lowerPrice[i]);
						}
						
						ArrayAdapter<String> adapter_upper = new ArrayAdapter<String> 
						((CMRegister_Activity)getActivity(),android.R.layout.simple_list_item_1, list_lower);
						Spinner_lower.setAdapter(adapter_upper);
						Spinner_lower.setSelection(lower);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
				
			};
			
			
			private View.OnClickListener ClearOnClick= new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							for(int i=0;i<IconId.length;i++){
								thisicon[0]=(ImageView)dlg.findViewById(IconId[i][0]);
								thisicon[0].setImageResource(R.drawable.all); //可點選 all	
								
								for(int j = 0;j<IconId[i].length;j++){
									if(j==1){
										isClick[i][j] =0;
									}else{
										isClick[i][j] =1;
									}
									ImageView view =(ImageView)dlg.findViewById(IconId[i][0]);
									if(isClick[i][0]==1){
										for(int k=1;k<IconId[i].length;k++){
											
											isClick[i][k] = 0;
											thisicon[0]=(ImageView)dlg.findViewById(IconId[i][k]);
											thisicon[0].setImageDrawable(drawableIcon[i] [isClick[i][k]] [k-1]);
										}
									}
								}
							}
							
							
							lower=0;
							upper=6;
							list_lower = new ArrayList<String>();
							for(int i=0;i<=upper;i++){
								list_lower.add(lowerPrice[i]);
							}
							
							ArrayAdapter<String> adapter_lower = new ArrayAdapter<String> 
							((CMRegister_Activity)getActivity(),android.R.layout.simple_list_item_1, list_lower);
							Spinner_lower.setAdapter(adapter_lower);
							
						
							list_upper = new ArrayList<String>();
							for(int i=lower;i<=6;i++){
								list_upper.add(upperPrice[i]);
							}
							
							ArrayAdapter<String> adapter_upper = new ArrayAdapter<String> 
							((CMRegister_Activity)getActivity(),android.R.layout.simple_list_item_1, list_upper);
							Spinner_upper.setAdapter(adapter_upper);

							Spinner_lower.setSelection(lower);
							Spinner_upper.setSelection(upper-lower);
							
							
						}
					};
			
			private View.OnClickListener CancelOnclick= new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dlg.dismiss();
				}
			};		
		private Button.OnClickListener SearchOnClick = new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				mTxtSearch.setImageResource(R.drawable.search_icon);
				for(int i=0;i<4;i++){
					if( isClick[i][0] == 0 ){
						mTxtSearch.setImageResource(R.drawable.search_icon2);
					}
				}
				if(lower!=0 || upper!=6){
					mTxtSearch.setImageResource(R.drawable.search_icon2);
				}
				new GetSearchResult().execute();
				}
		};
		class GetSearchResult extends AsyncTask<String, String, String> {//回傳搜尋結果masaki
			
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
				}

				@Override
				protected String doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					JSONObject UploadSearchGroupObject = new JSONObject();
					JSONArray BeaconIDArray = new JSONArray();
					JSONArray SexArray = new JSONArray();
					JSONArray TypeArray = new JSONArray();
					JSONArray ColorArray = new JSONArray();
					JSONArray BrandArray = new JSONArray();
					JSONArray PriceArray = new JSONArray();
					ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
					//陣列int[][] isClick;
					if(isClick[0][0]!=1){//SEX  是ＡＬＬ不設條件直接略過，所以找不等於一
						for(int i = 1 ; i <= 2 ; i++){
							if(isClick[0][i]==1){//如果選到
								String num = String.valueOf(i-1);
								SexArray.put(num);//第一格0是男生 第二格1是女生
							}
						}
					}
					
					if(isClick[1][0]!=1){//TYPE  是ＡＬＬ不設條件直接略過，所以找不等於一
						for(int i = 1 ; i <= 6 ; i++){
							if(isClick[1][i]==1){//如果選到
								String num = String.valueOf(i-1);
								TypeArray.put(num);//第一個TYPE為0
							}
						}
					}
					
					if(isClick[2][0]!=1){//COLOR  是ＡＬＬ不設條件直接略過，所以找不等於一
						for(int i = 1 ; i <= 11 ; i++){
							if(isClick[2][i]==1){//如果選到
								String num = String.valueOf(i-1);
								ColorArray.put(num);//第一個COLOR為0
							}
						}
					}
					
					if(isClick[3][0]!=1){//BRAND  是ＡＬＬ不設條件直接略過，所以找不等於一
						for(int i = 1 ; i <= 17 ; i++){
							if(isClick[3][i]==1){//如果選到
								BrandArray.put(Brand[i-1]);//第一個BRAND為0
							}
						}
					}
					if(lower!=0 || upper!=6){
						PriceArray.put(upper);
						PriceArray.put(lower);
						//Log.d("mylog","price" +upper+" "+lower);
					}
					
					for(String key: beaconMAC.keySet()){
							BeaconIDArray.put(key);
					}
					
					
					try {
						UploadSearchGroupObject.put("BeaconID", BeaconIDArray);
						UploadSearchGroupObject.put("Sex", SexArray);
						UploadSearchGroupObject.put("Type", TypeArray);
						UploadSearchGroupObject.put("Color", ColorArray);
						UploadSearchGroupObject.put("Brand", BrandArray);
						UploadSearchGroupObject.put("Price", PriceArray);
						UploadSearchGroupObject.put("UserID", thisUserID);
						UploadSearchGroupObject.put("isLike", likeClick);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d("mylog","UploadSearchGroupObject:::"+  UploadSearchGroupObject.toString());
					jsonarray.add(new BasicNameValuePair("Search", UploadSearchGroupObject.toString()));// 重要！！
					
					try {
						// Note that create product url accepts POST method

						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/search.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						try {
							String json = EntityUtils.toString(httpEntity);
							Log.d("mylog", "ＪＳＯＮ:::::" + json);
							ReturnInfoJsonobject = new JSONObject(json);
							Log.d("mylog", "ReturnInfoJsonobject:::::" + ReturnInfoJsonobject.toString());
							//Log.d("mylog", ReturnInfoJsonobject.toString());
						} catch (JSONException ex) {
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "error");
					}
					return null;
				}
				
				protected void onPostExecute(String file_url) {
					if(file_url==null){
						try {
							
							JSONArray jsonarray = ReturnInfoJsonobject.getJSONArray("SearchUser");
							String echoCount = ReturnInfoJsonobject.getString("Count");//這裏可能為null和數字
							
							JSONObject SearchUserJSONObject = new JSONObject(); 
							int length = jsonarray.length(); 
							Map<String,String> SearchCustomerId = new HashMap<String,String>(); //建立一個Map介面為CustomerId
							Map<String, Customer> SearchCustomerInfo = new HashMap<String,Customer>();
							for(int i=0;i<length;i++){
								SearchUserJSONObject = jsonarray.getJSONObject(i);//找第i條
								String echoUserID = SearchUserJSONObject.getString("UserID");
								String echoAdID = SearchUserJSONObject.getString("AdID");
								String echoBeaconID = SearchUserJSONObject.getString("BeaconID");
								String website = SearchUserJSONObject.getString("website");
								Customer customer = new Customer(echoUserID,echoAdID, "",0,echoBeaconID,0,website);
								int index=i+1;
								SearchCustomerId.put(String.valueOf(index), echoUserID+echoAdID);
								SearchCustomerInfo.put(echoUserID+echoAdID, customer);
								
							}
							
							customerInfo=SearchCustomerInfo;
							customerId=SearchCustomerId;
							adapter.notifyDataSetChanged();
							if(dlg!=null)
							dlg.cancel();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						adapter.notifyDataSetChanged();
					}
				}
		}
}
	
