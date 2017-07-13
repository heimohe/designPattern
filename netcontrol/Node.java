package com.netcontrol;

/** 
* �ڵ��� 
*/
public class Node {
	/** 
	  * �ڵ��� 
	  */  
	 public String id;  
	 /** 
	  * �ڵ����� 
	  */  
	 public String text;  
	 /** 
	  * ���ڵ��� 
	  */  
	 public String parentId;  
	 /** 
	  * ���ӽڵ��б� 
	  */  
	 private Children children = new Children();  
	   
	 // ���������ƴ��JSON�ַ���  
	 public String toString() {    
	  String result = "{"  
	   + "id : '" + id + "'"  
	   + ", text : '" + text + "'";  
	    
	  if (children != null && children.getSize() != 0) {  
	   result += ", children : " + children.toString();  
	  } else {  
	   result += ", leaf : true";  
	  }  
	      
	  return result + "}";  
	 }  
	   
	 // �ֵܽڵ��������  
	 public void sortChildren() {  
	  if (children != null && children.getSize() != 0) {  
	   children.sortChildren();  
	  }  
	 }  
	   
	 // ��Ӻ��ӽڵ�  
	 public void addChild(Node node) {  
	  this.children.addChild(node);  
	 }  
}

  
  