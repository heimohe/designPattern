import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Map;

import com.snmpdis.*;

public class SimulationData {
	//子网内设备IP与设备信息的map
		private static Map<String,NetworkDevice> subnetIpDevices = new LinkedHashMap<String,NetworkDevice>();
		//子网内设备mac与设备信息的map
		private static Map<String,NetworkDevice> subnetMacDevices = new LinkedHashMap<>();
		//子网内交换机ip与交换机信息的map
		private static Map<String,NetworkDevice> subnetIpSwitchs = new LinkedHashMap<>();
		//子网内交换机mac与交换机信息的map
		private static Map<String,NetworkDevice> subnetMacSwitchs = new LinkedHashMap<>();
		//需要用来展示拓扑结构树的节点信息
		private static TreeNode[] nodes;
		//全局变量TreeNode数组的索引
		private static int nodeIndex = 1;
		
		//网关IP
		private static String gatewayIp = "192.168.10.1";
		private static float gatewayId;
		
		//Map<IP（交换机的IP地址）,Set<Mac>（交换机除了接入网络的其他端口需要转发的mac地址）> 用于全局存储所有子网内交换机对应的mac地址
		private static Map<String,Set<String>> subnetSwitchsNextMacs;
		//已经生成节点信息的计算机（type = 3）节点的set 利用这个来防止重复生成计算机节点
		private static Set<String> hasNodeComputerMac = new HashSet<>();
		
