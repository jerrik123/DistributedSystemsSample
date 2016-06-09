package com.mangocity.distribute.thread.counter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Counter {

	private Map<String, Integer> counterMap = new HashMap<String, Integer>();

	private Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<String, Integer>();

	private Multiset<String> multiset = HashMultiset.create();

	private Multiset<String> concurrentHashMultiset = ConcurrentHashMultiset.create();

	// 没有同步锁 线程不安全
	public void addNoSync(String key) {
		Integer value = counterMap.get(key);
		if (null == value) {
			counterMap.put(key, 1);
		} else {
			counterMap.put(key, value + 1);
		}
	}

	// 通过ConcurrentHashMap来计数(不是线程安全的)
	/**
	 * 例如:get(key_1)等于1,在准备put(key_1,2)之前。 有另外的线程给该key 计数加1. 这种情况下,计数就不会准确
	 * 
	 * 或者说有多个线程同时到code: 1,这样就会导致计数失败
	 */
	public void addConcurrentHashMap(String key) {
		Integer value = concurrentHashMap.get(key);
		if (null == value) {// code: 1
			concurrentHashMap.put(key, 1);
		} else {
			concurrentHashMap.put(key, value + 1);
		}
	}

	// 线程安全
	public synchronized void addSync(String key) {
		Integer value = counterMap.get(key);
		if (null == value) {
			counterMap.put(key, 1);
		} else {
			counterMap.put(key, value + 1);
		}
	}

	// 用multiset
	public void addInMultiset(String keys) {
		multiset.add(keys);
	}

	// 用concurrentHashMultiset  线程安全
	public void addConcurrentHashMultiset(String keys) {
		concurrentHashMultiset.add(keys);
	}

	public Map<String, Integer> getMap() {
		return counterMap;
	}

	public Map<String, Integer> getConcurrentHashMap() {
		return concurrentHashMap;
	}

	public int getMultisetKeyCounts() {
		int size = 0;
		for (String key : multiset.elementSet()) {
			size += multiset.count(key);
		}
		return size;
	}
	
	public int getConcurrentHashMultisetKeyCounts() {
		int size = 0;
		for (String key : concurrentHashMultiset.elementSet()) {
			size += concurrentHashMultiset.count(key);
		}
		return size;
	}

}
