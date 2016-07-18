package com.github.dts.server.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 选举的主要实现类
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class AbstractTaskSchedulerServerListener extends LeaderSelectorListenerAdapter 
	implements Listener, InitializingBean, DisposableBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String leaderListenerPath; // 监听器在ZooKeeper上的路径

	private CuratorFramework client;

	public void setLeaderListenerPath(String leaderListenerPath) {
		this.leaderListenerPath = leaderListenerPath;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

	private LeaderSelector leaderSelector;
	private boolean leader = false;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(client);
		Assert.notNull(leaderListenerPath);
		
		// 利用Curator封装的选举机制, 确保任意时刻有且仅有一个leader
		leaderSelector = new LeaderSelector(client, leaderListenerPath, this); 
		leaderSelector.autoRequeue(); // 使丢失leader的服务, 仍可重新加入到竞争leader的行列中
		leaderSelector.start();
	}

	/**
	 * Server竞争到leader后触发
	 * 
	 * @see org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter
	 */
	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		leader = true; // 标记已成为主节点
		doTakeLeaderShip(client); // 交给子类实现
	}
	
	protected abstract void doTakeLeaderShip(CuratorFramework client) throws Exception;

	@Override
	public boolean isLeader() {
		return leader;
	}

	@Override
	public void destroy() throws Exception {
		leaderSelector.close();
	}
	

}