		public static void main(String[] args) {
			//--------------------------------------用于测试用的数据的构造 start------------------------------------//
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
			//根据子网在线设备数初始化TreeNode数组
			nodes = new TreeNode[subnetIpDevices.size()];
			
			subnetSwitchsNextMacs = new HashMap<>();
			//get subnet switchs port mac set
			for(Entry<String, NetworkDevice> entry : subnetIpSwitchs.entrySet()){
				
				Set<String> set = getSwitchPortMac(entry.getKey());
				subnetSwitchsNextMacs.put(entry.getKey(), set);
				
			}
			
			nodes[0] = new TreeNode(gatewayId, 0, gatewayIp, true, "../../../css/zTreeStyle/img/diy/gateway1.png");
			
			//--------------------------------------用于测试用的数据的构造 end------------------------------------//
			
//			checkSwitchMac(switchsNextMacs);
			
			//循环生成拓扑结构树，每生成一个分支，就把该分支删除掉，直到将网关下所有分支全部生成完毕
			while(subnetSwitchsNextMacs.size() > 0){
				generateNodeTree(gatewayIp,subnetSwitchsNextMacs);
			}
			
			//展示生成的节点，这里的数据就可以用来生成树需要的json数据了，用类似于gson的工具可以直接生成
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
		 * 这个方法采用了递归
		 * 我们是通过snmp获取的交换机下挂设备mac地址，获取的下挂设备的mac地址集合包括所有的直连设备（包括交换机和其他设备）和直连设备下挂设备的mac地址的集合
		 * 给上面的话举个例子交换机1下挂交换机2、3，和设备a，2下挂设备b，则1下挂设备2、3、a、b，2下挂设备b，3则没有下挂设备，如果设备b也是交换机，
		 * 下挂了c、d，则c和d既属于1，也属于2
		 * 这里也可以看出父节点的下挂设备包含了子节点和子节点的下挂设备，所有父节点下挂设备一定比子节点下挂设备多
		 * @param parentIp 父节点的ip（父节点可以是网关或交换机）
		 * @param switchsNextMacs 父节点下的所有支持snmp的交换机及交换机下挂的设备的mac  地址Map<String（交换机IP）,Set<String>（交换机下挂设备的mac地址集合）>
		 * 
		 * 基于上面的限定，我们首先要得出一个结论，即拥有最多下挂设备的交换机一定是parentIp的直接子节点，
		 * 这个可以反证一个，如果不是直接子节点的话那么它和parentIp直接一定还有其他交换机，即它的父节点，但是根据上面的限定，子节点的下挂设备不可能大于父节点的下挂设备
		 * 
		 * 首先以默认网关为父节点，找到下挂设备最多的交换机，这样这个交换机就可以确定是跟默认网关直连的了，然后再看这个交换机下的所有下挂设备是否包含交换机，如果包含则
		 * 将这个交换机作为parentIp，将所有下挂的交换机作为switchsNextMacs，再次调用自身，直到没有下挂交换机为止
		 */
		private static void generateNodeTree(String parentIp,Map<String,Set<String>> switchsNextMacs){
			//找出所有交换机中下挂设备最多的一个交换机
			String maxSwitchIp = getNextMaxSwitchIp(switchsNextMacs);
			
			Set<String> parentSet = null;
			if(subnetSwitchsNextMacs.containsKey(parentIp)){
				//获取当前处理的交换机的父设备的所有下挂设备
				parentSet = subnetSwitchsNextMacs.get(parentIp);
				//默认网关不支持snmp，无法获取所有下挂设备
				if(null != parentSet){
					//将当前处理的交换机从父设备的下挂设备中剔除，直到剔除干净，才算处理完了父设备
					//@@标记A
					parentSet.remove(subnetIpDevices.get(maxSwitchIp).getMacAddress());
				}
			}
			//利用上面结论，这个下挂设备最多的交换机是parentIp直连的，生成节点数据
			nodes[nodeIndex] = new TreeNode(getDeviceIp(maxSwitchIp), getDeviceIp(parentIp), maxSwitchIp, true, "../../../css/zTreeStyle/img/diy/switch1.png");
			nodeIndex++;
			
			//获取当前处理的有最多下挂设备的交换机的所有下挂设备
			Set<String> nextMacs = subnetSwitchsNextMacs.get(maxSwitchIp);
			
			//<ip,Set<mac>> 用于存储当前处理的有最多下挂设备的交换机的下挂交换机
			Map<String,Set<String>> nextMap = new HashMap<String,Set<String>>();
			
			for(String mac : nextMacs){
				if(subnetMacDevices.containsKey(mac) && subnetMacDevices.get(mac).getDeviceType() == 2){
					String switchIp = subnetMacDevices.get(mac).getIpAddress();
					nextMap.put(switchIp,switchsNextMacs.get(switchIp));
					//把是当前处理的有最多下挂设备的交换机的所有下挂交换机从它的父交换机的下挂设备中清除，
					//这样当回到处理父交换机时就不用再处理这些肯定不是跟她直连的交换机了
					if(null != parentSet){
						parentSet.remove(mac);
					}
				}
			}

			while(nextMap.size() > 0){
//				System.out.println("maxSwitchIp:"+maxSwitchIp+"  nextMap size:"+nextMap.size());
				//当下挂交换机的数量大于0时递归调用自己
				generateNodeTree(maxSwitchIp,nextMap);
				
				//调用完成后重新获取当前处理交换机的下挂交换机，如果还大于0继续
				//交换机的剔除是在@@标记A处调用的
				nextMap.clear();

				for(String mac : nextMacs){
					if(subnetMacDevices.containsKey(mac) && subnetMacDevices.get(mac).getDeviceType() == 2){
						String switchIp = subnetMacDevices.get(mac).getIpAddress();
						nextMap.put(switchIp,switchsNextMacs.get(switchIp));
					}
				}
			}

			//进入到这里代表当前处理的交换机已经没有下挂交换机了，可以把其他的下挂设备生成节点数据了，
			//要注意的是：因为父节点包含子节点和子节点的所有子节点，所有有些子节点的下挂设备已经生成过了，存储在hasNodeComputerMac里面的
			//把子节点的下挂设备剔除掉就是当前交换机自己的下挂设备啦
			for(String mac : nextMacs){
				if(subnetMacDevices.containsKey(mac) && !hasNodeComputerMac.contains(mac)){
					String computerIp = subnetMacDevices.get(mac).getIpAddress();;
					nodes[nodeIndex] = new TreeNode(getDeviceIp(computerIp), getDeviceIp(maxSwitchIp), computerIp, "../../../css/zTreeStyle/img/diy/computer1.png");
					hasNodeComputerMac.add(mac);
					nodeIndex++;
				}
			}
			
			//最后把自己从需要处理的交换机集合中剔除掉就行了
			subnetSwitchsNextMacs.remove(maxSwitchIp);
		}
		
		
		//找出所有交换机中下挂设备最多的一个交换机
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