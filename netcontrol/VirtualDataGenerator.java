package com.netcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** 
 * 构造虚拟的层次数据 
 */  
class VirtualDataGenerator {  
 // 构造无序的结果集列表，实际应用中，该数据应该从数据库中查询获得；  
 @SuppressWarnings("unchecked")
public static List getVirtualResult() {      
  List dataList = new ArrayList();  
    
  HashMap dataRecord1 = new HashMap();  
  dataRecord1.put("id", "112000");  
  dataRecord1.put("text", "防火墻");  
  dataRecord1.put("parentId", "");  
    
  HashMap dataRecord2 = new HashMap();  
  dataRecord2.put("id", "112200");  
  dataRecord2.put("text", "路由器");  
  dataRecord2.put("parentId", "112000");  
    
  HashMap dataRecord3 = new HashMap();  
  dataRecord3.put("id", "112100");  
  dataRecord3.put("text", "交換機");  
  dataRecord3.put("parentId", "112200");  
        
  HashMap dataRecord4 = new HashMap();  
  dataRecord4.put("id", "113000");  
  dataRecord4.put("text", "服務器");  
  dataRecord4.put("parentId", "112100");  
        
  HashMap dataRecord5 = new HashMap();  
  dataRecord5.put("id", "100000");  
  dataRecord5.put("text", "服務器");  
  dataRecord5.put("parentId", "112100");  
    
  HashMap dataRecord6 = new HashMap();  
  dataRecord6.put("id", "110000");  
  dataRecord6.put("text", "網絡打印機");  
  dataRecord6.put("parentId", "112100");  
    
  HashMap dataRecord7 = new HashMap();  
  dataRecord7.put("id", "111000");  
  dataRecord7.put("text", "客戶機");  
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
