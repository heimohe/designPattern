package com.snmpdis;


import java.net.Inet4Address;
import java.net.InetAddress;
import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class ARP {
	public static byte[] arp(InetAddress string) throws java.io.IOException{
		//find network interface
		//获取网络接口列表
		NetworkInterface[] devices=JpcapCaptor.getDeviceList();
		NetworkInterface device=null;

loop:	for(NetworkInterface d:devices){
			for(NetworkInterfaceAddress addr:d.addresses){
				if(!(addr.address instanceof Inet4Address)) continue;
				byte[] bip=string.getAddress();
				byte[] subnet=addr.subnet.getAddress();
				byte[] bif=addr.address.getAddress();
				for(int i=0;i<4;i++){
					bip[i]=(byte)(bip[i]&subnet[i]);
					bif[i]=(byte)(bif[i]&subnet[i]);
				}
				
				if(bip.equals(bif)){
					device=d;
					break loop;
				}
			}
		}
		
//		device.addresses.
//		System.out.println("");
		
		if(device==null)
			throw new IllegalArgumentException(string+" is not a local address");
		
		/**
		 * 打开指定的网络接口，并返回实例
		 * 2000:一次抓取的最大bytes
		 * false: If true, the inferface becomes promiscuous mode
		 * 3000:超时时间，并不是所有的平台都支持这个参数，如果不支持这个参数被忽略，如果支持的话只要不超时，Jpcap就会一直等待足够的数据到达
		 */
		//JpcapCaptor:是用来抓包、读取包数据，通过libpcap的抓包文件
		JpcapCaptor captor=JpcapCaptor.openDevice(device,2000,false,100);
		//"arp":condition - a string representation of the filter   true:optimize - If true, the filter is optimized
		captor.setFilter("arp",true);
		//根据名字应该是发送包的对象
		JpcapSender sender=captor.getJpcapSenderInstance();
		
		InetAddress srcip=null;
		//一个网络接口可能有多个地址，我们选取一个ipv4地址进行发送
		for(NetworkInterfaceAddress addr:device.addresses)
			if(addr.address instanceof Inet4Address){
				srcip=addr.address;
				break;
			}
		//广播地址
		byte[] broadcast=new byte[]{(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255};
		//是在jpcap.packet里面的一个类，里面还有其他的一些类，从这里也能了解到jpcap支持哪些协议的发送，这里的是用来处理arp和rarp的类
		ARPPacket arp=new ARPPacket();
		//硬件类型 从字面上看是以太网类型
		arp.hardtype=ARPPacket.HARDTYPE_ETHER;
		//协议类型：ip
		arp.prototype=ARPPacket.PROTOTYPE_IP;
		//arp请求
		arp.operation=ARPPacket.ARP_REQUEST;
		//mac地址长度
		arp.hlen=6;
		//ip长度
		arp.plen=4;
		
		//源和目的的mac地址、ip地址
		arp.sender_hardaddr=device.mac_address;
		arp.sender_protoaddr=srcip.getAddress();
		arp.target_hardaddr=broadcast;
		arp.target_protoaddr=string.getAddress();
		
		//以太网数据包，可以看到这里只需要源和目的mac地址，是处在数据链路层的，后面也指定了arp的数据链路层
		EthernetPacket ether=new EthernetPacket();
		ether.frametype=EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac=device.mac_address;
		ether.dst_mac=broadcast;
		arp.datalink=ether;
		
		//下面就是发送arp和接收了
		sender.sendPacket(arp);
//		System.out.println(System.currentTimeMillis());
		while(true){
			ARPPacket p=(ARPPacket)captor.getPacket();
//			System.out.println(System.currentTimeMillis());
			if(p==null){
				throw new IllegalArgumentException(string+" is not a local address");
			}
			if(p.target_protoaddr.equals(srcip.getAddress())){
				return p.sender_hardaddr;
			}
		}
	}
		
	public static String getMacAddress(byte[] mac){
	    if (mac == null)
	        return null;

	    StringBuilder sb = new StringBuilder(18);
	    for (byte b : mac) {
			if (sb.length() > 0)
	        	sb.append(':');
	        sb.append(String.format("%02x", b));
	    }
	    return sb.toString();
	}
	
	public static void main(String[] args) throws Exception{
		
		//byte[] mac=ARP.arp(InetAddress.getByName("10.164.16.108"));
		System.out.println(InetAddress.getByName("110.164.16.108"));
//		String strMac = getMacAddress(mac);
//		System.out.println(strMac);
//		System.exit(0);
		
	}
}