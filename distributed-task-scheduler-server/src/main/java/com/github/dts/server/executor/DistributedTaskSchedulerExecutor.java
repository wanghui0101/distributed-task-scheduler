package com.github.dts.server.executor;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.MethodInvoker;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.server.ServerNameAware;

/**
 * 实现具体的任务调度功能
 * 
 * @author wh
 * @since 0.0.2
 */
public class DistributedTaskSchedulerExecutor extends AbstractTaskSchedulerExecutor implements 
	ServerNameAware {
	
	private static final Logger logger = LoggerFactory.getLogger(DistributedTaskSchedulerExecutor.class);
	
	private String serverName;
	
	@Override
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	@Override
	public void schedule(ScheduledTaskDefinition task) {
		ScheduledFuture<?> scheduledFuture = getTaskScheduler().schedule(createTask(task), 
				new CronTrigger(task.getCronExpression()));
		getScheduledTaskHolder().add(task.getId(), scheduledFuture);
	}
	
	protected Runnable createTask(ScheduledTaskDefinition task) {
		MethodInvoker methodInvoker = new MethodInvoker();
		Object bean = getBean(task.getBeanName());
		String method = task.getMethodName();
		logger.info("创建定时任务 - {}.{}()", bean.getClass().getName(), method);
		
		try {
			methodInvoker.setTargetObject(bean);
			methodInvoker.setTargetMethod(method);
			methodInvoker.prepare();
		} catch (ClassNotFoundException e) {
			logger.error("创建定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), 
					method, e.getMessage());
		} catch (NoSuchMethodException e) {
			logger.error("创建定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), 
					method, e.getMessage());
		}
		
		return new RunnableTask(methodInvoker, bean, method);
	}
	
	private class RunnableTask implements Runnable {
		
		private MethodInvoker methodInvoker;
		private Object bean;
		private String method;
		
		RunnableTask(MethodInvoker methodInvoker, Object bean, String method) {
			this.methodInvoker = methodInvoker;
			this.bean = bean;
			this.method = method;
		}
		
		@Override
		public void run() {
			try {
				if (isLeader()) { // 只有主节点才执行
					if (logger.isDebugEnabled()) {
						logger.debug("定时任务 {}.{}() 将在主节点 - {} 上执行", 
								bean.getClass().getName(), method, serverName);
					}
					methodInvoker.invoke();
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("定时任务 {}.{}() 不会在从节点 - {} 上执行", 
								bean.getClass().getName(), method, serverName);
					}
				}
			} catch (InvocationTargetException e) {
				logger.error("运行定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), 
						method, e.getMessage());
			} catch (IllegalAccessException e) {
				logger.error("运行定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), 
						method, e.getMessage());
			}
		}
		
	}

	@Override
	public void cancel(ScheduledTaskDefinition task) {
		ScheduledFuture<?> scheduledFuture = getScheduledTaskHolder().get(task.getId());
		if (scheduledFuture != null) {
			Object bean = getBean(task.getBeanName());
			String method = task.getMethodName();
			logger.info("停止定时任务 - {}.{}()", bean.getClass().getName(), method);
			scheduledFuture.cancel(true);
			getScheduledTaskHolder().remove(task.getId());
		}
	}

}
