package com.github.dts.server.executor;

import com.github.dts.core.ScheduledTaskDefinition;

/**
 * 任务调度执行器
 * 
 * @author wh
 * @since 0.0.2
 */
public interface TaskSchedulerExecutor {

	/**
	 * 执行任务调度
	 * 
	 * @param task
	 */
	void schedule(ScheduledTaskDefinition task);
	
	/**
	 * 取消任务调度
	 * 
	 * @param task
	 */
	void cancel(ScheduledTaskDefinition task);
}
