package com.ntust.cmapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.ntust.cmapp.CMBrowse_Fragment.BarCode;

import android.content.Intent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Information_Fragment extends Fragment {
	ListView minfo_listview,mdeveloper_listview;
	private String[] information ={"Introduction","About Developer","Search Coupon","Search Commission","Log out"};
	private int[] icon = {R.drawable.introduction,R.drawable.developer,R.drawable.coupon,R.drawable.commission,R.drawable.logout};
	private int[] photo = {R.drawable.adidas,R.drawable.adidas,R.drawable.adidas};
	private String[] name={"Charlie","Masaki","Toast"};
	private String[] title={"A","B","C"};
	private String[] expert={"ddd","eee","fff"};
	private String[] hobby={"aaa","aaa","aaa"};
	private String[] contact={"@mail.XXXX","@mail.XXXX","@mail.XXXX"};
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View view =inflater.inflate(R.layout.information_listview,container,false);//設定Layout
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		minfo_listview =(ListView) (getActivity().findViewById(R.id.info_listview));
		Information_Adapter minfo_listviewrAdapter=  new Information_Adapter(getActivity(),0,getActivity());
		minfo_listview.setAdapter(minfo_listviewrAdapter);
		minfo_listview.setOnItemClickListener(itemClick);
	}
	
	class Information_Adapter extends BaseAdapter{
		private LayoutInflater CMInflater;//固定
        private Activity ParentActivity ;

		 public Information_Adapter(Context c,int layoutResource,Activity activity){
	            CMInflater =LayoutInflater.from(c);//指定主layout來源
	            ParentActivity=activity;
	        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return information.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView =CMInflater.inflate(R.layout.information_listview_layout, null);
			ImageView imgicon;
			TextView txtInfo;
			
			
			imgicon = (ImageView)convertView.findViewById(R.id.info_img);
			imgicon.setImageResource(icon[position]);
			txtInfo = (TextView)convertView.findViewById(R.id.info_txt);
			txtInfo.setText(information[position]);
			
			
			return convertView;
		}
		
	}
	
	private ListView.OnItemClickListener itemClick = new ListView.OnItemClickListener() {
		Dialog info_dlg;
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			info_dlg = new Dialog((CMRegister_Activity)getActivity());
			Button mbtnback;
			
			switch (position) {
			case 0:
				info_dlg.setContentView(R.layout.information_dialog);
				info_dlg.setTitle("Introduction");
				info_dlg.setCanceledOnTouchOutside(true);
				info_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
				info_dlg.setCancelable(false);
				
				mbtnback = (Button)info_dlg.findViewById(R.id.btnback);
				
				info_dlg.show();
				mbtnback.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						info_dlg.cancel();
					}
				});
				break;
			case 1:
				
				info_dlg.setContentView(R.layout.develop_listview);
				info_dlg.setTitle("About Devloper");
				info_dlg.setCanceledOnTouchOutside(true);
				info_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
				info_dlg.setCancelable(false);
				mdeveloper_listview =(ListView)info_dlg.findViewById(R.id.developer_listview);
				Developer_listview mdevelop_Adapter_listviewrAdapter=  new Developer_listview(getActivity(),0,getActivity());
				mdeveloper_listview.setAdapter(mdevelop_Adapter_listviewrAdapter);
				mbtnback = (Button)info_dlg.findViewById(R.id.btnback);
				info_dlg.show();
				mbtnback.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						info_dlg.cancel();
					}
				});
				break;
			case 2:
				((CMRegister_Activity)getActivity()).showcouponlistDialog("Coupon");
				break;
			case 3:
				((CMRegister_Activity)getActivity()).showcouponlistDialog("Commision");
				break;
			case 4:
				((CMRegister_Activity)getActivity()).logout();
				break;
			case 5:
				
			
				((CMRegister_Activity)getActivity()).startService();
				break;
			default:
				break;
			}
			
		}
	};
	
	public class Developer_listview extends BaseAdapter{
		private LayoutInflater CMInflater;//固定
        private Activity ParentActivity ;

		 public Developer_listview(Context c,int layoutResource,Activity activity){
	            CMInflater =LayoutInflater.from(c);//指定主layout來源
	            ParentActivity=activity;
	        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 convertView =CMInflater.inflate(R.layout.developer_dialog, null);
			
			
		
			ImageView imgdev = (ImageView)convertView.findViewById(R.id.imgdev);
			imgdev.setImageResource(photo[position]);
			TextView txtdev = (TextView)convertView.findViewById(R.id.txtdev);
			txtdev.setText(name[position] + "\n"
						 	+ "Title:" + title[position] + "\n"
							+ "Expert:" + expert[position] + "\n"
							+ "Hobby:" + hobby[position] + "\n"
							+ "Contact:" + contact[position] + "\n");
			
			return convertView;
		}
		
	}
	
	
	
}