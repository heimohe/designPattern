package com.snmpdis;

import java.net.Inet4Address;

import jpcap.*;
public class Snmp {

	public static void main(String[] args) {
		// ��ȡ�����ϵ�����ӿڶ�������    
        final NetworkInterface[] devices = JpcapCaptor.getDeviceList();    
        //��ȡ������һ����ipv4��ַ�Ҳ��ǻػ���ַ������ӿ�  
        for (NetworkInterface nc : devices) {     
            // һ�鿨�Ͽ����ж����ַ: 
        	
            if(nc.addresses.length > 0 && !nc.loopback){  
                for (int t = 0; t < nc.addresses.length; t++) { 
                    if(nc.addresses[t].address instanceof Inet4Address){  
                        NetworkInterface currentDevice = nc;  //���������Ҫ��ȡ������ӿڶ������������ͨ������ȡip������������nc.addresses[t].address.getHostAddress()  nc.addresses[t].subnet.getAddress()  
//                        System.out.println(nc.addresses[t]); 
                        System.out.println(nc.addresses[t].address.getHostAddress() +"<===>"+nc.addresses[t].subnet  );
                    }  
                }    
            }  
        } 
		
	}

}
