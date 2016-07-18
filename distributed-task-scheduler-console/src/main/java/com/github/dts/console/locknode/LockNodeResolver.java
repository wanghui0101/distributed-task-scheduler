package com.github.dts.console.locknode;

/**
 * 锁节点解析器。提供判断是否可以解析，和解析锁对象的方法。
 * 
 * @author wh
 * 
 * since 0.0.2
 */
public interface LockNodeResolver {
	
	boolean support(String lock);

	LockNode resolve(String lock);
}
