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
		//��ȡ����ӿ��б�
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
		 * ��ָ��������ӿڣ�������ʵ��
		 * 2000:һ��ץȡ�����bytes
		 * false: If true, the inferface becomes promiscuous mode
		 * 3000:��ʱʱ�䣬���������е�ƽ̨��֧����������������֧��������������ԣ����֧�ֵĻ�ֻҪ����ʱ��Jpcap�ͻ�һֱ�ȴ��㹻�����ݵ���
		 */
		//JpcapCaptor:������ץ������ȡ�����ݣ�ͨ��libpcap��ץ���ļ�
		JpcapCaptor captor=JpcapCaptor.openDevice(device,2000,false,100);
		//"arp":condition - a string representation of the filter   true:optimize - If true, the filter is optimized
		captor.setFilter("arp",true);
		//��������Ӧ���Ƿ��Ͱ��Ķ���
		JpcapSender sender=captor.getJpcapSenderInstance();
		
		InetAddress srcip=null;
		//һ������ӿڿ����ж����ַ������ѡȡһ��ipv4��ַ���з���
		for(NetworkInterfaceAddress addr:device.addresses)
			if(addr.address instanceof Inet4Address){
				srcip=addr.address;
				break;
			}
		//�㲥��ַ
		byte[] broadcast=new byte[]{(byte)255,(byte)255,(byte)255,(byte)255,(byte)255,(byte)255};
		//����jpcap.packet�����һ���࣬���滹��������һЩ�࣬������Ҳ���˽⵽jpcap֧����ЩЭ��ķ��ͣ����������������arp��rarp����
		ARPPacket arp=new ARPPacket();
		//Ӳ������ �������Ͽ�����̫������
		arp.hardtype=ARPPacket.HARDTYPE_ETHER;
		//Э�����ͣ�ip
		arp.prototype=ARPPacket.PROTOTYPE_IP;
		//arp����
		arp.operation=ARPPacket.ARP_REQUEST;
		//mac��ַ����
		arp.hlen=6;
		//ip����
		arp.plen=4;
		
		//Դ��Ŀ�ĵ�mac��ַ��ip��ַ
		arp.sender_hardaddr=device.mac_address;
		arp.sender_protoaddr=srcip.getAddress();
		arp.target_hardaddr=broadcast;
		arp.target_protoaddr=string.getAddress();
		
		//��̫�����ݰ������Կ�������ֻ��ҪԴ��Ŀ��mac��ַ���Ǵ���������·��ģ�����Ҳָ����arp��������·��
		EthernetPacket ether=new EthernetPacket();
		ether.frametype=EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac=device.mac_address;
		ether.dst_mac=broadcast;
		arp.datalink=ether;
		
		//������Ƿ���arp�ͽ�����
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