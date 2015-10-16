package com.example.testwifi;

public class ScanWifiInfo {
	private String SSID = "";
	private String BSSID = "";
	private String capabilities = "";
	private int level = 0;
	private int frequency = -1;
	public boolean isChecked=false;
	public ScanWifiInfo() {
		super();
	}

	public String getSSID() {
		return SSID;
	}

	public void setSSID(String sSID) {
		SSID = sSID;
	}

	public String getBSSID() {
		return BSSID;
	}

	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}

	public String getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
