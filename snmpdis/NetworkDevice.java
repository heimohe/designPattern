package com.snmpdis;

public class NetworkDevice {

	private String macAddress;
	private String ipAddress;
	//1:default gateway  2:switch  3:computer
	private int deviceType;
	
	public NetworkDevice(){
		
	}
	
	public NetworkDevice(String macAddress, String ipAddress) {
		super();
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
	}
	
	public NetworkDevice(String macAddress, String ipAddress, int type){
		super();
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.deviceType = type;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	
	
}