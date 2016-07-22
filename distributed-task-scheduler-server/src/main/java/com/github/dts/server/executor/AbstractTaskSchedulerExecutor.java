package com.github.dts.server.executor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;

import com.github.dts.server.listener.TaskSchedulerServerListener;

/**
 * 为子类准备好必要的工具类
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class AbstractTaskSchedulerExecutor extends ApplicationObjectSupport 
	implements TaskSchedulerExecutor, InitializingBean {
	
	private TaskScheduler taskScheduler;
	
	private ScheduledTaskHolder scheduledTaskHolder;
	
	private TaskSchedulerServerListener taskSchedulerServerListener;
	
	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}
	
	protected TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
	
	public ScheduledTaskHolder getScheduledTaskHolder() {
		return scheduledTaskHolder;
	}

	public void setScheduledTaskHolder(ScheduledTaskHolder scheduledTaskHolder) {
		this.scheduledTaskHolder = scheduledTaskHolder;
	}
	
	public void setTaskSchedulerServerListener(TaskSchedulerServerListener taskSchedulerServerListener) {
		this.taskSchedulerServerListener = taskSchedulerServerListener;
	}
	
	public TaskSchedulerServerListener getTaskSchedulerServerListener() {
		return taskSchedulerServerListener;
	}
	
	public AbstractTaskSchedulerExecutor() {
		super();
	}

	public AbstractTaskSchedulerExecutor(TaskScheduler taskScheduler, TaskSchedulerServerListener 
			taskSchedulerServerListener) {
		this.taskScheduler =  taskScheduler;
		this.taskSchedulerServerListener = taskSchedulerServerListener;
		this.scheduledTaskHolder = new RunningScheduledTaskHolder();
	}
	
	public AbstractTaskSchedulerExecutor(TaskScheduler taskScheduler, TaskSchedulerServerListener 
			taskSchedulerServerListener, ScheduledTaskHolder scheduledTaskHolder) {
		this.taskScheduler =  taskScheduler;
		this.taskSchedulerServerListener = taskSchedulerServerListener;
		this.scheduledTaskHolder = scheduledTaskHolder;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(taskScheduler);
		Assert.notNull(taskSchedulerServerListener);
		
		if (scheduledTaskHolder == null) {
			this.scheduledTaskHolder = new RunningScheduledTaskHolder();
		}
	}
	
	public boolean isLeader() { // 委托给taskSchedulerServerListener
		return taskSchedulerServerListener.isLeader();
	}
	
	protected final Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}
}
