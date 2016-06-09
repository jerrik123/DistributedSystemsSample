package com.mangocity.distribute.thread.counter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mangocity.distribute.thread.annotation.ThreadSafe;

@ThreadSafe
public class ConcurrentHashMultisetCounter {

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
					counter.addConcurrentHashMultiset(keys);// ConcurrentHashMultiset 计数,线程安全
				}
			});
		}

		try {
			System.out.println("main thread sleep.");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("total Count: " + counter.getConcurrentHashMultisetKeyCounts());
		executorService.shutdown();
	}
	
	/**
	 * total Count: 1000
	 * 
	 * 
	 */

}
