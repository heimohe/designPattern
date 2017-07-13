package com.snmpdis;

import java.net.Inet4Address;

import jpcap.*;
public class Snmp {

	public static void main(String[] args) {
		// 获取本机上的网络接口对象数组    
        final NetworkInterface[] devices = JpcapCaptor.getDeviceList();    
        //获取本机第一个有ipv4网址且不是回环地址的网络接口  
        for (NetworkInterface nc : devices) {     
            // 一块卡上可能有多个地址: 
        	
            if(nc.addresses.length > 0 && !nc.loopback){  
                for (int t = 0; t < nc.addresses.length; t++) { 
                    if(nc.addresses[t].address instanceof Inet4Address){  
                        NetworkInterface currentDevice = nc;  //这个就是需要获取的网络接口对象，在这里可以通过它获取ip和子网掩码如nc.addresses[t].address.getHostAddress()  nc.addresses[t].subnet.getAddress()  
//                        System.out.println(nc.addresses[t]); 
                        System.out.println(nc.addresses[t].address.getHostAddress() +"<===>"+nc.addresses[t].subnet  );
                    }  
                }    
            }  
        } 
		
	}

}
