package com.github.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class Node<K,V> {
	private K key;
	private V value;
	
	public Node(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
}
class EntryValue<K,V> {
	private V value;
	private Node<K,V> node;
	
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public Node<K,V> getNode() {
		return node;
	}
	public void setNode(Node<K,V> node) {
		this.node = node;
	}
}
public class LRUCache<K,V> {
	private final int size;
	private final Map<K,EntryValue<K,V>> map;
	private final Queue<Node<K,V>> queue;
	private final Computable<K,V> loader;
	
	public LRUCache(int size, Computable<K,V> loader) {
		this.size = size;
		this.map = new HashMap<K,EntryValue<K,V>>(size);
		this.queue = new LinkedList<Node<K,V>>();
		this.loader = loader;
	}
	
	public V get(K key) {
		if(!map.containsKey(key)) {
			try {
				//Fetch the value from the data source or a loader
				V value = loader.compute(key);			
				evictEntryNode();
				return createNewEntryNode(key, value).getValue();
			} catch (Exception e) {}
		} else {
			//Change the position
			EntryValue<K,V> entryValue = map.get(key);
			Node<K,V> oldNode = entryValue.getNode();
			queue.remove(oldNode);
			queue.add(oldNode);
			return entryValue.getValue();
		}
		return null;
	}
	
	public void set(K key, V val) {
		if(map.containsKey(key)) {
			EntryValue<K,V> entryValue = map.get(key);
			entryValue.setValue(val);
			Node<K,V> oldNode = entryValue.getNode();
			oldNode.setValue(val);
			queue.remove(oldNode);
			queue.add(oldNode);
		} else {
			evictEntryNode();
			createNewEntryNode(key, val);
		}
	}
	
	private EntryValue<K,V> createNewEntryNode(K key, V value) {
		EntryValue<K,V> newEntry = new EntryValue<K,V>();
		newEntry.setValue(value);
		Node<K,V> newNode = new Node<K,V>(key, value);
		newEntry.setNode(newNode);
		map.put(key, newEntry);
		queue.add(newNode);
		return newEntry;
	}
	
	private void evictEntryNode() {
		if(map.size() >= size) {
			Node<K,V> frontNode = queue.poll();
			map.remove(frontNode.getKey());
			System.out.println("Evicting : "+frontNode.getKey());	
		}
	}
	
	public void showQueue(){
		Iterator<Node<K,V>> it = queue.iterator();
		while(it.hasNext()) {
			System.out.print(it.next().getKey() + " ");
		}
	}
	public static void main(String[] args) {
		IntegerInputLoader loader = new IntegerInputLoader();
		LRUCache<Integer, Integer> lruCache = new LRUCache<Integer, Integer>(5, loader);
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
