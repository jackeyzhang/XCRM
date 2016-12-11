package com.xcrm.common.util;

public class Tuple<A, B> {
	public final A first;
	public final B second;

	public Tuple(A a, B b) {
		this.first = a;
		this.second = b;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

}
