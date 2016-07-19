package com.github.dts.console.locknode;

import org.apache.commons.lang3.StringUtils;

import com.github.dts.core.support.curator.CuratorOperations;

/**
 * 默认的锁节点解析器
 * 
 * @author wh
 * 
 * since 0.0.2
 */
public class DefaultLockNodeResolver implements LockNodeResolver {
	
	private static final String LOCK_FLAG = "-lock-";

	private CuratorOperations curatorOperations;
	private String leaderListenerPath;
	
	public DefaultLockNodeResolver(CuratorOperations curatorOperations, 
			String leaderListenerPath) {
		this.curatorOperations = curatorOperations;
		this.leaderListenerPath = leaderListenerPath;
	}
	
	public void setCuratorOperations(CuratorOperations curatorOperations) {
		this.curatorOperations = curatorOperations;
	}
	
	public CuratorOperations getCuratorOperations() {
		return curatorOperations;
	}
	
	public void setLeaderListenerPath(String leaderListenerPath) {
		this.leaderListenerPath = leaderListenerPath;
	}

	public String getLeaderListenerPath() {
		return leaderListenerPath;
	}

	@Override
	public boolean support(String lock) {
		return lock.contains(LOCK_FLAG);
	}

	@Override
	public LockNode resolve(String lock) {
		String leaderLockPath = leaderListenerPath + "/" + lock;
		String ip = new String(curatorOperations.getData(leaderLockPath));
		int sn = Integer.valueOf(StringUtils.substringAfter(lock, LOCK_FLAG));
		return new LockNode(ip, sn);
	}

}
