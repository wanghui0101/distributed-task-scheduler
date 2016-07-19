package com.github.dts.server.executor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;

import com.github.dts.server.ServerNameAware;
import com.github.dts.server.listener.TaskSchedulerServerListener;

/**
 * 
 * 
 * @author wh
 * @since 0.0.2
 */
public abstract class AbstractTaskSchedulerExecutor extends ApplicationObjectSupport 
	implements TaskSchedulerExecutor, ServerNameAware, InitializingBean {
	
	private TaskScheduler taskScheduler;
	
	private ScheduledTaskHolder scheduledTaskHolder;
	
	private TaskSchedulerServerListener taskSchedulerServerListener;
	
	private String serverName;
	
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
		setTaskScheduler(taskScheduler);
		setTaskSchedulerServerListener(taskSchedulerServerListener);
		setScheduledTaskHolder(new RunningScheduledTaskHolder());
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(taskScheduler);
		Assert.notNull(taskSchedulerServerListener);
		
		if (scheduledTaskHolder == null) {
			setScheduledTaskHolder(new RunningScheduledTaskHolder());
		}
	}
	
	public boolean isLeader() { // 委托给taskSchedulerServerListener
		return taskSchedulerServerListener.isLeader();
	}
	
	@Override
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	protected String getServerName() {
		return serverName;
	}
	
	protected final Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}
}
