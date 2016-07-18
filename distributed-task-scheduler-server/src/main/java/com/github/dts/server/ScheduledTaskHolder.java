package com.github.dts.server;

import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务持有者。可对定时任务提供添加、获取、删除功能
 * 
 * @author wh
 * @since 0.0.2
 */
public interface ScheduledTaskHolder {

	void add(String taskId, ScheduledFuture<?> scheduledTask);
	
	ScheduledFuture<?> get(String taskId);
	
	void remove(String taskId);
}
