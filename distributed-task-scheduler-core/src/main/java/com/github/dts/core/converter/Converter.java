package com.github.dts.core.converter;

public interface Converter<S, T> {

	T from(S source);
	
	S to(T target);
	
}
