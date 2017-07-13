package com.netcontrol;

import java.util.Comparator;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;

/** 
 * ������� 
*/  
public class MultipleTree {  
 public static void main(String[] args) {  
	 
  // ��ȡ������ݽ�����б�   
  List dataList = VirtualDataGenerator.getVirtualResult();    
    
  // �ڵ��б�ɢ�б�������ʱ�洢�ڵ����  
  HashMap nodeList = new HashMap();  
  // ���ڵ�  
  Node root = null;  
  // ���ݽ��������ڵ��б�����ɢ�б�  
  for (Iterator it = dataList.iterator(); it.hasNext();) {  
   Map dataRecord = (Map) it.next();  
   Node node = new Node();  
   node.id = (String) dataRecord.get("id");  
   node.text = (String) dataRecord.get("text");  
   node.parentId = (String) dataRecord.get("parentId");  
   nodeList.put(node.id, node);  
  }  
  // ��������Ķ����  
  Set entrySet = nodeList.entrySet();  
  for (Iterator it = entrySet.iterator(); it.hasNext();) {  
   Node node = (Node) ((Map.Entry) it.next()).getValue();  
   if (node.parentId == null || node.parentId.equals("")) {  
    root = node;  
   } else {  
    ((Node) nodeList.get(node.parentId)).addChild(node);  
   }  
  }  
  // �����������β˵���JSON�ַ���  
  System.out.println(root.toString());     
  // �Զ�������к�������  
  root.sortChildren();  
  // �����������β˵���JSON�ַ���  
  System.out.println(root.toString());      
    
 }  
     
}   
  
/** 
 * �ڵ�Ƚ��� 
 */  
class NodeIDComparator implements Comparator {  
 // ���սڵ��űȽ�  
 public int compare(Object o1, Object o2) {  
  int j1 = Integer.parseInt(((Node)o1).id);  
     int j2 = Integer.parseInt(((Node)o2).id);  
     return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));  
 }   
}  
  
