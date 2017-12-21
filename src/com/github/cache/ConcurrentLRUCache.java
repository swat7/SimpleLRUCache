package com.github.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLRUCache<K,V> {

	private final int size;
	private final ConcurrentHashMap<K,V> map;
	private final ConcurrentLinkedQueue<K> queue;
	private final Computable<K,V> loader;
	
	public ConcurrentLRUCache(int size, Computable<K,V> loader) {
		this.size = size;
		this.map = new ConcurrentHashMap<K,V>(size);
		this.queue = new ConcurrentLinkedQueue<K>();
		this.loader = loader;
	}
	
	public V get(K key) {
		V result = map.get(key);
		if(result == null) {
			try {
				V value = loader.compute(key);
				evictEntryNode();
				createNewEntryNode(key, value);
				return value;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			changePosQueueNode(key);
		}
		return result;
	}
	
	public void set(K key, V val) {
		V result = map.get(key);
		if(result == null) {
			evictEntryNode();
			createNewEntryNode(key, val);
		} else {
			map.putIfAbsent(key, val);
			changePosQueueNode(key);
		}
	}
	
	@Override
	public String toString() {
		return queue.toString();
	}
	
	private synchronized void changePosQueueNode(K key) {
		queue.remove(key);
		queue.add(key);
	}
	
	private synchronized void createNewEntryNode(K key, V value) {
		map.put(key, value);
		queue.add(key);
	}
	
	private synchronized void evictEntryNode() {
		if(map.size() >= size) {
			K frontNode = queue.poll();
			map.remove(frontNode);
			System.out.println("Evicting : "+frontNode);	
		}
	}
	
	public static void main(String[] args) {
		IntegerInputLoader loader = new IntegerInputLoader();
		ConcurrentLRUCache<Integer, Integer> lruCache = new ConcurrentLRUCache<Integer, Integer>(5, loader);
		System.out.println(lruCache.get(2));
		System.out.println(lruCache.get(4));
		System.out.println(lruCache.get(3));
		System.out.println(lruCache.get(5));
		System.out.println(lruCache.get(2));
		System.out.println(lruCache.get(10));
		System.out.println(lruCache.get(5));
		
		System.out.println(lruCache.toString());
	}
}
