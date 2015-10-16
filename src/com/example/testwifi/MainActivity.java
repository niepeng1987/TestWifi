package com.example.testwifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.testwifi.WifiMar.WifiConnect;

import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity implements OnClickListener {

	private Button mStartConfigure = null;
	private TextView mHidePasswd = null;
	private EditText mWifiPasswd = null;
	private TextView mCancelConfigure = null;
	private EditText mWifiSSID = null;
	private TextView mWifiStatusText = null;

	private boolean miSHidePasswd = true;

	private boolean mIsOpenScanWifi = false;
	private ListView mWifiNamelistView;
    private RelativeLayout mNameListLayout;

    private ArrayList<ScanWifiInfo> mScanWifiInfos = null;
    private WifiNameAdapter wifiNameAdapter = null;


    WifiMar mWifiMar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mStartConfigure = (Button)findViewById(R.id.fragment_config_net_next_btn);
		mHidePasswd = (TextView) findViewById(R.id.fragment_config_net_hide_password);
		mWifiPasswd = (EditText) findViewById(R.id.fragment_config_net_wifipassword);
		mCancelConfigure = (TextView) findViewById(R.id.fragment_config_net_cancle);
		mWifiSSID = (EditText) findViewById(R.id.fragment_config_net_wifiname);
		mWifiStatusText = (TextView)findViewById(R.id.fragment_config_net_wifi_status);

		mWifiNamelistView = (ListView) findViewById(R.id.wifi_name_list);
		mNameListLayout = (RelativeLayout) findViewById(R.id.wifi_name_list_rl);


		if (mStartConfigure != null) {
			mStartConfigure.setOnClickListener(this);
		}
		if (mHidePasswd != null) {
			mHidePasswd.setOnClickListener(this);
			mWifiPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			mHidePasswd.setText(R.string.config_network_wifi_display_password);
			miSHidePasswd = false;
		}
		if (mCancelConfigure != null) {
			mCancelConfigure.setOnClickListener(this);
		}
		if (mWifiSSID != null) {
			mWifiSSID.setOnClickListener(this);
			mWifiSSID.setFocusable(false);
			mWifiSSID.setText("");
		}
		mWifiPasswd.setText("");
		mWifiMar = new WifiMar(this);


	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// scan wifi
			case R.id.fragment_config_net_next_btn:
				String wifiName = mWifiSSID.getText().toString().trim();
				String wifiPassword = mWifiPasswd.getText().toString().trim();

				mWifiMar.connectWifi(wifiName, wifiPassword, wifiConnect);
				break;
			// scan wifi
			case R.id.fragment_config_net_wifiname:
				mWifiMar.openWifi();
				mWifiMar.startScanWifi(wifiConnect);
				mWifiSSID.setFocusable(false);
				if(mIsOpenScanWifi){
					mIsOpenScanWifi = false;
					hideWifiNameList(true);
				}else{
					mIsOpenScanWifi = true;
					showWifiNameList();
				}

				break;
			case R.id.fragment_config_net_hide_password:
				int index = mWifiPasswd.getSelectionStart();
				if (miSHidePasswd) {
					mWifiPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
					mHidePasswd.setText(R.string.config_network_wifi_display_password);
					miSHidePasswd = false;
				} else {
					mWifiPasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					mHidePasswd.setText(R.string.config_network_wifi_hide_password);
					miSHidePasswd = true;
				}
				Selection.setSelection(mWifiPasswd.getText(), index);
				break;
			case R.id.fragment_config_net_cancle:
//				finish();
				WifiInfo wifiInto = mWifiMar.getWifiInfo();
				mWifiMar.disconnectWifi(mWifiMar.getCurrentNetId());

				mWifiMar.resumeWifi(wifiInto, wifiConnect);
				break;
			default:
				break;
		}
	}

	public WifiConnect wifiConnect = new WifiConnect() {

		@Override
		public void wifiConnecting(int type) {
			// TODO Auto-generated method stub
			if(type == 0){
				//Toast.makeText(getBaseContext(), "wifi Connecting...", Toast.LENGTH_SHORT).show();
				mWifiStatusText.setText("WIFI 正在获取IP地址...");
			}else if(type == 1){
				//Toast.makeText(getBaseContext(), "wifi password error", Toast.LENGTH_SHORT).show();
				mWifiStatusText.setText("WIFI 密码有误");
			}
		}

		@Override
		public void wifiConnected() {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), "WIFI 连接成功", Toast.LENGTH_LONG).show();
			mWifiStatusText.setText("WIFI 连接成功");
		}

		@Override
		public void wifiScanWifiList(ArrayList<ScanWifiInfo> scanWifiInfos) {
			// TODO Auto-generated method stub
			mScanWifiInfos = scanWifiInfos;
			if(wifiNameAdapter == null){
				wifiNameAdapter = new WifiNameAdapter(getBaseContext(), mScanWifiInfos);
			}
			if(mWifiNamelistView != null){
				mWifiNamelistView.setAdapter(wifiNameAdapter);
				mWifiNamelistView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						if(mWifiSSID != null){
							mWifiSSID.setText(mScanWifiInfos.get(position).getSSID());
						}

						hideWifiNameList(true);
					}
				});
			}
			wifiNameAdapter.notifyDataSetChanged();

		}
	};

    private void hideWifiNameList(boolean showAnim) {
        if (!mNameListLayout.isShown()) {
            return;
        }

        if (!showAnim) {
        	mNameListLayout.setVisibility(View.GONE);
            mWifiSSID.setBackgroundResource(R.drawable.startconfig_image2);
            return;
        }

        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.account_list_disappear);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	mNameListLayout.setVisibility(View.GONE);
                mWifiSSID.setBackgroundResource(R.drawable.startconfig_image2);
            }
        });
        mNameListLayout.startAnimation(animation);
    }

	   private void showWifiNameList() {
	        if (mNameListLayout.isShown()) {
	            return;
	        }

	        mNameListLayout.startAnimation(AnimationUtils.loadAnimation(
	                this, R.anim.account_list_appear));
	        mWifiSSID.setBackgroundResource(R.drawable.wifi_listshow_top_bg);
	        mNameListLayout.setVisibility(View.VISIBLE);
//	        mNameListLayout.bringToFront();
	    }


}
