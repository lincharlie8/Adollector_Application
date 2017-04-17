package com.ntust.cmapp;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClothListView_BaeAdater extends BaseAdapter {
	public String[] kind; //類型
	public Drawable[] drawablekind;//類型圖片
	public String sort="cloth";//分類
	Activity myactivity;//主要呼叫的Activity
	private LayoutInflater myInflater;
	private Context mycontext;
	private int length=10;//預設長度10
	TypedArray colors ;//抓顏色陣列用的轉換陣列
	int[] color;//抓顏色陣列
	
	public ClothListView_BaeAdater(Activity activity,Context c){
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
			length=myactivity.getResources().getInteger(R.integer.clothSum);//抓取clothSum數值 類型個數
			
			kind= new String[length];//重置String陣列
			kind=myactivity.getResources().getStringArray(R.array.clothkind);//抓取類型文字陣列
			
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.clothkind_images);//抓取圖片陣列
			drawablekind=new Drawable[length];//重置Drawable陣列
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);//存入drawable陣列
			}
			
		}else if(sort.equals("pants")){
			length=myactivity.getResources().getInteger(R.integer.pantsSum);
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.pantskind);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.pantskind_images);
			
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
		}else if(sort.equals("shoes")){
			length=myactivity.getResources().getInteger(R.integer.shoesSum);
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.shoeskind);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.shoeskind_images);
			
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
		}else if(sort.equals("accessory")){
			length=myactivity.getResources().getInteger(R.integer.accessorySum);
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.accessorykind);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.accessorykind_images);
			
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
		}else if(sort.equals("colors")){
			length=myactivity.getResources().getInteger(R.integer.colorSum);
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.colorName);
			
			TypedArray colors = myactivity.getResources().obtainTypedArray(R.array.clothColors);//抓顏色陣列
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.color_images);
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
			
			color = new int[colors.length()];
			for(int i=0;i<colors.length();i++){
				color[i]=colors.getColor(i,0);
				
			}
		}else if(sort.equals("brand")){
			length=myactivity.getResources().getInteger(R.integer.brandSum);
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.brandName);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.brandkind_images);
			
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
		}else if(sort.equals("price")){
			length=myactivity.getResources().getInteger(R.integer.PriceSum);
			kind= new String[length];
			kind=myactivity.getResources().getStringArray(R.array.Price_interval);
			TypedArray images = myactivity.getResources().obtainTypedArray(R.array.price_images);
			
			drawablekind=new Drawable[length];
			for(int i=0;i<length;i++){
				drawablekind[i]= images.getDrawable(i);
			}
		}
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return length;
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
		convertView =myInflater.inflate(R.layout.selection_lauout, null);//設定Layout
		if(sort.equals("cloth")||sort.equals("pants")||sort.equals("shoes")||sort.equals("accessory")){
			
			if(drawablekind!=null && kind!=null){
				ImageView img =(ImageView)convertView.findViewById(R.id.selection_image);//抓取圖片位置
				TextView txtName=((TextView)convertView.findViewById(R.id.selection_txt));	//抓取文字位置
				img.setImageDrawable(drawablekind[position]);//設定顯示圖片
				txtName.setText(kind[position]);//設定顯示名稱
				img.setOnTouchListener( new ImageView.OnTouchListener(){

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						ClipData data = ClipData.newPlainText("cloth",
								String.valueOf(position) );
						v.startDrag(data, new DragShadowBuilder(v), v, 0);
						return true;
					}
			    	
			    });
				return convertView;
				
			}
			
		}else if(sort.equals("colors")){
			
			
			ImageView img =(ImageView)convertView.findViewById(R.id.selection_image);//抓取圖片位置
			TextView txtName=((TextView)convertView.findViewById(R.id.selection_txt));	//抓取文字位置
			//img.setImageDrawable(drawablekind[position]);//設定顯示圖片
			img.setBackgroundColor(color[position]);//設定顯示顏色
			img.setOnTouchListener( new ImageView.OnTouchListener(){

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						ClipData data = ClipData.newPlainText("color",
								String.valueOf(position) );
						v.startDrag(data, new DragShadowBuilder(v), v, 0);
						return true;
					}
			    	
			    });
			txtName.setText(kind[position]);//設定顯示名稱
			
			return convertView;
			
		}else if(sort.equals("brand")){
			
			
			ImageView img =(ImageView)convertView.findViewById(R.id.selection_image);//抓取圖片位置
			TextView txtName=((TextView)convertView.findViewById(R.id.selection_txt));	//抓取文字位置
			img.setImageDrawable(drawablekind[position]);//設定顯示圖片
			img.setOnTouchListener( new ImageView.OnTouchListener(){

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						ClipData data = ClipData.newPlainText("brand",
								String.valueOf(position) );
						v.startDrag(data, new DragShadowBuilder(v), v, 0);
						return true;
					}
			    	
			    });
			txtName.setText(kind[position]);//設定顯示名稱
			
			return convertView;
			
		}else if(sort.equals("price")){
			
			
			ImageView img =(ImageView)convertView.findViewById(R.id.selection_image);//抓取圖片位置
			TextView txtName=((TextView)convertView.findViewById(R.id.selection_txt));	//抓取文字位置
			img.setImageDrawable(drawablekind[position]);//設定顯示圖片
			img.setOnTouchListener( new ImageView.OnTouchListener(){

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						ClipData data = ClipData.newPlainText("price",
								String.valueOf(position) );
						v.startDrag(data, new DragShadowBuilder(v), v, 0);
						return true;
					}
			    	
			    });
			txtName.setText("");//設定顯示名稱
			
			return convertView;
			
		}
		
		return null;
		
	}

}
