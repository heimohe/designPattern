import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Map;

import com.snmpdis.*;

public class SimulationData {
	//�������豸IP���豸��Ϣ��map
		private static Map<String,NetworkDevice> subnetIpDevices = new LinkedHashMap<String,NetworkDevice>();
		//�������豸mac���豸��Ϣ��map
		private static Map<String,NetworkDevice> subnetMacDevices = new LinkedHashMap<>();
		//�����ڽ�����ip�뽻������Ϣ��map
		private static Map<String,NetworkDevice> subnetIpSwitchs = new LinkedHashMap<>();
		//�����ڽ�����mac�뽻������Ϣ��map
		private static Map<String,NetworkDevice> subnetMacSwitchs = new LinkedHashMap<>();
		//��Ҫ����չʾ���˽ṹ���Ľڵ���Ϣ
		private static TreeNode[] nodes;
		//ȫ�ֱ���TreeNode���������
		private static int nodeIndex = 1;
		
		//����IP
		private static String gatewayIp = "192.168.10.1";
		private static float gatewayId;
		
		//Map<IP����������IP��ַ��,Set<Mac>�����������˽�������������˿���Ҫת����mac��ַ��> ����ȫ�ִ洢���������ڽ�������Ӧ��mac��ַ
		private static Map<String,Set<String>> subnetSwitchsNextMacs;
		//�Ѿ����ɽڵ���Ϣ�ļ������type = 3���ڵ��set �����������ֹ�ظ����ɼ�����ڵ�
		private static Set<String> hasNodeComputerMac = new HashSet<>();
		
		public static void main(String[] args) {
			//--------------------------------------���ڲ����õ����ݵĹ��� start------------------------------------//
			subnetIpDevices.put("192.168.10.1", new NetworkDevice("00:23:cd:87:b6:18", "192.168.10.1", 1));
			
			subnetIpDevices.put("192.168.10.14", new NetworkDevice("ff:ff:ff:87:b6:14", "192.168.10.14", 3));
			subnetIpDevices.put("192.168.10.15", new NetworkDevice("ff:ff:ff:87:b6:15", "192.168.10.15", 3));
			subnetIpDevices.put("192.168.10.16", new NetworkDevice("ff:ff:ff:87:b6:16", "192.168.10.16", 3));
			subnetIpDevices.put("192.168.10.17", new NetworkDevice("ff:ff:ff:87:b6:17", "192.168.10.17", 3));

			subnetIpDevices.put("192.168.10.89", new NetworkDevice("f0:8a:28:00:03:89", "192.168.10.89", 2));
			subnetIpDevices.put("192.168.10.196", new NetworkDevice("f0:8a:28:00:03:96", "192.168.10.196", 2));
			subnetIpDevices.put("192.168.10.198", new NetworkDevice("f0:8a:28:00:03:98", "192.168.10.198", 2));
			

			
			//init subnetMacDevices
			for(Entry<String, NetworkDevice> entry : subnetIpDevices.entrySet()){
				subnetMacDevices.put(entry.getValue().getMacAddress(), entry.getValue());
			}
			//init subnetIpSwitchs
			for(Entry<String, NetworkDevice> entry : subnetIpDevices.entrySet()){
				if(entry.getValue().getDeviceType() == 2){
					subnetIpSwitchs.put(entry.getKey(), entry.getValue());
				}
			}
			
			//init subnetMacSwitchs
			for(Entry<String, NetworkDevice> entry : subnetMacDevices.entrySet()){
				if(entry.getValue().getDeviceType() == 2){
					subnetMacSwitchs.put(entry.getKey(), entry.getValue());
				}
			}
			
			//
			
//			check();
			//�������������豸����ʼ��TreeNode����
			nodes = new TreeNode[subnetIpDevices.size()];
			
			subnetSwitchsNextMacs = new HashMap<>();
			//get subnet switchs port mac set
			for(Entry<String, NetworkDevice> entry : subnetIpSwitchs.entrySet()){
				
				Set<String> set = getSwitchPortMac(entry.getKey());
				subnetSwitchsNextMacs.put(entry.getKey(), set);
				
			}
			
			nodes[0] = new TreeNode(gatewayId, 0, gatewayIp, true, "../../../css/zTreeStyle/img/diy/gateway1.png");
			
			//--------------------------------------���ڲ����õ����ݵĹ��� end------------------------------------//
			
//			checkSwitchMac(switchsNextMacs);
			
			//ѭ���������˽ṹ����ÿ����һ����֧���ͰѸ÷�֧ɾ������ֱ�������������з�֧ȫ���������
			while(subnetSwitchsNextMacs.size() > 0){
				generateNodeTree(gatewayIp,subnetSwitchsNextMacs);
			}
			
			//չʾ���ɵĽڵ㣬��������ݾͿ���������������Ҫ��json�����ˣ���������gson�Ĺ��߿���ֱ������
			for(TreeNode node : nodes){
				try{
					System.out.println("id:"+node.getId()+"  pId:"+node.getpId()+"  isOpen:"+node.isOpen());
				}catch (Exception e){
					break;
				}
			}
			
//			for(Entry<String, Set<String>> entry : subnetSwitchsNextMacs.entrySet()){
//				System.out.println(entry.getKey());
//			}
			
			
		}
		
