package com.ntust.cmapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cmappTest_Activity extends Activity {
    Map<String,String> CustomerId = new HashMap<String,String>(); //建立一個Map介面為CustomerId
    //服飾Item
    Map<String,String> ItemId = new HashMap<String,String>();
    ArrayList<String> customerItemId = new ArrayList<String>();
    Map<String,ArrayList<String>> customerItemListMap = new HashMap<String,ArrayList<String>>();
    Map<String,String> ItemWidth = new HashMap<String,String>();
    Map<String,String> ItemHeight= new HashMap<String,String>();
    Map<String,String> ItemTopMargin = new HashMap<String,String>();
    Map<String,String> ItemLeftMargin = new HashMap<String,String>();
    Map<String,String> ItemRotation= new HashMap<String,String>();
    Map<String,String> ItemType = new HashMap<String,String>();
    Map<String,String> ItemStyle = new HashMap<String,String>();
    Map<String,String> ItemColor = new HashMap<String,String>();
    Map<String,String> ItemBrand = new HashMap<String,String>();
    Map<String,String> ItemSex = new HashMap<String,String>();
    private ListView mListView;
    int screen_width;
    int UserID = 0;
    String[] Brand ={"uniqlo"," lativ"," net","  obdesign","  caco","  Others"}; //Brand陣列
    String[] Type={"hat","  clothes","  pants","dress","  Others"};//Type陣列
    String[] Style={"cap","  shirt","  suit","  shorts","pants"," Others"};//Style陣列
    String[] Color={"Red","Orange","Yellow","Green","Blue","Deep-Blue","Purple","Black","White","Brown","Others"};//Color陣列
    String[] Sex={"Male","Female","Children"}; //Sex陣列(男/女/童)
  //  Integer[] mImgArr={R.drawable.hat1,R.drawable.shirt1,R.drawable.pants1}; //服裝Icon
    //Integer[] mBrand={R.drawable.uniqlo,R.drawable.lativ,R.drawable.net,R.drawable.obdesign,R.drawable.caco};//檔名和Brand陣列名稱一致
    CMAdapeter adapter;
    String customerPhotoID;
    String CustomerItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmapptest_layout);//使用設有listview的layout
        mListView=(ListView)findViewById(R.id.cmlistview);//抓到畫面上listview的位置

        int customerno = 3;
        for(int i=1;i<=customerno;i++){
            String customerPhotoID = String.valueOf(i);
            CustomerId.put(customerPhotoID, customerPhotoID); //設定photo的id&值

            int itemID = 1;
            CustomerItemId = customerPhotoID + ( itemID<10 ? ("0"+itemID) : itemID);

            ItemId.put(CustomerItemId, "101");
            ItemWidth.put(CustomerItemId,"100");
            ItemHeight.put(CustomerItemId,"100");
            ItemTopMargin.put(CustomerItemId,"-5");
            ItemLeftMargin.put(CustomerItemId,"280");
            ItemRotation.put(CustomerItemId,"0");
            ItemColor.put(CustomerItemId,"1");
            ItemBrand.put(CustomerItemId,"0");
            ItemType.put(CustomerItemId,"0");
            ItemStyle.put(CustomerItemId,"0");
            ItemSex.put(CustomerItemId,"0");
            customerItemId.add(CustomerItemId);

            itemID ++;
            CustomerItemId = customerPhotoID +( itemID<10 ? ("0"+itemID) : itemID);

            ItemId.put(CustomerItemId, "102");

            ItemWidth.put(CustomerItemId,"300");
            ItemHeight.put(CustomerItemId,"300");
            ItemTopMargin.put(CustomerItemId,"150");
            ItemLeftMargin.put(CustomerItemId,"200");
            ItemRotation.put(CustomerItemId,"-5");
            ItemColor.put(CustomerItemId,"3");
            ItemBrand.put(CustomerItemId,"1");
            ItemType.put(CustomerItemId,"1");
            ItemStyle.put(CustomerItemId,"1");
            ItemSex.put(CustomerItemId,"1");
            customerItemId.add(CustomerItemId);
            itemID ++;
            CustomerItemId = customerPhotoID +( itemID<10 ? ("0"+itemID) : itemID);

            ItemId.put(CustomerItemId, "103");
            ItemWidth.put(CustomerItemId,"400");
            ItemHeight.put(CustomerItemId,"400");
            ItemTopMargin.put(CustomerItemId,"400");
            ItemLeftMargin.put(CustomerItemId,"150");
            ItemRotation.put(CustomerItemId,"0");
            ItemColor.put(CustomerItemId,"9");
            ItemBrand.put(CustomerItemId,"2");
            ItemType.put(CustomerItemId,"2");
            ItemStyle.put(CustomerItemId,"5");
            ItemSex.put(CustomerItemId,"2");
            customerItemListMap.put(customerPhotoID,customerItemId);
            customerItemId.add(CustomerItemId);
            customerItemId = new ArrayList<String>();
            UserID=i;
        }

    }
    @Override
    public void onWindowFocusChanged(boolean hasFpcus){
        super.onWindowFocusChanged(hasFpcus);
        ListView out_layout = (ListView)findViewById(R.id.cmlistview);
        screen_width = out_layout.getWidth();
       adapter=new CMAdapeter(this,R.layout.cmapp_listview,this);//建立新類別物件
        mListView.setAdapter(adapter);	//讓listview使用此物件顯示
    }
    public void reflashAdapter(){
        adapter.notifyDataSetChanged();
    }
    public void addAd(){
        UserID++;
            String customerPhotoID = String.valueOf(UserID);

        int itemID = 1;
        CustomerItemId = customerPhotoID + ( itemID<10 ? ("0"+itemID) : itemID);

        ItemId.put(CustomerItemId, "101");
        ItemWidth.put(CustomerItemId,"100");
        ItemHeight.put(CustomerItemId,"100");
        ItemTopMargin.put(CustomerItemId,"-5");
        ItemLeftMargin.put(CustomerItemId,"280");
        ItemRotation.put(CustomerItemId,"0");
        ItemColor.put(CustomerItemId,"1");
        ItemBrand.put(CustomerItemId,"0");
        ItemType.put(CustomerItemId,"0");
        ItemStyle.put(CustomerItemId,"0");
        ItemSex.put(CustomerItemId,"0");
        customerItemId.add(CustomerItemId);

        itemID ++;
        CustomerItemId = customerPhotoID +( itemID<10 ? ("0"+itemID) : itemID);

        ItemId.put(CustomerItemId, "102");

        ItemWidth.put(CustomerItemId,"300");
        ItemHeight.put(CustomerItemId,"300");
        ItemTopMargin.put(CustomerItemId,"150");
        ItemLeftMargin.put(CustomerItemId,"200");
        ItemRotation.put(CustomerItemId,"-5");
        ItemColor.put(CustomerItemId,"3");
        ItemBrand.put(CustomerItemId,"1");
        ItemType.put(CustomerItemId,"1");
        ItemStyle.put(CustomerItemId,"1");
        ItemSex.put(CustomerItemId,"1");
        customerItemId.add(CustomerItemId);
        itemID ++;
        CustomerItemId = customerPhotoID +( itemID<10 ? ("0"+itemID) : itemID);

        ItemId.put(CustomerItemId, "103");
        ItemWidth.put(CustomerItemId,"400");
        ItemHeight.put(CustomerItemId,"400");
        ItemTopMargin.put(CustomerItemId,"400");
        ItemLeftMargin.put(CustomerItemId,"150");
        ItemRotation.put(CustomerItemId,"0");
        ItemColor.put(CustomerItemId,"9");
        ItemBrand.put(CustomerItemId,"2");
        ItemType.put(CustomerItemId,"2");
        ItemStyle.put(CustomerItemId,"5");
        ItemSex.put(CustomerItemId,"2");
        customerItemListMap.put(customerPhotoID,customerItemId);
        customerItemId.add(CustomerItemId);
        customerItemId = new ArrayList<String>();
        CustomerId.put(customerPhotoID, customerPhotoID); //設定photo的id&值

    }
    public class CMAdapeter extends BaseAdapter{//建立adapter類別
        private LayoutInflater CMInflater;//固定
        private Activity ParentActivity ;

        public CMAdapeter(Context c,int layoutResource,Activity activity){
            CMInflater =LayoutInflater.from(c);//指定主layout來源
            ParentActivity=activity;
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
            return CustomerId.size();
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent){
            String customerindex = String.valueOf(position+1);
            convertView =CMInflater.inflate(R.layout.cmapp_listview, null);//指定listview的layout

            RelativeLayout main_layout =(RelativeLayout)convertView.findViewById(R.id.main_layout);
            ImageView mImgPhoto =(ImageView)convertView.findViewById(R.id.photo);//顯示使用者照片
         //   mImgPhoto.setImageResource(R.drawable.photo);

            ArrayList<String> thisList =customerItemListMap.get(CustomerId.get(customerindex));
            LinearLayout logo_layout =(LinearLayout)convertView.findViewById(R.id.logo_layout);

            int icon_width = (screen_width/3);
            RelativeLayout.LayoutParams frame_icon = new RelativeLayout.LayoutParams(icon_width,100);

            for(int i=0;i<thisList.size();i++){
                final String listitem =thisList.get(i);
                RelativeLayout.LayoutParams margin_main = new RelativeLayout.LayoutParams(Integer.parseInt(ItemWidth.get(listitem)), Integer.parseInt(ItemHeight.get(listitem)));  //設定背景長寬
                ImageView mImgClothes = new ImageView(ParentActivity); //Item
                margin_main.topMargin=(Integer.parseInt(ItemTopMargin.get(listitem)));
                margin_main.leftMargin=(Integer.parseInt(ItemLeftMargin.get(listitem)));
                mImgClothes.setRotation(Integer.parseInt(ItemRotation.get(listitem)));
                mImgClothes.setLayoutParams(margin_main);
               // mImgClothes.setImageResource(mImgArr[i]);
                final Toast item_toast = new Toast(ParentActivity);
                mImgClothes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item_toast.setGravity(Gravity.CENTER,Integer.parseInt(ItemLeftMargin.get(listitem)),Integer.parseInt(ItemTopMargin.get(listitem)));
                        item_toast.makeText(ParentActivity,"Brand:"+Brand[Integer.parseInt(ItemBrand.get(listitem))] +"\n"
                                                          +"Type:"+ Type[Integer.parseInt(ItemType.get(listitem))] +"\n"
                                                          +"Style:"+ Style[Integer.parseInt(ItemStyle.get(listitem))]+"\n"
                                                          +"Color:"+ Color[Integer.parseInt(ItemColor.get(listitem))]+"\n"
                                                          +"Classification:" + Sex[Integer.parseInt(ItemSex.get(listitem))] ,Toast.LENGTH_SHORT).show();
                    }
                });
                main_layout.addView(mImgClothes);

                ImageView mLogoIcon =new ImageView(ParentActivity);//LogoIcon
                 mLogoIcon.setId(Integer.parseInt(ItemId.get(listitem)));
                 mLogoIcon.setRotation(0);
          //      Bitmap logo_icon = BitmapFactory.decodeResource(getResources(), mBrand[Integer.parseInt(ItemBrand.get(listitem))]);
             //    mLogoIcon.setImageBitmap(getCircleBitmap(logo_icon));
                 mLogoIcon.setLayoutParams(frame_icon);
                 mLogoIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item_toast.setGravity(Gravity.CENTER,Integer.parseInt(ItemLeftMargin.get(listitem)),Integer.parseInt(ItemTopMargin.get(listitem)));
                        item_toast.makeText(ParentActivity,
                        String.valueOf(CustomerId.size()),Toast.LENGTH_SHORT).show();
                                                        /*
                                "Type:"+ Type[Integer.parseInt(ItemType.get(listitem))] +"\n"
                                +"Style:"+ Style[Integer.parseInt(ItemStyle.get(listitem))]+"\n"
                                +"Color:"+ Color[Integer.parseInt(ItemColor.get(listitem))]+"\n"
                                +"Classification:" + Sex[Integer.parseInt(ItemSex.get(listitem))] */
                        addAd();
                        reflashAdapter();
                    }
                });
                 logo_layout.addView(mLogoIcon);
            }
            return convertView;//回傳給adapter
        }
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();
        return output;
    }
}