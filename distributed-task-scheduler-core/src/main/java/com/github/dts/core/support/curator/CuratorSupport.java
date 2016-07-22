package com.github.dts.core.support.curator;

/**
 * Curator支持类
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class CuratorSupport {

	/**
	 * 将异常都转换为CuratorException
	 * @param t
	 * @return
	 */
	public CuratorException convertToCuratorException(Throwable t) {
		return new CuratorException(t);
	}
}
