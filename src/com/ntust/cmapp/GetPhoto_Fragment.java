package com.ntust.cmapp;


import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.google.android.gms.location.places.AutocompletePrediction.Substring;
import com.ntust.cmapp.CMBrowse_Fragment.CMAdapeter;
import com.ntust.cmapp.CMBrowse_Fragment.imgDownLoader;
import com.ntust.cmapp.CMSetting_Activity.uploadItem_listview;
import com.ntust.cmapp.R.id;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GetPhoto_Fragment extends Fragment {
	private static final int IMAGE_CAMERA = 100;//使用相機代號
	private static final int IMAGE_FILE = 200;//使用相簿代號
	public Uri mPictureUri; //相片Uri
	private File image;//相片檔
	public Bitmap bmp;//相片顯示物件
	public String UserBeaconMac="",UserID="",AdID="",ClickAD="";
	public Dialog historyDlg ;
	public ArrayList< String> historyAD_ID=new ArrayList<String>();
	public Map< String,String> historyAD_UUID=new HashMap<String,String>();
	public Map<String,Bitmap> historyAd_photo = new HashMap<String,Bitmap>();
	public Map<String, ImageView> historyAd_imageview =new HashMap<String, ImageView>();
	showHistoryAD_listview adapter;
	ListView historyAdListView ;
	public boolean isCancelled=false;
	
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View view =inflater.inflate(R.layout.getphoto_fragment_layout,container,false);//設定Layout
		return view;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		isCancelled=false;
		super.onStart();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		isCancelled=true;
		historyAd_photo.clear();
		super.onStop();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		ImageView btGallery=(ImageView)getActivity().findViewById(R.id.CreateAD);//使用相簿按鍵
		btGallery.setOnClickListener(btListener);//使用相簿按鍵偵測
		try {
			Bundle extras = getArguments();
			if(extras!=null){
				UserBeaconMac=extras.getString("BeaconID");
				UserID=extras.getString("UserID");
				AdID=extras.getString("AdID");
				ClickAD=AdID;
			
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Mylog", "onGetBeaconID Error");
		}
		/*if (!AdID.equals("")){
			imgDownLoader id =new imgDownLoader((ImageView)getActivity().findViewById(R.id.getPhotoPicture),UserID+AdID);
			id.execute();
		}*/
		
	}
	
	public void showDialog(){
		
		final Dialog dlg =new Dialog((CMRegister_Activity)getActivity());
		//dlg.setTitle("Create a new AD!");
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		dlg.setContentView(R.layout.selectphoto_dialog);
		dlg.setCanceledOnTouchOutside(true);
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		ImageView fromCamera =(ImageView)dlg.findViewById(R.id.getPhotoFromCamera);
		ImageView fromAlbum =(ImageView)dlg.findViewById(R.id.getPhotoFromAlbum);
		ImageView fromHistory =(ImageView)dlg.findViewById(R.id.getPhotoFromHistory);
		fromCamera.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				launchChooser("Camera");
				dlg.cancel();
			}
			
		});
		fromAlbum.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				launchChooser("Gallery");
				dlg.cancel();
			}
			
		});
		fromHistory.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//launchChooser("Gallery");
				showHistoryAD_Dialog();
				
				//dlg.cancel();
			}
			
		});
		
		dlg.show();
	}
	
	public void showHistoryDialog(final String ADID,String title,final String status){
		
		final Dialog dlg =new Dialog((CMRegister_Activity)getActivity());
		//dlg.setTitle("Create a new AD!");
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		dlg.setContentView(R.layout.history_ad_click_dialog);
		dlg.setCanceledOnTouchOutside(true);
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		ImageView clickADImg =(ImageView)dlg.findViewById(R.id.selectedHistoryAd);
		TextView dialog_title =(TextView)dlg.findViewById(R.id.historyAdTxt);
		clickADImg.setImageResource(R.drawable.loading);
		if(historyAd_photo.get(ADID.substring(10))!=null){
			clickADImg.setImageBitmap(historyAd_photo.get(ADID.substring(10)));
		}else{
			imgDownLoader id =new imgDownLoader(clickADImg,ADID,true);
			id.execute();
		}
		
		dialog_title.setText(title);
		dlg.findViewById(R.id.historyDialogCancel).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dlg.cancel();
			}
			
		});
		dlg.findViewById(R.id.historyDialogOK).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//launchChooser("Gallery");
				
				((CMRegister_Activity)getActivity()).findBeacon(true, historyAD_UUID.get(ClickAD));
				new SetHistoryAD(UserID).execute();
	
				dlg.cancel();
				historyDlg.cancel();
				
			}
			
		});
		dlg.findViewById(R.id.historyDialogDelete).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//launchChooser("Gallery");

					new DeleteHistoryAD(UserID, ADID.substring(10)).execute();

				dlg.cancel();
				
			}
			
		});
		
		dlg.show();
	}
	private Button.OnClickListener btListener =new  Button.OnClickListener(){//按鍵偵測
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		
			if(v.getId()==R.id.CreateAD){//按相簿
				
				showDialog();
			}
		}
		
		
	};
	
	private void launchChooser(String type) {  //案件處理
		   
	    if(type.equals("Camera")){//按相機
	    	Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );  //新增相機Intent
	    	String text = (String) DateFormat.format("yyyyMMddkkmmss", Calendar.getInstance());//設定相片檔案名稱 
	    	image=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),text+".jpg");//設定相片路徑
	    	i2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));  //將相片路徑加到Intent
	    	
	    	mPictureUri=Uri.fromFile(image);//儲存路徑
	    	startActivityForResult(i2, IMAGE_CAMERA);//執行相機 
	    	
	    }
	    if(type.equals("Gallery")){//按相簿
	    	Intent intent = new Intent(//新增相簿Intent
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");//抓取圖片
			startActivityForResult(//執行相簿  
					Intent.createChooser(intent, "Select File"),
					IMAGE_FILE);
	    }
	}  
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 
		Intent intent = new Intent();//準備切換到CMSetting Activity 的Intent
		Bundle bundle = new Bundle();//準備傳給CMSetting Activity 的Bundle
		intent.setClass(getActivity(), CMSetting_Activity.class);//設定要開啟的Activity
			
	    if (requestCode == IMAGE_CAMERA  && resultCode == Activity.RESULT_OK) {  //相機
	    	bundle.putString("type", "Camera");//存入方式 相機
	    	bundle.putString("data", mPictureUri.toString());//存入路徑
	    	bundle.putString("path", mPictureUri.getPath());
	    	bundle.putString("BeaconID", UserBeaconMac);
	    	bundle.putString("UserID", UserID);
	    	
	    	intent.putExtras(bundle);
			startActivity(intent);//執行Activity
			
	    }else if (requestCode == IMAGE_FILE && resultCode == Activity.RESULT_OK) {  //相簿
	    	bundle.putString("type", "Gallery");//存入方式 相簿
	    	Uri selectedImageUri = data.getData();//抓取傳回的路徑
	  		bundle.putString("data", selectedImageUri.toString());//存入路徑
	  		bundle.putString("path", selectedImageUri.getPath());//存入路徑
	  		bundle.putString("BeaconID", UserBeaconMac);
	  		bundle.putString("UserID", UserID);
	  		intent.putExtras(bundle);
			startActivity(intent);//執行Activity
	    }  
	    return;
	    
	    
	} 
	
	public void showHistoryAD_Dialog(){
		
		historyDlg =new Dialog((CMRegister_Activity)getActivity());
		historyDlg.setTitle("Click to Set Your AD");
		//
  
		historyDlg.setContentView(R.layout.history_ad_dialog);
		//historyDlg.setCanceledOnTouchOutside(true);
		historyDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		historyDlg.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		Button btnCancle =(Button)historyDlg.findViewById(R.id.btnDeleteCancel);
		
		btnCancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isCancelled=true;
				historyDlg.cancel();
			}
		});
		
		ClickAD= UserID+GetPhoto_Fragment.this.AdID;
		isCancelled=false;
		historyDlg.show();
		new GetHistoryAD(UserID).execute();
	}
	
	public class showHistoryAD_listview extends BaseAdapter {
    	
    	private Drawable[] drawablekind;//類型圖片
    	Activity myactivity;//主要呼叫的Activity
    	private LayoutInflater myInflater;
    	private Context mycontext;
    	private int length=0;//預設長度10
    	
    	
    	public showHistoryAD_listview(int length,Context c){
    		myInflater =LayoutInflater.from(c);
    		this.length=historyAD_ID.size();
    		
    	}
    	
    	@Override
		public int getCount() {
    		this.length=historyAD_ID.size();
			// TODO Auto-generated method stub
    		if(length==0) return 0;
    		if(length%3==0) return length/3;
    		else return (length/3 ) + 1;
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
    		convertView =myInflater.inflate(R.layout.history_ad_listview, null);//設定Layout
    		final View v=convertView;
    		ImageView[] img =new ImageView[3];
    		img[0]=(ImageView)convertView.findViewById(R.id.historyphoto1);
    		img[1]=(ImageView)convertView.findViewById(R.id.historyphoto2);
    		img[2]=(ImageView)convertView.findViewById(R.id.historyphoto3);
    		for( int i=0;i<3;i++){
    			if(position*3+i>=length) break;
    			String listview_AdID=historyAD_ID.get(position*3+i);
    			
    			
    			img[i].setImageResource(R.drawable.loading);
    			//img[i]
    			if(historyAd_photo.get(listview_AdID)==null){
    				
    				imgDownLoader id =new imgDownLoader(img[i],listview_AdID);
 					id.execute();
    			}else{
    				if(listview_AdID.equals(UserID+GetPhoto_Fragment.this.AdID) && ClickAD.equals(listview_AdID) ){
    					img[i].setBackgroundResource(R.color.select);
        			}else{
        				
        				if(ClickAD.equals(listview_AdID)){
        					img[i].setBackgroundResource(R.color.select);
        				}
        			}
    				img[i].setImageBitmap(historyAd_photo.get(listview_AdID));
    			}
    		
    		
    			final int j= i;
    			img[i].setOnClickListener(new View.OnClickListener() {
    				
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ClickAD=String.valueOf(historyAD_ID.get(position*3+j));
						showHistoryDialog(ClickAD, "", "change");
						
					
					
					}
				});
    		/*	img[i].setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						showHistoryDialog(historyAD_ID.get(position*3+j), "Do you Want to Delete this AD?", "delete");
						return true;
					}
				});*/
    			
    		}
    		
    		
    		return convertView;

    	}
   	 
		

    }
	
	 class GetHistoryAD extends AsyncTask<String, String, String> {
	    	private String UserID="";
	    	JSONObject ReturnAdIDJsonobject = new JSONObject();
	    	 public GetHistoryAD(String userID) {
				// TODO Auto-generated constructor stub
	    		 this.UserID=userID;
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
				Log.d("mylog", "getHistoryAd Now");
				JSONObject UserIDObject = new JSONObject();
				
			
				
				
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				
				
				try {
					UserIDObject.put("UserID", this.UserID);
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					
					
					jsonarray.add(new BasicNameValuePair("UserID", UserIDObject.toString()));// 重要！！
					Log.d("mylog", "output:"+jsonarray.toString());
					try {
						// Note that create product url accepts POST method

						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/get_all_Ad.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						try {
							String json = EntityUtils.toString(httpEntity);					
							ReturnAdIDJsonobject = new JSONObject(json);
							Log.d("mylog", "getAllAd:"+ReturnAdIDJsonobject.toString());
						} catch (JSONException ex) {
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "GetAllAd Error");
					}
					return null;
					
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				
					
					JSONObject PartItemjsonobject = new JSONObject(); 
					try {
						JSONArray jsonarray = ReturnAdIDJsonobject.getJSONArray("AD");
						//JSONArray uuidjsonarray = ReturnAdIDJsonobject.getJSONArray("UUID");
						for(int i=0;i<jsonarray.length();i++){
							PartItemjsonobject=jsonarray.getJSONObject(i);//找第j條
							if(!historyAD_ID.contains(UserID+PartItemjsonobject.getString("AdID"))){
								historyAD_ID.add(UserID+PartItemjsonobject.getString("AdID"));
								historyAD_UUID.put(UserID+PartItemjsonobject.getString("AdID"), PartItemjsonobject.getString("UUID"));
							}
							Log.d("mylog",historyAD_ID.get(i) );
						}
						
						historyAdListView =(ListView)historyDlg.findViewById(R.id.history_Listview);
						adapter =new showHistoryAD_listview(historyAD_ID.size(),(CMRegister_Activity)getActivity());
						historyAdListView.setAdapter(adapter);
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						historyDlg.cancel();
					}
					
					
					
				}

		}
	 class SetHistoryAD extends AsyncTask<String, String, String> {
	    	private String UserID="";
	    	JSONObject ReturnAdIDJsonobject = new JSONObject();
	    	 public SetHistoryAD(String userID) {
				// TODO Auto-generated constructor stub
	    		 this.UserID=userID;
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
				JSONObject UserIDObject = new JSONObject();
				
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				
				try {
					UserIDObject.put("UserID", this.UserID);
					UserIDObject.put("AdID", ClickAD.substring(10));
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					
					
					jsonarray.add(new BasicNameValuePair("ChangeData", UserIDObject.toString()));// 重要！！
					Log.d("mylog", "output:"+jsonarray.toString());
					try {
						// Note that create product url accepts POST method

						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/Change_Ad.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						
							String echo = EntityUtils.toString(httpEntity);					
							return echo;
							
						
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "Change Error");
					}
					return null;
					
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String echo) {
				// dismiss the dialog once done
				
					
					
				if(echo.equals("1")){
					AdID=ClickAD.substring(10);
					Log.d("mylog","Change Scuess!"+ClickAD);
					
					Log.d("mylog", ClickAD);
					adapter.notifyDataSetChanged();
				
					
				}	
					
					
					
			}

		}
	 class DeleteHistoryAD extends AsyncTask<String, String, String> {
	    	private String UserID="",AdID="";
	    	
	    	JSONObject ReturnAdIDJsonobject = new JSONObject();
	    	 public DeleteHistoryAD(String userID,String AdID) {
				// TODO Auto-generated constructor stub
	    		 this.UserID=userID;
	    		 this.AdID=AdID;
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
				Log.d("mylog", "getHistoryAd Now");
				JSONObject UserIDObject = new JSONObject();
				
				ArrayList<NameValuePair> jsonarray = new ArrayList<NameValuePair>();
				
				try {
					UserIDObject.put("UserID", this.UserID);
					UserIDObject.put("AdID",  this.AdID);
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					
					
					jsonarray.add(new BasicNameValuePair("DeleteData", UserIDObject.toString()));// 重要！！
					Log.d("mylog", "output:"+jsonarray.toString());
					try {
						// Note that create product url accepts POST method

						HttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://cmapp.nado.tw/android_connect_user/Delete_Ad.php");
						httpPost.setEntity(new UrlEncodedFormEntity(jsonarray,HTTP.UTF_8));
						HttpResponse httpResponse = httpClient.execute(httpPost); 
						HttpEntity httpEntity = httpResponse.getEntity();
						
						
							String echo = EntityUtils.toString(httpEntity);					
							return echo;
							
						
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mylog", "Delete Error");
					}
					return null;
					
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String echo) {
				// dismiss the dialog once done
				
					
					
				if(echo.equals("1")){
					historyAd_photo.remove(this.UserID+this.AdID);
					historyAD_ID.remove(this.UserID+this.AdID);
					adapter.notifyDataSetChanged();
					Log.d("mylog","Remove Scuess!"+this.AdID);
				}
					
					
					
			}

		}
	 public class imgDownLoader extends AsyncTask<String, Integer, Bitmap> {
			
			private ImageView imgView;
			private String ADID;
			private Boolean isBigPhoto=false;
			
			 public imgDownLoader(ImageView imgView,String ADID) {
			        super();
			        this.imgView=imgView;
			        this.ADID=ADID;
			        isBigPhoto=false;
			        
			    }
			 public imgDownLoader(ImageView imgView,String ADID,Boolean  isBigPhoto) {
			        super();
			        this.imgView=imgView;
			        this.ADID=ADID;
			        this.isBigPhoto=isBigPhoto;
			        
			    }
			protected void onPreExecute() {
			}
			@Override
			protected Bitmap doInBackground(String... params) {
				// TODO Auto-generated method stub
				while (true)
				   {
					if (isCancelled){
				    	  break;
				    	  
				      }  
					try {
						URL url;
						Bitmap outBitmap;
						if(isBigPhoto){
							url=new URL("http://cmapp.nado.tw/photo/"+ADID+".jpg");
						}else{
							url=new URL("http://cmapp.nado.tw/photo/"+ADID+"_s.jpg");
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
						Log.d("mylog","Faild to load Img!"+ADID);
						return null;
						// TODO: handle exception
					}
				      
				        
				   } 
				
				return null;
			}
			 @Override
			    protected void onProgressUpdate(Integer... progress) {
			        //progressDialog_.incrementProgressBy(progress[0]);
			    }
			
			protected void onPostExecute(Bitmap img) {
				if(img!=null){
					if(isBigPhoto){
						historyAd_photo.put(ADID.substring(10),img);
						imgView.setImageBitmap(img);
					}else{
						
				
						if(this.ADID.equals(UserID+GetPhoto_Fragment.this.AdID) && ClickAD.equals(ADID)){
							imgView.setBackgroundResource(R.color.select);
		    			}
						historyAd_photo.put(ADID,img);
						imgView.setImageBitmap(img);
					}
				}else{
					//Bitmap bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.loading);
					//historyAd_photo.put(ADID,bitmap);
					
				}
				
			
			
				
			}
			
		
	}

}
