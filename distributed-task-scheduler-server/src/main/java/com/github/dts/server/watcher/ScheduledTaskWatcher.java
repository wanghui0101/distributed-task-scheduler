package com.github.dts.server.watcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.core.converter.Converter;
import com.github.dts.core.support.curator.CuratorOperations;
import com.github.dts.server.TaskSchedulerServer;

/**
 * 定时任务监视服务, 用于监视zookeeper上所有的定时任务的节点状态, 以触发相应的事件
 * 
 * @author wh
 * @lastModified 2016-6-15 10:55:39
 */
public class ScheduledTaskWatcher implements InitializingBean, DisposableBean {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskWatcher.class);
	
	private CuratorOperations curatorOperations;
	
	private TaskSchedulerServer taskSchedulerServer;

	private Converter<ScheduledTaskDefinition, byte[]> converter;
	
	private String scheduledTaskDefinitionsParentPath;
	
	private PathChildrenCache scheduledTaskDefinitionsParentPathCache;

	public void setCuratorOperations(CuratorOperations curatorOperations) {
		this.curatorOperations = curatorOperations;
	}

	public void setTaskSchedulerServer(TaskSchedulerServer taskSchedulerServer) {
		this.taskSchedulerServer = taskSchedulerServer;
	}

	public void setConverter(Converter<ScheduledTaskDefinition, byte[]> converter) {
		this.converter = converter;
	}
	
	public void setScheduledTaskDefinitionsParentPath(String scheduledTaskDefinitionsParentPath) {
		this.scheduledTaskDefinitionsParentPath = scheduledTaskDefinitionsParentPath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		curatorOperations.createPathIfAbsent(scheduledTaskDefinitionsParentPath);
		scheduledTaskDefinitionsParentPathCache = new PathChildrenCache(curatorOperations.getClient(), scheduledTaskDefinitionsParentPath, true);
		scheduledTaskDefinitionsParentPathCache.start(StartMode.POST_INITIALIZED_EVENT);
		scheduledTaskDefinitionsParentPathCache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
					case CHILD_ADDED: {
						logger.info("新增定时任务 - {}", event.getData().getPath());
						ScheduledTaskDefinition task = converter.to(event.getData().getData());
						taskSchedulerServer.add(task);
					} break;
					case CHILD_UPDATED: {
						logger.info("修改定时任务 - {}", event.getData().getPath());
						ScheduledTaskDefinition task = converter.to(event.getData().getData());
						taskSchedulerServer.update(task);
					} break;
					case CHILD_REMOVED: {
						logger.info("删除定时任务 - {}", event.getData().getPath());
						ScheduledTaskDefinition task = converter.to(event.getData().getData());
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
						logger.info("{} 子节点初始化完毕", scheduledTaskDefinitionsParentPath);
						break;
					default:
						break;
				}
			}
		});
	}

	@Override
	public void destroy() throws Exception {
		scheduledTaskDefinitionsParentPathCache.close();
	}
	
}
