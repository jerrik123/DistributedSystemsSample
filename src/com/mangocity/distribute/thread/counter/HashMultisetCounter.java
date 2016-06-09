package com.mangocity.distribute.thread.counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.mangocity.distribute.thread.annotation.UnThreadSafe;

@UnThreadSafe
public class HashMultisetCounter {

	public static void main(String[] args) {
		final Counter counter = new Counter();
		final Random random = new Random();

		final int[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		ExecutorService executorService = Executors.newCachedThreadPool();

		for (int i = 0; i < 1000; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					String keys = "key_" + arr[random.nextInt(10) % arr.length];
					// System.out.println("keys: " + keys);
					counter.addInMultiset(keys);// Multiset 计数,线程不安全
				}
			});
		}

		try {
			System.out.println("main thread sleep.");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("total Count: " + counter.getMultisetKeyCounts());
		executorService.shutdown();
	}
	
	/**
	 * total Count: 991
	 * 
	 * 
	 */

}
