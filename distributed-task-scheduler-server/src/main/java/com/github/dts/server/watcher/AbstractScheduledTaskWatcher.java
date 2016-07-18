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
import org.springframework.util.Assert;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.core.converter.Converter;
import com.github.dts.core.converter.ScheduledTaskDefinitionConverter;
import com.github.dts.core.support.curator.CuratorOperations;
import com.github.dts.server.TaskSchedulerServer;

/**
 * 实现监视者的启动和销毁
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class AbstractScheduledTaskWatcher implements ScheduledTaskWatcher, 
	InitializingBean, DisposableBean {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private CuratorOperations curatorOperations;
	
	private TaskSchedulerServer taskSchedulerServer;

	private Converter<ScheduledTaskDefinition, byte[]> converter = new ScheduledTaskDefinitionConverter();
	
	private String scheduledTaskDefinitionsParentPath;
	
	private PathChildrenCache scheduledTaskDefinitionsParentPathCache;

	public void setCuratorOperations(CuratorOperations curatorOperations) {
		this.curatorOperations = curatorOperations;
	}
	
	protected CuratorOperations getCuratorOperations() {
		return curatorOperations;
	}

	public void setTaskSchedulerServer(TaskSchedulerServer taskSchedulerServer) {
		this.taskSchedulerServer = taskSchedulerServer;
	}

	protected TaskSchedulerServer getTaskSchedulerServer() {
		return taskSchedulerServer;
	}

	protected PathChildrenCache getScheduledTaskDefinitionsParentPathCache() {
		return scheduledTaskDefinitionsParentPathCache;
	}

	protected Converter<ScheduledTaskDefinition, byte[]> getConverter() {
		return converter;
	}

	public void setConverter(Converter<ScheduledTaskDefinition, byte[]> converter) {
		this.converter = converter;
	}

	public void setScheduledTaskDefinitionsParentPath(String scheduledTaskDefinitionsParentPath) {
		this.scheduledTaskDefinitionsParentPath = scheduledTaskDefinitionsParentPath;
	}
	
	protected String getScheduledTaskDefinitionsParentPath() {
		return scheduledTaskDefinitionsParentPath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(curatorOperations);
		Assert.notNull(scheduledTaskDefinitionsParentPath);
		
		curatorOperations.createPathIfAbsent(scheduledTaskDefinitionsParentPath);
		scheduledTaskDefinitionsParentPathCache = new PathChildrenCache(curatorOperations.getClient(), 
				scheduledTaskDefinitionsParentPath, true);
		start();
	}

	@Override
	public void start() throws Exception {
		getScheduledTaskDefinitionsParentPathCache().start(getStartMode());
		getScheduledTaskDefinitionsParentPathCache().getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				onPathChildrenCacheEvent(client, event);
			}
		});
	}
	
	protected abstract void onPathChildrenCacheEvent(CuratorFramework client, PathChildrenCacheEvent event)
			throws Exception;

	protected StartMode getStartMode() {
		return StartMode.NORMAL;
	}

	@Override
	public void destroy() throws Exception {
		Assert.notNull(scheduledTaskDefinitionsParentPath);
		shutdown();
	}

	@Override
	public void shutdown() throws Exception {
		scheduledTaskDefinitionsParentPathCache.close();
	}
	
}