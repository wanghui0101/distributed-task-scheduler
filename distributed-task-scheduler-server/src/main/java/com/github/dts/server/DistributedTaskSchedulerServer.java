package com.github.dts.server;

import com.github.dts.core.ScheduledTaskDefinition;

/**
 * 分布式任务调度服务，提供对定时任务的动态添加、修改、删除等功能
 * 
 * @author wh
 * @since 0.0.1
 */
public class DistributedTaskSchedulerServer extends AbstractTaskSchedulerServer {
	
	@Override
	public void add(ScheduledTaskDefinition task) {
		if (task.isRunning()) {
			getTaskSchedulerExecutor().schedule(task);
		}
	}
	
	@Override
	public void update(ScheduledTaskDefinition task) {
		delete(task);
		add(task);
	}

	@Override
	public void delete(ScheduledTaskDefinition task) {
		getTaskSchedulerExecutor().cancel(task);
	}

}
