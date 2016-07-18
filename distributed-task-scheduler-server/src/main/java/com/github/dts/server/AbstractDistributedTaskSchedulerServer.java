package com.github.dts.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.scheduling.TaskScheduler;

import com.github.dts.server.listener.TaskSchedulerServerListener;

/**
 * 分布式任务调度器服务. 为子类准备好各种依赖的对象
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class AbstractDistributedTaskSchedulerServer extends ApplicationObjectSupport implements 
	TaskSchedulerServer {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private TaskScheduler taskScheduler;
	
	private TaskSchedulerServerListener taskSchedulerServerListener;
		
	public AbstractDistributedTaskSchedulerServer(TaskSchedulerServerListener taskSchedulerServerListener) {
		this.taskSchedulerServerListener = taskSchedulerServerListener;
	}
	
	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}
	
	protected TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	protected void setTaskSchedulerServerListener(TaskSchedulerServerListener taskSchedulerServerListener) {
		this.taskSchedulerServerListener = taskSchedulerServerListener;
	}

	public boolean isLeader() { // 委托给taskSchedulerServerListener
		return taskSchedulerServerListener.isLeader();
	}

	protected String getName() { // 委托给taskSchedulerServerListener
		return taskSchedulerServerListener.getName();
	}

	protected final Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}

}
