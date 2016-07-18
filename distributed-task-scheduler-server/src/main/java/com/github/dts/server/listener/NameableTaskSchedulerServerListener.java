package com.github.dts.server.listener;

import com.github.dts.core.util.NetUtils;

/**
 * 为服务监听器添加名字
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class NameableTaskSchedulerServerListener extends AbstractTaskSchedulerServerListener 
	implements Nameable {

	private String leaderListenerName; // 监听器的名称
	
	public void setLeaderListenerName(String leaderListenerName) {
		this.leaderListenerName = leaderListenerUniqueName(leaderListenerName);
	}
	
	/**
	 * 每个监听器都有唯一的名字
	 * 
	 * @param name
	 * @return
	 */
	protected String leaderListenerUniqueName(String name) {
		return name + "@" + NetUtils.getLocalIpAddress(); // name@IP
	}
	
	@Override
	public String getName() {
		return leaderListenerName;
	}
}
