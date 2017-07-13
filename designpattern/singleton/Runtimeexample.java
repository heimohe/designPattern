package com.designpattern;

import java.io.IOException;

public class Runtimeexample {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//单例设计模式应用场景
		Runtime r = Runtime.getRuntime(); //获取运行时对象
//		r.exec("shutdown -s -t 300");
		r.exec("shutdown -a");	
		//第一次是修改的，第二次是修改之后的，两个是同一个对象
	}
}
