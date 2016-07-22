package com.github.dts.console.locknode;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 锁节点
 * 
 * @author wh
 * @since 0.0.2
 */
public class LockNode {
	
	private String ip;
	private int sn;
	
	public LockNode(String ip, int sn) {
		this.ip = ip;
		this.sn = sn;
	}

	public String getIp() {
		return ip;
	}

	public Integer getSn() {
		return sn;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
