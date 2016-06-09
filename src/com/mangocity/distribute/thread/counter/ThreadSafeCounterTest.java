package com.mangocity.distribute.thread.counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mangocity.distribute.thread.annotation.ThreadSafe;

@ThreadSafe
public class ThreadSafeCounterTest {

	public static void main(String[] args) {
		final Counter counter = new Counter();
		final Random random = new Random();

		final int[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		ExecutorService executorService = Executors.newCachedThreadPool();

		for (int i = 0; i < 1000; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					String keys = "key_" + arr[random.nextInt(10) % arr.length];
					System.out.println("keys: " + keys);
					counter.addSync(keys);//同步方法
				}
			});
		}
		
		try {
			System.out.println("main thread sleep.");
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Map<String, Integer> newMap = new HashMap(counter.getMap());
		System.out.println("newMap: " + newMap + " ,total Count: " + getTotalCount(newMap));
		executorService.shutdown();
	}

	private static long getTotalCount(Map<String, Integer> newMap) {
		long sum = 0;
		long t = 0;
		for(String key :  newMap.keySet()){
			t = newMap.get(key);
			sum += t;
		}
		return sum;
	}
	
	/***
	 * TEST RESULT
	 * first times:
	 * newMap: {key_7=96, key_6=104, key_5=96, key_4=118, key_3=82, key_2=109, key_1=88, key_0=117, key_9=95, key_8=95} ,total Count: 1000

	 * 
	 * seconde times:
	 *newMap: {key_7=112, key_6=105, key_5=106, key_4=97, key_3=111, key_2=100, key_1=90, key_0=109, key_9=81, key_8=89} ,total Count: 1000


	 * third times:
	 * newMap: {key_7=103, key_6=106, key_5=97, key_4=84, key_3=83, key_2=100, key_1=88, key_0=111, key_9=114, key_8=114} ,total Count: 1000

	 * 
	 * 4th times:
	 * newMap: {key_7=85, key_6=96, key_5=97, key_4=96, key_3=104, key_2=108, key_1=115, key_0=96, key_9=97, key_8=106} ,total Count: 1000


	 */

}
