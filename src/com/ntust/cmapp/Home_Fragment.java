package com.ntust.cmapp;


import java.io.File;
import java.util.Calendar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Home_Fragment extends Fragment {
	private static final int IMAGE_CAMERA = 100;//使用相機代號
	private static final int IMAGE_FILE = 200;//使用相簿代號
	public Uri mPictureUri; //相片Uri
	private File image;//相片檔
	public Bitmap bmp;//相片顯示物件
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View view =inflater.inflate(R.layout.home_fragment_layout,container,false);//設定Layout
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	
	}
	
	
	
	
	
	
	
	
}