		/**
		 * ������������˵ݹ�
		 * ������ͨ��snmp��ȡ�Ľ������¹��豸mac��ַ����ȡ���¹��豸��mac��ַ���ϰ������е�ֱ���豸�������������������豸����ֱ���豸�¹��豸��mac��ַ�ļ���
		 * ������Ļ��ٸ����ӽ�����1�¹ҽ�����2��3�����豸a��2�¹��豸b����1�¹��豸2��3��a��b��2�¹��豸b��3��û���¹��豸������豸bҲ�ǽ�������
		 * �¹���c��d����c��d������1��Ҳ����2
		 * ����Ҳ���Կ������ڵ���¹��豸�������ӽڵ���ӽڵ���¹��豸�����и��ڵ��¹��豸һ�����ӽڵ��¹��豸��
		 * @param parentIp ���ڵ��ip�����ڵ���������ػ򽻻�����
		 * @param switchsNextMacs ���ڵ��µ�����֧��snmp�Ľ��������������¹ҵ��豸��mac  ��ַMap<String��������IP��,Set<String>���������¹��豸��mac��ַ���ϣ�>
		 * 
		 * ����������޶�����������Ҫ�ó�һ�����ۣ���ӵ������¹��豸�Ľ�����һ����parentIp��ֱ���ӽڵ㣬
		 * ������Է�֤һ�����������ֱ���ӽڵ�Ļ���ô����parentIpֱ��һ�����������������������ĸ��ڵ㣬���Ǹ���������޶����ӽڵ���¹��豸�����ܴ��ڸ��ڵ���¹��豸
		 * 
		 * ������Ĭ������Ϊ���ڵ㣬�ҵ��¹��豸���Ľ���������������������Ϳ���ȷ���Ǹ�Ĭ������ֱ�����ˣ�Ȼ���ٿ�����������µ������¹��豸�Ƿ���������������������
		 * �������������ΪparentIp���������¹ҵĽ�������ΪswitchsNextMacs���ٴε�������ֱ��û���¹ҽ�����Ϊֹ
		 */
		private static void generateNodeTree(String parentIp,Map<String,Set<String>> switchsNextMacs){
			//�ҳ����н��������¹��豸����һ��������
			String maxSwitchIp = getNextMaxSwitchIp(switchsNextMacs);
			
			Set<String> parentSet = null;
			if(subnetSwitchsNextMacs.containsKey(parentIp)){
				//��ȡ��ǰ����Ľ������ĸ��豸�������¹��豸
				parentSet = subnetSwitchsNextMacs.get(parentIp);
				//Ĭ�����ز�֧��snmp���޷���ȡ�����¹��豸
				if(null != parentSet){
					//����ǰ����Ľ������Ӹ��豸���¹��豸���޳���ֱ���޳��ɾ������㴦�����˸��豸
					//@@���A
					parentSet.remove(subnetIpDevices.get(maxSwitchIp).getMacAddress());
				}
			}
			//����������ۣ�����¹��豸���Ľ�������parentIpֱ���ģ����ɽڵ�����
			nodes[nodeIndex] = new TreeNode(getDeviceIp(maxSwitchIp), getDeviceIp(parentIp), maxSwitchIp, true, "../../../css/zTreeStyle/img/diy/switch1.png");
			nodeIndex++;
			
			//��ȡ��ǰ�����������¹��豸�Ľ������������¹��豸
			Set<String> nextMacs = subnetSwitchsNextMacs.get(maxSwitchIp);
			
			//<ip,Set<mac>> ���ڴ洢��ǰ�����������¹��豸�Ľ��������¹ҽ�����
			Map<String,Set<String>> nextMap = new HashMap<String,Set<String>>();
			
			for(String mac : nextMacs){
				if(subnetMacDevices.containsKey(mac) && subnetMacDevices.get(mac).getDeviceType() == 2){
					String switchIp = subnetMacDevices.get(mac).getIpAddress();
					nextMap.put(switchIp,switchsNextMacs.get(switchIp));
					//���ǵ�ǰ�����������¹��豸�Ľ������������¹ҽ����������ĸ����������¹��豸�������
					//�������ص�����������ʱ�Ͳ����ٴ�����Щ�϶����Ǹ���ֱ���Ľ�������
					if(null != parentSet){
						parentSet.remove(mac);
					}
				}
			}

			while(nextMap.size() > 0){
//				System.out.println("maxSwitchIp:"+maxSwitchIp+"  nextMap size:"+nextMap.size());
				//���¹ҽ���������������0ʱ�ݹ�����Լ�
				generateNodeTree(maxSwitchIp,nextMap);
				
				//������ɺ����»�ȡ��ǰ�����������¹ҽ����������������0����
				//���������޳�����@@���A�����õ�
				nextMap.clear();

				for(String mac : nextMacs){
					if(subnetMacDevices.containsKey(mac) && subnetMacDevices.get(mac).getDeviceType() == 2){
						String switchIp = subnetMacDevices.get(mac).getIpAddress();
						nextMap.put(switchIp,switchsNextMacs.get(switchIp));
					}
				}
			}

			//���뵽�������ǰ����Ľ������Ѿ�û���¹ҽ������ˣ����԰��������¹��豸���ɽڵ������ˣ�
			//Ҫע����ǣ���Ϊ���ڵ�����ӽڵ���ӽڵ�������ӽڵ㣬������Щ�ӽڵ���¹��豸�Ѿ����ɹ��ˣ��洢��hasNodeComputerMac�����
			//���ӽڵ���¹��豸�޳������ǵ�ǰ�������Լ����¹��豸��
			for(String mac : nextMacs){
				if(subnetMacDevices.containsKey(mac) && !hasNodeComputerMac.contains(mac)){
					String computerIp = subnetMacDevices.get(mac).getIpAddress();;
					nodes[nodeIndex] = new TreeNode(getDeviceIp(computerIp), getDeviceIp(maxSwitchIp), computerIp, "../../../css/zTreeStyle/img/diy/computer1.png");
					hasNodeComputerMac.add(mac);
					nodeIndex++;
				}
			}
			
			//�����Լ�����Ҫ����Ľ������������޳���������
			subnetSwitchsNextMacs.remove(maxSwitchIp);
		}
		
		
		//�ҳ����н��������¹��豸����һ��������
		private static String getNextMaxSwitchIp(Map<String,Set<String>> switchsNextMacs){
			int maxCount = -1;
			String maxSwitchIp = null;
			for(Entry<String, Set<String>> entry : switchsNextMacs.entrySet()){
				if(entry.getValue().size() > maxCount){
					maxCount = entry.getValue().size();
					maxSwitchIp = entry.getKey();
				}
			}
			return maxSwitchIp;
		}
		
		private static Set<String> getSwitchPortMac(String targetIp){
			
			Set<String> result = new HashSet<String>();
			switch (targetIp) {
			case "192.168.10.89":
				result.add("ff:ff:ff:87:b6:14");
				result.add("ff:ff:ff:87:b6:15");
				break;
			case "192.168.10.196":
				result.add("ff:ff:ff:87:b6:16");
				result.add("ff:ff:ff:87:b6:17");
				result.add("f0:8a:28:00:03:98");
				break;
			case "192.168.10.198":
				result.add("ff:ff:ff:87:b6:16");
				break;
			default:
				break;
			}
			
			return result;
			
		}
		
		public static float getDeviceIp(String deviceIp){
			String[] strs = deviceIp.split("\\.");
			float f = Float.parseFloat(strs[3]);
			return f;
		}
}