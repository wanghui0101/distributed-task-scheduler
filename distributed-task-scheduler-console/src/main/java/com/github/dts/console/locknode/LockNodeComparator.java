package com.github.dts.console.locknode;

import java.util.Comparator;

/**
 * 锁节点排序比较器
 * 
 * @author wh
 * @since 0.0.2
 */
public enum LockNodeComparator implements Comparator<LockNode> {

	INSTANCE;

	@Override
	public int compare(LockNode node1, LockNode node2) {
		return node1.getSn().compareTo(node2.getSn()); // 使用sn属性判断
	}

}
