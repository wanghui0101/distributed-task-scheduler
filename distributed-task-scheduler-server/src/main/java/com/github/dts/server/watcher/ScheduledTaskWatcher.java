package com.github.dts.server.watcher;

/**
 * 定时任务的在Zookeeper上的监视者，主要用于监控Zookeeper定时任务节点的变化
 * 
 * @author wh
 * @since 0.0.2
 */
public interface ScheduledTaskWatcher {

	void start() throws Exception;
	
	void shutdown() throws Exception;
	
}
