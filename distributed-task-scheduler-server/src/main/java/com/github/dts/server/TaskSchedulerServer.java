package com.github.dts.server;

import com.github.dts.core.ScheduledTaskDefinition;

/**
 * 任务调度服务。可以添加、修改、删除任务定义
 * 
 * @author wh
 * @since 0.0.1
 */
public interface TaskSchedulerServer {
	
	void add(ScheduledTaskDefinition task);
	
	void update(ScheduledTaskDefinition task);

	void delete(ScheduledTaskDefinition task);

}
