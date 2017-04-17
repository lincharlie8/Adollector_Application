package com.ntust.cmapp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;

public class Customer implements Serializable{
	public String customerName="",AdId="",customerId="",itemBrand="",itemUrl,BeaconId="",BrandItemName="",BrandItemID="",customerwesite="";
	public int itemIndex=0,itemType=-1,itemColor=-1,itemSex=-1,itemPrice=-1,likeClike=0,isOftenUse=0,isBrandItem=0;
	public float itemWidth=-1f,itemHeight=-1f,itemTopMargin=-1f,itemLeftMargin=-1f; 
	
	
	public Customer(float itemWidth,float itemHeight,float itemTopMargin,float itemLeftMargin,
			       String itemBrand,int itemType,int itemColor,int itemSex ,int itemPrice,String itemUrl,int itemIndex,int isBrandItem){
		   this.customerId = customerId;
		   this.itemWidth=itemWidth;
		   this.itemHeight=itemHeight;
		   this.itemTopMargin=itemTopMargin;
		   this.itemLeftMargin=itemLeftMargin;
		   this.itemBrand=itemBrand;
		   this.itemType=itemType;
		   this.itemColor=itemColor;
		   this.itemSex=itemSex;
		   this.itemPrice=itemPrice;
		   this.itemIndex=itemIndex;
		   this.isBrandItem=isBrandItem;
		   setUrl(itemUrl);
	}
	public Customer(String BrandItemID,String BrandItemName,int itemPrice,String itemBrand){
	   this.BrandItemID = BrandItemID;
	   this.BrandItemName=BrandItemName;
	   this.itemPrice=itemPrice;
	   this.itemBrand=itemBrand;
	 
	}
	public Customer(String customerId,String AdId,String customerName,int customerSex,String BeaconId ,int likeClike,String customerwesite){
		this.customerId=customerId;
		this.AdId=AdId;
		this.customerName = customerName;
		this.itemSex=customerSex;
		this.BeaconId = BeaconId;
		this.likeClike=likeClike;
		if(!customerwesite.equals("")&&!customerwesite.equals(null)){
			sewebsitetUrl(customerwesite);
		}
		
	}
	public Customer(String clothid) {
		// TODO Auto-generated constructor stub
			this.customerId = clothid;
		   this.itemWidth=-1f;
		   this.itemHeight=-1f;
		   this.itemTopMargin=-1f;
		   this.itemLeftMargin=-1f;
		   this.itemBrand="";
		   this.itemType=0;
		   this.itemColor=0;
		   this.itemSex=0;
		   this.itemPrice=0;
	}
	
	public void setType(int itemType){
		  this.itemType=itemType;
	}
	public void setBrand(String itemBrand){
		this.itemBrand=itemBrand;
	}
	public void setColor(int itemColor){
		this.itemColor=itemColor;
	}
	public void setIsOftenUse(int isOftenUse){
		this.isOftenUse=isOftenUse;
	}
	public void setAttr(float itemWidth,float itemHeight,float itemTopMargin,float itemLeftMargin){
		  this.itemWidth=itemWidth;
		   this.itemHeight=itemHeight;
		   this.itemTopMargin=itemTopMargin;
		   this.itemLeftMargin=itemLeftMargin;
	}
	public void setPrice(int itemPrice){
		this.itemPrice=itemPrice;
	}
	public void setUrl(String itemUrl){
		if(itemUrl.indexOf("http://")!=-1){
			this.itemUrl=itemUrl;
		}else{
			this.itemUrl="http://"+itemUrl;
		}
	}
	public void sewebsitetUrl(String itemUrl){
		if(itemUrl.indexOf("http://")!=-1 || itemUrl.indexOf("https://")!=-1){
			this.customerwesite=itemUrl;
		}else{
			this.customerwesite="http://"+itemUrl;
		}
	}
	public void setLikeClick(int likeClike){
		this.likeClike=likeClike;
	}
	
}

