package com.github.dts.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.github.dts.server.executor.TaskSchedulerExecutor;

/**
 * 分布式任务调度器服务. 为子类准备好各种依赖的对象
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class AbstractTaskSchedulerServer implements TaskSchedulerServer, 
	InitializingBean {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private TaskSchedulerExecutor taskSchedulerExecutor;

	public TaskSchedulerExecutor getTaskSchedulerExecutor() {
		return taskSchedulerExecutor;
	}

	public void setTaskSchedulerExecutor(TaskSchedulerExecutor taskSchedulerExecutor) {
		this.taskSchedulerExecutor = taskSchedulerExecutor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(taskSchedulerExecutor);
	}

}
