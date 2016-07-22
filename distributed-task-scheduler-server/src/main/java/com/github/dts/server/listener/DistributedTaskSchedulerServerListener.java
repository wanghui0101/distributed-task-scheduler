package com.github.dts.server.listener;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dts.server.ServerNameAware;

/**
 * 分布式任务调度器服务器监听. 任务调度器服务可部署多个, 但只有一个可以被选为主节点, 选举结果由此类监听
 * 
 * @author wh
 * @since 0.0.2
 */
public class DistributedTaskSchedulerServerListener extends AbstractTaskSchedulerServerListener 
	implements ServerNameAware {
	
	private static final Logger logger = LoggerFactory.getLogger(DistributedTaskSchedulerServerListener.class);
	
	private String serverName;
	
	@Override
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * 方法最后加入了死循环, 使获得leader后一直占有leader角色, 除非发生连接丢失等异常
	 */
	@Override
	protected void doTakeLeaderShip(CuratorFramework client) throws Exception {
    	logger.info("{} 被选举为主节点", serverName);
	}

}
