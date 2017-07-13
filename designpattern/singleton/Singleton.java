package com.designpattern;

public class Singleton {

	/**
	 * @param args
	 * 单例设计模式：保证类在内存中只有一个实例
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
 * 饿汉式
 * */
class singleton1 {
	//1 私有构造方法,其他类不能访问该构造方法
	private singleton1() {}
	//2 创建本类对象
	private static singleton1 s = new singleton1();
	//3 对外提供公共的访问方法
	public static singleton1 getinstance() {
		return s;
	}
}
/*
 * 懒汉式 单例的延迟加载模式
 * */
class singleton2 {
	//1 私有构造方法,其他类不能访问该构造方法
	private singleton2() {}
	//2 声明一个引用
	private static singleton2 s ;
	//3 对外提供公共的访问方法
	public static singleton2 getinstance() {
		if(s == null){
			//线程1等待，线程2等待
			s = new singleton2();
		}
		return s;
	}
}
/*
 * 饿汉式和懒汉式区别
 * 1 饿汉式是空间换时间  懒汉式是时间换空间
 * 2 在所线程访问时，饿汉式不会创建多个对象 懒汉式可能会创建多个对象
 * */
