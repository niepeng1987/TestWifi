package com.example.testwifi;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater = null;
	
	private ArrayList<Map<String, String>> mList = null;

	public MessageAdapter(Context context, ArrayList<Map<String, String>> list) {
		super();
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.message_list_item, null);
			holder = new ViewHolder();
			
			holder.tv_data = (TextView) convertView.findViewById(R.id.tv_date);
			holder.iv_dot = (ImageView) convertView.findViewById(R.id.iv_dot);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_alarm = (TextView) convertView.findViewById(R.id.tv_alarm);
			
//			holder.real_video = (Button) convertView.findViewById(R.id.real_video);
//			holder.search_video = (Button) convertView.findViewById(R.id.search_video);
//			holder.search_snap = (Button) convertView.findViewById(R.id.search_snap);
            
            convertView.setTag(holder);
            
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
		
		System.out.println("niepeng log ==> " + position + " " + mList.get(position).get("tv_data"));
		
		Map<String, String> temp = mList.get(position);
		if(temp != null){
			if(temp.containsKey("tv_data")){
				String value = temp.get("tv_data");
				System.out.println("niepeng log ==> " + value.length());
				if(value.length() > 0){
					holder.tv_data.setVisibility(View.VISIBLE);
					holder.tv_data.setText(value);
				}else{
					holder.tv_data.setVisibility(View.GONE);
				}
			}
			
			if(temp.containsKey("tv_time")){
				String value = temp.get("tv_time");
				holder.tv_time.setText(value);
			}
			
			if(temp.containsKey("tv_alarm")){
				String value = temp.get("tv_alarm");
				holder.tv_alarm.setText(value);
			}
		}
		
		return convertView;
	}
	
	static class ViewHolder {  
		public TextView tv_data;
		public ImageView iv_dot;
		public TextView tv_time;
		public TextView tv_alarm;
//		public Button real_video;
//		public Button search_video;
//		public Button search_snap;
	}

}
