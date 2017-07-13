package com.designpattern;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



public class Demo_timer {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		timer.schedule(new mytimetask(), new Date(117, 6, 9, 21, 15, 12));
		while(true){
			Thread.sleep(1000);
			Date a = new Date();
			System.out.println(a);
		}
	}
	
}
class mytimetask extends TimerTask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Æð´²±³Ó¢Óïµ¥´Ê");
	}
	
}