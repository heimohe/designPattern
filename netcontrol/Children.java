package com.netcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** 
* �����б��� 
*/  
class Children {  
 @SuppressWarnings("unchecked")
private List list = new ArrayList();  
   
 public int getSize() {  
  return list.size();  
 }  
   
 @SuppressWarnings("unchecked")
public void addChild(Node node) {  
  list.add(node);  
 }  
// ƴ�Ӻ��ӽڵ��JSON�ַ���  
public String toString() {  
 String result = "[";    
 for (Iterator it = list.iterator(); it.hasNext();) {  
  result += ((Node) it.next()).toString();  
  result += ",";  
 }  
 result = result.substring(0, result.length() - 1);  
 result += "]";  
 return result;  
}  
// ���ӽڵ�����  
public void sortChildren() {  
 // �Ա���ڵ��������  
 // �ɸ��ݲ�ͬ���������ԣ����벻ͬ�ıȽ��������ﴫ��ID�Ƚ���  
 Collections.sort(list, new NodeIDComparator());  
 // ��ÿ���ڵ����һ��ڵ��������  
 for (Iterator it = list.iterator(); it.hasNext();) {  
  ((Node) it.next()).sortChildren();  
 }  
}  
} 