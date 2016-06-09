package com.mangocity.distribute.thread.counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mangocity.distribute.thread.annotation.UnThreadSafe;

@UnThreadSafe
public class UnThreadSafeCounterTest {

	public static void main(String[] args) {
		final Counter counter = new Counter();
		final Random random = new Random();

		final int[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		ExecutorService executorService = Executors.newCachedThreadPool();

		for (int i = 0; i < 1000; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					String keys = "key_" + arr[random.nextInt(10) % arr.length];//设定可能出现重复key
					System.out.println("keys: " + keys);
					counter.addNoSync(keys);//没有加同步锁
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
	 * newMap: {key_7=100, key_6=83, key_5=100, key_4=102, key_3=114, key_2=89, key_1=95, key_0=99, key_9=96, key_8=111} ,total Count: 989

	 * 
	 * seconde times:
	 * newMap: {key_7=86, key_6=112, key_5=96, key_4=90, key_3=106, key_2=109, key_1=97, key_0=104, key_9=109, key_8=91} ,total Count: 1000


	 * third times:
	 * newMap: {key_7=106, key_6=97, key_5=105, key_4=98, key_3=98, key_2=105, key_1=97, key_0=94, key_9=90, key_8=91} ,total Count: 981

	 * 
	 * 4th times:
	 * newMap: {key_7=105, key_6=83, key_5=108, key_4=86, key_3=103, key_2=107, key_1=91, key_0=110, key_9=111, key_8=93} ,total Count: 997

	 */

}
