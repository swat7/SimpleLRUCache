package com.github.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LinkedLRUCache<K,V> extends LinkedHashMap<K,V> {
	/**
	 * 
	 */
	private final int size;
	public LinkedLRUCache(int size) {
		super(size, 0.75f, true);
		this.size = size;
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return size() > size;
	}
	
	public void showQueue(){
		Set<Map.Entry<K,V>> entrySet = this.entrySet();
		for(Map.Entry<K, V> entry : entrySet) {
			System.out.print(entry.getKey() + " ");
		}
	}
	
	public static void main(String[] args) {
		
		LinkedLRUCache<Integer, Integer> lruCache = new LinkedLRUCache<Integer, Integer>(5);
		lruCache.put(2,4);
		lruCache.put(4, 16);
		lruCache.put(3, 9);
		lruCache.put(5, 25);
		lruCache.put(10, 100);
		lruCache.showQueue();
		System.out.println(lruCache.get(2));
		System.out.println(lruCache.get(4));
		System.out.println(lruCache.get(3));
		System.out.println(lruCache.get(5));
		System.out.println(lruCache.get(2));
		System.out.println(lruCache.get(10));
		System.out.println(lruCache.get(5));
		lruCache.showQueue();
		
	}
}
