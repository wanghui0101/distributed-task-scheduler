package com.github.dts.server.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.core.converter.Converter;
import com.github.dts.server.TaskSchedulerServer;


/**
 * 定时任务监视服务, 用于监视zookeeper上所有的定时任务的节点状态, 以触发相应的事件
 * 
 * @author wh
 * @since 0.0.2
 */
public class DistributedScheduledTaskWatcher extends AbstractScheduledTaskWatcher {
	
	@Override
	protected StartMode getStartMode() {
		return StartMode.POST_INITIALIZED_EVENT;
	}

	@Override
	protected void onPathChildrenCacheEvent(CuratorFramework client, PathChildrenCacheEvent event) 
			throws Exception {
		Converter<byte[], ScheduledTaskDefinition> converter = getConverter();
		PathChildrenCache scheduledTaskDefinitionsParentPathCache = getScheduledTaskDefinitionsParentPathCache();
		TaskSchedulerServer taskSchedulerServer = getTaskSchedulerServer();
		
		switch (event.getType()) {
			case CHILD_ADDED: {
				logger.info("新增定时任务 - {}", event.getData().getPath());
				ScheduledTaskDefinition task = converter.from(event.getData().getData());
				taskSchedulerServer.add(task);
			} break;
			case CHILD_UPDATED: {
				logger.info("修改定时任务 - {}", event.getData().getPath());
				ScheduledTaskDefinition task = converter.from(event.getData().getData());
				taskSchedulerServer.update(task);
			} break;
			case CHILD_REMOVED: {
				logger.info("删除定时任务 - {}", event.getData().getPath());
				ScheduledTaskDefinition task = converter.from(event.getData().getData());
				taskSchedulerServer.delete(task);
			} break;
			case CONNECTION_LOST:
				logger.info("连接丢失");
				break;
			case CONNECTION_RECONNECTED:
				logger.info("重新连接");
				break;
			case CONNECTION_SUSPENDED:
				logger.info("连接挂起");
				break;
			case INITIALIZED:
				logger.info("{} 子节点初始化完毕", scheduledTaskDefinitionsParentPathCache);
				break;
			default:
				break;
		}
	}
	
}
