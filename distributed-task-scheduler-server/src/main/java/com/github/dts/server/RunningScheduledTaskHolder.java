package com.github.dts.server;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import com.google.common.collect.Maps;

/**
 * 当前正在运行的任务持有者
 * 
 * @author wh
 * @since 0.0.2
 */
public class RunningScheduledTaskHolder implements ScheduledTaskHolder {
	
	private Map<String, ScheduledFuture<?>> runningTasks = Maps.newConcurrentMap(); // 运行中的任务

	@Override
	public void add(String taskId, ScheduledFuture<?> scheduledTask) {
		runningTasks.put(taskId, scheduledTask);
	}
	
	@Override
	public ScheduledFuture<?> get(String taskId) {
		return runningTasks.get(taskId);
	}

	@Override
	public void remove(String taskId) {
		runningTasks.remove(taskId);
	}

}
