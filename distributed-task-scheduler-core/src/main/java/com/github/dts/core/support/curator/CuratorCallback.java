package com.github.dts.core.support.curator;

import org.apache.curator.framework.CuratorFramework;

/**
 * Curator的回调接口
 * 
 * @author wh
 * @since 0.0.1
 * 
 * @param <T> 方法返回值类型
 */
public interface CuratorCallback<T> {

	T doInCurator(CuratorFramework client) throws Exception;
	
}
