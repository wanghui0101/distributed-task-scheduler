package com.github.dts.core.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * 比较器工具类
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class ComparatorUtils {

	public static <T> void sort(List<T> list, Comparator<T> comparator) {
		if (!CollectionUtils.isEmpty(list) && list.size() > 1) {
			Collections.sort(list, comparator);
		}
	}
}
