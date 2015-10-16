package com.example.testwifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class WifiMar {
	public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
	public static final String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
	private static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";

	private WifiManager mWifiManager = null;
	private ConnectivityManager mConnectivityManager = null;
	private WifiInfo mWifiInfo = null;
	private WifiReceiver mWifiReceiver = null;
	private Context mContext;
	private List<WifiConfiguration> mWifiConfigList;

	private List<ScanResult> mScanResultList = null;

	private WifiConnect mWifiConectListener = null;
	public int wifiConnectErrorCode = -1;

	private int mAddNetworkId = -1;

	private boolean mConnectWifi = false;
	private boolean mScanWifi = false;

	private ArrayList<ScanWifiInfo> mScanWifiInfoList = new ArrayList<ScanWifiInfo>();

	public WifiMar(Context context){
		this.mContext = context;
		this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		this.mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.mScanResultList = new ArrayList<ScanResult>();
//		startScanWifi(mWifiConectListener);
		regesterWifiReceiver();
	}

	public void regesterWifiReceiver(){
		if (mWifiReceiver == null) {
			mWifiReceiver = new WifiReceiver();
		}
		mContext.registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
		mContext.registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mContext.registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
	}

	public void uregesterWifiReceiver(){
		if(mWifiReceiver != null){
			mContext.unregisterReceiver(mWifiReceiver);
			mWifiReceiver = null;
		}
	}

	//打开WIFI
	public boolean openWifi() {
		if (mWifiManager == null)
			return false;
		if (!mWifiManager.isWifiEnabled()) {
			return mWifiManager.setWifiEnabled(true);
		} else {
			return false;
		}
	}

	//关闭WIFI
	public boolean closeWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			return true;
		} else {
			return mWifiManager.setWifiEnabled(false);
		}
	}

	//获取WIFI信号强度
	public int getLevel(int NetId) {
		return mScanResultList.get(NetId).level;
	}

	//获取本机Mac地址
	public String getMac() {
		if (mWifiManager == null)
			return null;
		this.mWifiInfo = mWifiManager.getConnectionInfo();
		return (mWifiInfo == null) ? "" : mWifiInfo.getMacAddress();
	}

	//获取连接WIFI的BSSID
	public String getBSSID() {
		if (mWifiManager == null)
			return null;
		this.mWifiInfo = mWifiManager.getConnectionInfo();
		return (mWifiInfo == null) ? null : mWifiInfo.getBSSID();
	}

	//获取连接WIFI的SSID
	public String getSSID() {
		if (mWifiManager == null)
			return null;
		this.mWifiInfo = mWifiManager.getConnectionInfo();
		if (this.mWifiInfo == null)
			return null;
		String ssid = mWifiInfo.getSSID();
		if (ssid != null) {
			return ssid.replaceAll("\"", "");
		}
		return null;
	}

	//获取当前连接WIFI的networiID
	public int getCurrentNetId() {
		if (mWifiManager == null)
			return -1;
		this.mWifiInfo = mWifiManager.getConnectionInfo();
		return (mWifiInfo == null) ? null : mWifiInfo.getNetworkId();
	}

	//获取当前连接WIFI信息
	public String getwifiInfo() {
		// 得到Wifi信息
		this.mWifiInfo = mWifiManager.getConnectionInfo();
		return (mWifiInfo == null) ? null : mWifiInfo.toString();
	}

	public WifiInfo getWifiInfo(){
		return mWifiManager.getConnectionInfo();
	}

	// 获取IP地址
	public int getIP() {
		if (mWifiManager == null)
			return -1;
		this.mWifiInfo = mWifiManager.getConnectionInfo();
		return (mWifiInfo == null) ? null : mWifiInfo.getIpAddress();
	}

	//添加WIFI连接
	public boolean addNetWordLink(WifiConfiguration config) {
		if (mWifiManager == null)
			return false;
		int NetId = mWifiManager.addNetwork(config);
		return mWifiManager.enableNetwork(NetId, true);
	}

	//禁用WIFI连接
	public boolean disableNetWordLick(int NetId) {
		if (mWifiManager == null)
			return false;
		mWifiManager.disableNetwork(NetId);
		return mWifiManager.disconnect();
	}

	//移除一个WIFI连接
	public boolean removeNetworkLink(int NetId) {
		if (mWifiManager == null)
			return false;
		return mWifiManager.removeNetwork(NetId);
	}

	//不显示SSID
	public void hiddenSSID(int NetId) {
		mWifiConfigList.get(NetId).hiddenSSID = true;
	}

	//显示SSID
	public void displaySSID(int NetId) {
		mWifiConfigList.get(NetId).hiddenSSID = false;
	}

	//连接到指定SSID 的WIFI连接
	public boolean connectWifi(String ssid, WifiConnect wifiConectListener) {
		mConnectWifi = true;
		mWifiConectListener = wifiConectListener;
		int passwordType = getCipherType(getCapabilities(this.mContext, ssid));
		WifiConfiguration wifiConfig = this.CreateWifiInfo(ssid, "", passwordType);
		int wcgID = mWifiManager.addNetwork(wifiConfig);
		boolean flag = mWifiManager.enableNetwork(wcgID, true);
		return flag;
	}

	public boolean connectWifi(String ssid, String password, WifiConnect wifiConectListener){
		mConnectWifi = true;
		mWifiConectListener = wifiConectListener;
		int passwordType = getCipherType(getCapabilities(this.mContext, ssid));
		WifiConfiguration wifiConfig = this.CreateWifiInfo(ssid, password, passwordType);
		mAddNetworkId = mWifiManager.addNetwork(wifiConfig);
		boolean flag = mWifiManager.enableNetwork(mAddNetworkId, true);


		return flag;
	}

	public boolean connectWifi(WifiConfiguration wifiConfiguration, WifiConnect wifiConectListener){
		mConnectWifi = true;
		mWifiConectListener = wifiConectListener;
		mAddNetworkId = mWifiManager.addNetwork(wifiConfiguration);
		boolean flag = mWifiManager.enableNetwork(mAddNetworkId, true);

		return flag;
	}

	private WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		if (Type == 1) {// WIFICIPHER_NOPASS
			//config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			//config.wepTxKeyIndex = 0;
		}
		if (Type == 2) {// WIFICIPHER_WEP
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) {// WIFICIPHER_WPA
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	public WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		if(existingConfigs == null){
			return null;
		}
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	public int getCipherType(String capabilities) {
		int wifiPasswdTyep = 0;
		if (!TextUtils.isEmpty(capabilities)) {
			if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
				wifiPasswdTyep = 3;
			} else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
				wifiPasswdTyep = 2;
			} else {
				wifiPasswdTyep = 1;
			}
		}
		return wifiPasswdTyep;
	}

	 private String getCapabilities(Context context, String ssid) {
	        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	        List<ScanResult> list = wifiManager.getScanResults();
	        for (ScanResult scResult : list) {
	            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {
	                String capabilities = scResult.capabilities;
	                if (!TextUtils.isEmpty(capabilities)) {
	                    return capabilities;
	                }
	            }
	        }
	        return "";
	    }

		public boolean isWifiConnected(){
			boolean mIsWifiConnected = false;
			State wifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			if (wifi == State.CONNECTED/* || wifi == State.CONNECTING*/) {
				mIsWifiConnected = true;
			} else if (wifi == State.DISCONNECTED/* || wifi == State.DISCONNECTING*/) {
				mIsWifiConnected = false;
			}
			return mIsWifiConnected;
		}

		public void startScanWifi(WifiConnect wifiConectListener) {
			mScanWifi = true;
			mWifiConectListener = wifiConectListener;
			mWifiManager.startScan();
			mScanResultList = mWifiManager.getScanResults();
			mWifiConfigList = mWifiManager.getConfiguredNetworks();

		}

	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			if(intent.getAction().equals(NETWORK_STATE_CHANGED_ACTION)){
				System.out.println("niepeng log => " + isWifiConnected() + " " + mWifiConectListener == null);
				if(isWifiConnected() && mWifiConectListener != null && mConnectWifi){
					mWifiConectListener.wifiConnected();
					mWifiConectListener = null;
					mConnectWifi = false;
				}
			}else if (intent.getAction().equals(SCAN_RESULTS_AVAILABLE_ACTION)) {
				if(mWifiConectListener != null && mScanWifi){
					getScanWifiInfo();
					mWifiConectListener = null;
					mScanWifi = false;
				}

			}else if (intent.getAction().equals(SUPPLICANT_STATE_CHANGED_ACTION)) {
				if(mWifiConectListener != null && mConnectWifi){
					if(!isWifiConnected()){
						wifiConnectErrorCode  = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
					}
					if(wifiConnectErrorCode == 1){
						mWifiConectListener.wifiConnecting(1);
						mWifiConectListener = null;
						mConnectWifi = false;
						if(mAddNetworkId != -1){
							disableNetWordLick(mAddNetworkId);
						}
					}else{
						mWifiConectListener.wifiConnecting(0);
					}
				}
			}
		}
	}

	public interface WifiConnect {
		public void wifiConnected();
		public void wifiConnecting(int type);
		public void wifiScanWifiList(ArrayList<ScanWifiInfo> scanWifiInfos);
	}

	public void getScanWifiInfo(){
		//add by 0902
//		ArrayList<ScanWifiInfo> temp = mScanWifiInfoList;
		if(mScanResultList == null){
			return;
		}
		mScanWifiInfoList.clear();

		for (int i = 0; i < mScanResultList.size(); i++) {
			ScanWifiInfo scanWifiInfo = new ScanWifiInfo();
			String wifiName = mScanResultList.get(i).SSID.toString().trim();
			scanWifiInfo.setBSSID(mScanResultList.get(i).BSSID);
			scanWifiInfo.setSSID(wifiName);
			scanWifiInfo.setCapabilities(mScanResultList.get(i).capabilities);
			scanWifiInfo.setLevel(mScanResultList.get(i).level);
			scanWifiInfo.setFrequency(mScanResultList.get(i).frequency);
			if(!TextUtils.isEmpty(wifiName)){
				mScanWifiInfoList.add(scanWifiInfo);
			}
		}

		mScanResultList.clear();


		Collections.sort(mScanWifiInfoList, new SortByWifiLevel());

		mWifiConectListener.wifiScanWifiList(mScanWifiInfoList);

	}

	class SortByWifiLevel implements Comparator<ScanWifiInfo>  {

		@Override
		public int compare(ScanWifiInfo lhs, ScanWifiInfo rhs) {
			ScanWifiInfo scanWifiInfo1 = (ScanWifiInfo) lhs;
			ScanWifiInfo scanWifiInfo2 = (ScanWifiInfo) rhs;
			if (scanWifiInfo1.getLevel() > scanWifiInfo2.getLevel()){
				return 1;
			}
			return 0;
		}
	}

	public void disconnectWifi(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				disconnectWifi(existingConfig.networkId);
			}
		}
	}

	public void disconnectWifi(int networkid){
		mWifiManager.disableNetwork(networkid);
		mWifiManager.disconnect();
	}

	public void resumeWifi(WifiInfo wifiInfo, WifiConnect wifiConnectListener){
		if(wifiInfo != null){
			WifiConfiguration tempConfig = this.IsExsits(wifiInfo.getSSID().replace("\"", ""));
			if(tempConfig != null){
				connectWifi(tempConfig, wifiConnectListener);
			}
		}
	}

	public void resumeWifi(String ssid, String password, WifiConnect wifiConnectListener){
		connectWifi(ssid, password, wifiConnectListener);
	}
}
