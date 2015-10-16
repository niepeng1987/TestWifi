package com.example.testwifi;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class WifiNameAdapter extends BaseAdapter {

	private List<ScanWifiInfo> mScanWifiNameList = new ArrayList<ScanWifiInfo>();
	private LayoutInflater mInflater = null;
	private String mWifiName = "";
	private Drawable mWifiCipherTypeIcon = null;
	private WifiMar mWifiMar = null;
	 
	public WifiNameAdapter(Context context, ArrayList<ScanWifiInfo> scanWifiNameList) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.mScanWifiNameList = scanWifiNameList;
		mWifiCipherTypeIcon = context.getResources().getDrawable(R.drawable.startconfig_image4);
		mWifiCipherTypeIcon.setBounds(0, 0, mWifiCipherTypeIcon.getMinimumWidth(), mWifiCipherTypeIcon.getMinimumHeight());
		mWifiMar = new WifiMar(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mScanWifiNameList != null){
			return mScanWifiNameList.size();
		}else{
			return 0;
		}
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mScanWifiNameList != null){
			return mScanWifiNameList.get(position);
		}else{
			return null;
		}
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void setCurrInputName(String wifiName){
		this.mWifiName = wifiName;
	}
	
	public String getCurrInputName(){
		return mWifiName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.wifi_name_list, null);
            holder.wifiName = (TextView)convertView.findViewById(R.id.wifi_item_text);
            convertView.setTag(holder);
            
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        holder.wifiName.setText(mScanWifiNameList.get(position).getSSID());
        String capabilities = mScanWifiNameList.get(position).getCapabilities();
        if(mWifiMar.getCipherType(capabilities) != 1){
	        holder.wifiName.setCompoundDrawables(null, null, mWifiCipherTypeIcon, null);
        }else{
	        holder.wifiName.setCompoundDrawables(null, null, null, null);
        }
        
//        boolean isLastItem = (position == mWifiNameList.size() - 1);
//
//        if (mWifiName.equals(mWifiNameList.get(position))){
//            if (isLastItem){
//                convertView.setBackgroundResource(R.drawable.wifi_listshow_bottom_press_bg);
//            }
//            else {
//                convertView.setBackgroundResource(R.drawable.wifi_listshow_middle_press_bg);
//            }
//        }else{
//            if (isLastItem){
//                convertView.setBackgroundResource(R.drawable.wifi_listshow_bottom_bg);
//            }
//            else{
//                convertView.setBackgroundResource(R.drawable.wifi_listshow_middle_bg);
//            }
//        }
        
//        if(position != 0 && position % 2 == 0){
//        	System.out.println("niepeng position " + position);
//        	convertView.setBackgroundResource(R.drawable.wifi_listshow_bottom_bg);
//        }else{
//        	System.out.println("niepeng2 position " + position);
//        	 convertView.setBackgroundResource(R.drawable.wifi_listshow_middle_bg);
//        }
//        if(position == 0 && mWifiNameList.size() > 0){
//        	convertView.setBackgroundResource(R.drawable.wifi_listshow_middle_bg);
//        }
        
        return convertView;
	}
	
	static class ViewHolder {  
		public TextView wifiName;   
	}


}
