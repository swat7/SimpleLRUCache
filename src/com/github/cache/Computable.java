package com.github.cache;

//Interface which computes V for every key K
public interface Computable<K, V> {
	public V compute(K key) throws InterruptedException;
}
