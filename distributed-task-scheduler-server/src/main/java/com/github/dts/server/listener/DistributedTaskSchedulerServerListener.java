package com.github.dts.server.listener;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.github.dts.core.util.NetUtils;

/**
 * 分布式任务调度器服务器监听. 任务调度器服务可部署多个, 但只有一个可以被选为主节点, 选举结果由此类监听
 * 
 * @author wh
 * @lastModified 2016-6-15 10:39:19
 */
public class DistributedTaskSchedulerServerListener extends LeaderSelectorListenerAdapter 
			implements TaskSchedulerServerListener, InitializingBean, DisposableBean {
	
	private static final Logger logger = LoggerFactory.getLogger(DistributedTaskSchedulerServerListener.class);
	
	private String leaderListenerName; // 监听器的名称
    private String leaderListenerPath; // 监听器在ZooKeeper上的路径
    
    private CuratorFramework client;

	public void setLeaderListenerName(String leaderListenerName) {
		this.leaderListenerName = leaderListenerUniqueName(leaderListenerName);
	}
	
	public void setLeaderListenerPath(String leaderListenerPath) {
		this.leaderListenerPath = leaderListenerPath;
	}

	/**
	 * 每个监听器都有唯一的名字
	 * @param name
	 * @return
	 */
	protected String leaderListenerUniqueName(String name) {
		return name + "@" + NetUtils.getLocalIpAddress(); // name@IP
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}
	
	private LeaderSelector leaderSelector;
    private boolean leader = false;

	@Override
	public void afterPropertiesSet() throws Exception {
    	leaderSelector = new LeaderSelector(client, leaderListenerPath, this); // 利用Curator封装的选举机制, 确保任意时刻有且仅有一个leader
    	leaderSelector.autoRequeue(); // 使丢失leader的服务, 仍可重新加入到竞争leader的行列中
    	leaderSelector.start();
	}

	/**
	 * <p>Server竞争到leader后触发
	 * <p>方法最后加入了死循环, 使获得leader后一直占有leader角色, 除非发生连接丢失等异常
	 * 
	 * @see org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter
	 */
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
    	leader = true;
    	logger.info("{} 被选举为主节点", getName());
    	while (true) {
            TimeUnit.HOURS.sleep(12);
        }
    }
    
    @Override
    public boolean isLeader() {
    	return leader;
    }
    
    @Override
    public String getName() {
		return leaderListenerName;
	}
	
	@Override
	public void destroy() throws Exception {
		leaderSelector.close();
	}
	
}
