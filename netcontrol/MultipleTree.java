package com.netcontrol;

import java.util.Comparator;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;

/** 
 * 多叉树类 
*/  
public class MultipleTree {  
 public static void main(String[] args) {  
	 
  // 读取层次数据结果集列表   
  List dataList = VirtualDataGenerator.getVirtualResult();    
    
  // 节点列表（散列表，用于临时存储节点对象）  
  HashMap nodeList = new HashMap();  
  // 根节点  
  Node root = null;  
  // 根据结果集构造节点列表（存入散列表）  
  for (Iterator it = dataList.iterator(); it.hasNext();) {  
   Map dataRecord = (Map) it.next();  
   Node node = new Node();  
   node.id = (String) dataRecord.get("id");  
   node.text = (String) dataRecord.get("text");  
   node.parentId = (String) dataRecord.get("parentId");  
   nodeList.put(node.id, node);  
  }  
  // 构造无序的多叉树  
  Set entrySet = nodeList.entrySet();  
  for (Iterator it = entrySet.iterator(); it.hasNext();) {  
   Node node = (Node) ((Map.Entry) it.next()).getValue();  
   if (node.parentId == null || node.parentId.equals("")) {  
    root = node;  
   } else {  
    ((Node) nodeList.get(node.parentId)).addChild(node);  
   }  
  }  
  // 输出无序的树形菜单的JSON字符串  
  System.out.println(root.toString());     
  // 对多叉树进行横向排序  
  root.sortChildren();  
  // 输出有序的树形菜单的JSON字符串  
  System.out.println(root.toString());      
    
 }  
     
}   
  
/** 
 * 节点比较器 
 */  
class NodeIDComparator implements Comparator {  
 // 按照节点编号比较  
 public int compare(Object o1, Object o2) {  
  int j1 = Integer.parseInt(((Node)o1).id);  
     int j2 = Integer.parseInt(((Node)o2).id);  
     return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));  
 }   
}  
  
