package com.github.dts.core.converter;

/**
 * 转换器接口
 * 
 * @author wh
 *
 * @param <S> 源类型
 * @param <T> 目标类型
 * @since 0.0.1
 */
public interface Converter<S, T> {

	T from(S source);
	
	S to(T target);
	
}
