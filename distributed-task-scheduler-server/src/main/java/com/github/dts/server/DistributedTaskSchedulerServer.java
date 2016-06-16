package com.github.dts.server;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.MethodInvoker;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.server.listener.Listenable;
import com.github.dts.server.listener.TaskSchedulerServerListener;
import com.google.common.collect.Maps;

/**
 * 分布式任务调度器服务. 由监听器监听是否为主节点, 只有主节点才能执行定时任务
 * 
 * @author wh
 * @lastModified 2016-6-15 10:38:02
 */
public class DistributedTaskSchedulerServer implements TaskSchedulerServer, Listenable, ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(DistributedTaskSchedulerServer.class);
	
	private Map<String, ScheduledFuture<?>> runningTasks = Maps.newConcurrentMap(); // 运行中的任务
	
	private ApplicationContext applicationContext;
	
	private TaskScheduler taskScheduler;
	
	private TaskSchedulerServerListener taskSchedulerServerListener;
	
	public DistributedTaskSchedulerServer(TaskSchedulerServerListener taskSchedulerServerListener) {
		this.taskSchedulerServerListener = taskSchedulerServerListener;
	}
	
	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public boolean isLeader() {
		return taskSchedulerServerListener.isLeader();
	}

	@Override
	public void add(ScheduledTaskDefinition task) {
		if (task.isRunning()) {
			ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(createTask(task), new CronTrigger(task.getCronExpression()));
			runningTasks.put(task.getId(), scheduledFuture);
		}
	}
	
	protected Runnable createTask(final ScheduledTaskDefinition task) {
		final MethodInvoker methodInvoker = new MethodInvoker();
		final Object bean = getBean(task.getBeanName());
		final String method = task.getMethodName();
		logger.info("创建定时任务 - {}.{}()", bean.getClass().getName(), method);
		
		try {
			methodInvoker.setTargetObject(bean);
			methodInvoker.setTargetMethod(method);
			methodInvoker.prepare();
		} catch (ClassNotFoundException e) {
			logger.error("创建定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), method, e.getMessage());
		} catch (NoSuchMethodException e) {
			logger.error("创建定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), method, e.getMessage());
		}
		
		return new Runnable() {

			@Override
			public void run() {
				try {
					if (isLeader()) {
						logger.debug("定时任务 {}.{}() 将在主节点 - {} 上执行", bean.getClass().getName(), method, 
								taskSchedulerServerListener.getName());
						methodInvoker.invoke();
					} else {
						logger.debug("定时任务 {}.{}() 不会在从节点 - {} 上执行", bean.getClass().getName(), method, 
								taskSchedulerServerListener.getName());
					}
				} catch (InvocationTargetException e) {
					logger.error("运行定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), method, e.getMessage());
				} catch (IllegalAccessException e) {
					logger.error("运行定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), method, e.getMessage());
				}
			} 	
			
		};
	}
	
	private Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
	
	@Override
	public void update(ScheduledTaskDefinition task) {
		delete(task);
		add(task);
	}

	@Override
	public void delete(ScheduledTaskDefinition task) {
		ScheduledFuture<?> scheduledFuture = runningTasks.get(task.getId());
		if (scheduledFuture != null) {
			final Object bean = getBean(task.getBeanName());
			final String method = task.getMethodName();
			logger.info("停止定时任务 - {}.{}()", bean.getClass().getName(), method);
			scheduledFuture.cancel(true);
			runningTasks.remove(task.getId());
		}
	}

}
