package com.github.dts.server.listener;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;

/**
 * 分布式任务调度器服务器监听. 任务调度器服务可部署多个, 但只有一个可以被选为主节点, 选举结果由此类监听
 * 
 * @author wh
 * @since 0.0.2
 */
public class DistributedTaskSchedulerServerListener extends AbstractTaskSchedulerServerListener {

	/**
	 * 方法最后加入了死循环, 使获得leader后一直占有leader角色, 除非发生连接丢失等异常
	 */
	@Override
	protected void doTakeLeaderShip(CuratorFramework client) throws Exception {
    	logger.info("{} 被选举为主节点", getServerName());
    	while (true) {
            TimeUnit.HOURS.sleep(12);
        }
	}

}
