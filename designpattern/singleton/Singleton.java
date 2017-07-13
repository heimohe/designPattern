package com.designpattern;

public class Singleton {

	/**
	 * @param args
	 * �������ģʽ����֤�����ڴ���ֻ��һ��ʵ��
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//singleton1 s1 = new singleton1();
		//singleton1 s2 = new singleton1();
//		singleton1 s2 = singleton1.s;
//		System.out.println(s1==s2);
		singleton1 s1 = singleton1.getinstance();
	}

}
/*
 * ����ʽ
 * */
class singleton1 {
	//1 ˽�й��췽��,�����಻�ܷ��ʸù��췽��
	private singleton1() {}
	//2 �����������
	private static singleton1 s = new singleton1();
	//3 �����ṩ�����ķ��ʷ���
	public static singleton1 getinstance() {
		return s;
	}
}
/*
 * ����ʽ �������ӳټ���ģʽ
 * */
class singleton2 {
	//1 ˽�й��췽��,�����಻�ܷ��ʸù��췽��
	private singleton2() {}
	//2 ����һ������
	private static singleton2 s ;
	//3 �����ṩ�����ķ��ʷ���
	public static singleton2 getinstance() {
		if(s == null){
			//�߳�1�ȴ����߳�2�ȴ�
			s = new singleton2();
		}
		return s;
	}
}
/*
 * ����ʽ������ʽ����
 * 1 ����ʽ�ǿռ任ʱ��  ����ʽ��ʱ�任�ռ�
 * 2 �����̷߳���ʱ������ʽ���ᴴ��������� ����ʽ���ܻᴴ���������
 * */
