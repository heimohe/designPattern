package com.netcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** 
 * ��������Ĳ������ 
 */  
class VirtualDataGenerator {  
 // ��������Ľ�����б�ʵ��Ӧ���У�������Ӧ�ô����ݿ��в�ѯ��ã�  
 @SuppressWarnings("unchecked")
public static List getVirtualResult() {      
  List dataList = new ArrayList();  
    
  HashMap dataRecord1 = new HashMap();  
  dataRecord1.put("id", "112000");  
  dataRecord1.put("text", "������");  
  dataRecord1.put("parentId", "");  
    
  HashMap dataRecord2 = new HashMap();  
  dataRecord2.put("id", "112200");  
  dataRecord2.put("text", "·����");  
  dataRecord2.put("parentId", "112000");  
    
  HashMap dataRecord3 = new HashMap();  
  dataRecord3.put("id", "112100");  
  dataRecord3.put("text", "���Q�C");  
  dataRecord3.put("parentId", "112200");  
        
  HashMap dataRecord4 = new HashMap();  
  dataRecord4.put("id", "113000");  
  dataRecord4.put("text", "������");  
  dataRecord4.put("parentId", "112100");  
        
  HashMap dataRecord5 = new HashMap();  
  dataRecord5.put("id", "100000");  
  dataRecord5.put("text", "������");  
  dataRecord5.put("parentId", "112100");  
    
  HashMap dataRecord6 = new HashMap();  
  dataRecord6.put("id", "110000");  
  dataRecord6.put("text", "�W�j��ӡ�C");  
  dataRecord6.put("parentId", "112100");  
    
  HashMap dataRecord7 = new HashMap();  
  dataRecord7.put("id", "111000");  
  dataRecord7.put("text", "�͑��C");  
  dataRecord7.put("parentId", "112100");    
      
  dataList.add(dataRecord1);  
  dataList.add(dataRecord2);  
  dataList.add(dataRecord3);  
  dataList.add(dataRecord4);  
  dataList.add(dataRecord5);  
  dataList.add(dataRecord6);  
  dataList.add(dataRecord7);  
    
  return dataList;  
 }   
}  
