package com.suanfa;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Yue {
	public static void main(String[] args) {  
      Scanner scanner = new Scanner(System.in);  
      System.out.print("��������������");  
      int totalNum = scanner.nextInt();  
      System.out.print("�����뱨���Ĵ�С��");  
      int cycleNum = scanner.nextInt();  
      System.out.print("�����뿪ʼ��ţ�");  
      int  startNO= scanner.nextInt();  
      yuesefu(totalNum, cycleNum,startNO);  
  }  
 
 public static void yuesefu(int totalNum, int countNum,int startNO) {  
      // ��ʼ������  
      List<Integer> start = new ArrayList<Integer>();  
      for (int i = 1; i <= totalNum; i++) {  
          start.add(i);  
      }  
      //���±�ΪK��ʼ����  
      int k = startNO-1;  
      while (start.size() >0) {  
          System.out.println(start);
          //��m�˵�����λ��  
          k = (k + countNum) % (start.size()) - 1;  
         // �ж��Ƿ񵽶�β  ����βʱ��k=-1
          if (k < 0) {  
              System.out.println(start.get(start.size()-1));  
              start.remove(start.size() - 1);  
              k = 0;  
          } else {  
              System.out.println(start.get(k));  
              start.remove(k);  
          }  
      }  
  }  
}