package com.designpattern;

import java.io.IOException;

public class Runtimeexample {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//�������ģʽӦ�ó���
		Runtime r = Runtime.getRuntime(); //��ȡ����ʱ����
//		r.exec("shutdown -s -t 300");
		r.exec("shutdown -a");	
		//��һ�����޸ĵģ��ڶ������޸�֮��ģ�������ͬһ������
	}
}
