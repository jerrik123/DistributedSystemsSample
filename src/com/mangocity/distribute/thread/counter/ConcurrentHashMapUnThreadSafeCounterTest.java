package com.mangocity.distribute.thread.counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mangocity.distribute.thread.annotation.UnThreadSafe;

@UnThreadSafe
public class ConcurrentHashMapUnThreadSafeCounterTest {

	public static void main(String[] args) {
		final Counter counter = new Counter();
		final Random random = new Random();

		final int[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		ExecutorService executorService = Executors.newCachedThreadPool();

		for (int i = 0; i < 1000; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					String keys = "key_" + arr[random.nextInt(10) % arr.length];
					//System.out.println("keys: " + keys);
					counter.addConcurrentHashMap(keys);// ConcurrentHashMap,没有加锁
				}
			});
		}

		try {
			System.out.println("main thread sleep.");
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Map<String, Integer> newMap = new HashMap(counter.getConcurrentHashMap());
		System.out.println("newMap: " + newMap + " ,total Count: " + getTotalCount(newMap));
		executorService.shutdown();
	}

	private static long getTotalCount(Map<String, Integer> newMap) {
		long sum = 0;
		long t = 0;
		for (String key : newMap.keySet()) {
			t = newMap.get(key);
			sum += t;
		}
		return sum;
	}

	/***
	 * TEST RESULT first times: 
	 *newMap: {key_7=74, key_6=88, key_5=107, key_4=115, key_3=88, key_2=101, key_1=89, key_0=104, key_9=100, key_8=91} ,total Count: 957
	 * 
	 * 
	 * seconde times: 
	 * newMap: {key_7=102, key_6=94, key_5=99, key_4=86, key_3=98, key_2=108, key_1=99, key_0=77, key_9=88, key_8=114} ,total Count: 965
	 * 
	 * 
	 * third times: 
	 * newMap: {key_7=97, key_6=84, key_5=87, key_4=111, key_3=102, key_2=85, key_1=117, key_0=84, key_9=86, key_8=110} ,total Count: 963
	 * 
	 * 
	 */

}
