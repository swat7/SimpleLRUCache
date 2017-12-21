package com.github.cache;

public class IntegerInputLoader implements Computable<Integer,Integer>{

	@Override
	public Integer compute(Integer key) {
		return key * key;
	}
}
