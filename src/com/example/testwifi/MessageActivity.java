package com.example.testwifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MessageActivity extends Activity {

	private ListView mListView = null;
	private ArrayList<Map<String, String>> mArrayList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		
		mListView = (ListView) findViewById(R.id.lv_alarm);
		
		mArrayList = new ArrayList<Map<String,String>>();
		int count = 10;
		for(int i=0; i<30; i++){
			Map<String, String> map = new HashMap<String, String>();
			if(i%5 == 0){
				count += 1;
//				map.put("tv_data", "2015/09/" + count);
				map.put("tv_data", "2015/09/" + count);
			}else{
//				map.put("tv_data", "2015/09/" + count);
				map.put("tv_data", "");
			}
//			map.put("tv_data", "2015/09/" + count);
			map.put("tv_time", "14:" + (i + 10));
			map.put("tv_alarm", "≤‚ ‘");
			
			mArrayList.add(map);
		}
		
		
		MessageAdapter messageAdapter = new MessageAdapter(getApplication(), mArrayList);
		
		mListView.setAdapter(messageAdapter);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
