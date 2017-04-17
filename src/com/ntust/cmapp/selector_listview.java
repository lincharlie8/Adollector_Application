package com.ntust.cmapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class selector_listview extends BaseAdapter {
	public String[] kind; //類型
	public Drawable[] drawablekind;//類型圖片
	public String sort="cloth";//分類
	Activity myactivity;//主要呼叫的Activity
	private LayoutInflater myInflater;
	private Context mycontext;
	private int length=10;//預設長度10
	private int numofListviewItem=4;
	TypedArray colors ;//抓顏色陣列用的轉換陣列
	int[] color;//抓顏色陣列
	
	public selector_listview(Activity activity,Context c){
		myInflater =LayoutInflater.from(c);
		myactivity=activity;
		mycontext=c;
		setKind(sort);
		
	}
	public String getSort(){//傳回目前分類
		return sort;
	} 
	public void setKind(String string){//設定分類
		sort=string;
		
		if(sort.equals("cloth")){
			length=myactivity.getResources().getInteger(R.integer.clothesSum);//抓取clothSum數值 類型個數
			
		}else if(sort.equals("colors")){
			length=myactivity.getResources().getInteger(R.integer.colorSum);
			
		}else if(sort.equals("brand")){
			length=myactivity.getResources().getInteger(R.integer.brandSum);
			
		}else if(sort.equals("price")){
			length=myactivity.getResources().getInteger(R.integer.PriceSum);
			
		}
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(length%numofListviewItem==0) return length/numofListviewItem;
		else return  (length/numofListviewItem)+1;
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(sort.equals("cloth")||sort.equals("pants")||sort.equals("shoes")||sort.equals("brand")||sort.equals("price")||sort.equals("accessory")){
			return drawablekind[position];
		}
		if(sort.equals("colors")){
			return color[position];
			
		}
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView =myInflater.inflate(R.layout.selector_listview, null);//設定Layout
		ImageView[] img =new ImageView[numofListviewItem];
		img[0] =(ImageView)convertView.findViewById(R.id.selection_image_01);//抓取圖片位置
		img[1] =(ImageView)convertView.findViewById(R.id.selection_image_02);//抓取圖片位置
		img[2] =(ImageView)convertView.findViewById(R.id.selection_image_03);//抓取圖片位置
		img[3] =(ImageView)convertView.findViewById(R.id.selection_image_04);//抓取圖片位置
		TextView[] txtName= new TextView[numofListviewItem];
		txtName[0]=((TextView)convertView.findViewById(R.id.selection_txt_01));	//抓取文字位置
		txtName[1]=((TextView)convertView.findViewById(R.id.selection_txt_02));	//抓取文字位置
		txtName[2]=((TextView)convertView.findViewById(R.id.selection_txt_03));	//抓取文字位置
		txtName[3]=((TextView)convertView.findViewById(R.id.selection_txt_04));	//抓取文字位置
		
		if(sort.equals("cloth")){
			
			kind= new String[length];//重置String陣列
			kind=myactivity.getResources().getStringArray(R.array.clothes);//抓取類型文字陣列
			
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.clothes_s_images);//抓取圖片陣列
			drawablekind=new Drawable[length];//重置Drawable陣列
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);//存入drawable陣列
			}
			
			if(drawablekind!=null && kind!=null){
					
				for( int i=0;i<numofListviewItem;i++){
					if((position*numofListviewItem)+i>=drawablekind.length) break;
					img[i].setImageDrawable(drawablekind[(position*numofListviewItem)+i]);
					txtName[i].setText(kind[(position*numofListviewItem)+i]);
					final String clothindex = String.valueOf((position*numofListviewItem)+i);
					
					img[i].setOnTouchListener( new ImageView.OnTouchListener(){
					
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							ClipData data = ClipData.newPlainText("cloth",
									clothindex);
							v.startDrag(data, new DragShadowBuilder(v), v, 0);
							return true;
						}
				    	
				    });
				}
			
				
				
				return convertView;
				
			}
			
		}else if(sort.equals("colors")){
			
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.colorName);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.color_images);//抓取圖片陣列
			drawablekind=new Drawable[length];//重置Drawable陣列
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);//存入drawable陣列
			}
			TypedArray colors = myactivity.getResources().obtainTypedArray(R.array.clothColors);//抓顏色陣列
			color = new int[colors.length()];
			for(int i=0;i<colors.length();i++){
				color[i]=colors.getColor(i,0);
				
			}
			if(drawablekind!=null && kind!=null){
				
				for( int i=0;i<numofListviewItem;i++){
					if((position*numofListviewItem)+i>=color.length) break;
					img[i].setImageDrawable(drawablekind[(position*numofListviewItem)+i]);
					//img[i].setBackgroundColor(color[(position*numofListviewItem)+i]);
					//設定顯示顏色
					
					txtName[i].setText(kind[(position*numofListviewItem)+i]);
					//Log.d("Mylog",kind[(position*numofListviewItem)+i] );
					final String colorindex= String.valueOf((position*numofListviewItem)+i);
					
					img[i].setOnTouchListener( new ImageView.OnTouchListener(){
					
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							ClipData data = ClipData.newPlainText("color",
									colorindex);
							v.startDrag(data, new DragShadowBuilder(v), v, 0);
							return true;
						}
				    	
				    });
				}
			}
			
			
			return convertView;
			
		}else if(sort.equals("brand")){
			
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.brandName);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.brandkind_images);
			
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
			if(drawablekind!=null && kind!=null){
				
				for( int i=0;i<numofListviewItem;i++){
					if((position*numofListviewItem)+i>=drawablekind.length) break;
					img[i].setImageDrawable(drawablekind[(position*numofListviewItem)+i]);//設定顯示顏色
					
					txtName[i].setText(kind[(position*numofListviewItem)+i]);
					final String brandindex= String.valueOf((position*numofListviewItem)+i);
					
					img[i].setOnTouchListener( new ImageView.OnTouchListener(){
					
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							ClipData data = ClipData.newPlainText("brand",
									brandindex);
							v.startDrag(data, new DragShadowBuilder(v), v, 0);
							return true;
						}
				    	
				    });
				}
			}
			return convertView;
			
		}else if(sort.equals("price")){
			
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.Price_interval);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.price_images);
			
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
			if(drawablekind!=null && kind!=null){
				
				for( int i=0;i<numofListviewItem;i++){
					if((position*numofListviewItem)+i>=drawablekind.length) break;
					//設定顯示顏色
					img[i].setImageDrawable(drawablekind[(position*numofListviewItem)+i]);
					txtName[i].setText("");
					final String catalogindex= String.valueOf((position*numofListviewItem)+i);
					
					img[i].setOnTouchListener( new ImageView.OnTouchListener(){
					
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							ClipData data = ClipData.newPlainText("price",
									catalogindex);
							v.startDrag(data, new DragShadowBuilder(v), v, 0);
							return true;
						}
				    	
				    });
				}
			}
			return convertView;
			
		}
		
		return null;
	}

}
