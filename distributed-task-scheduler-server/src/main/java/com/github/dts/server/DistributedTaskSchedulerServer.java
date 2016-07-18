package com.github.dts.server;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.MethodInvoker;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.server.listener.TaskSchedulerServerListener;

/**
 * 分布式任务调度服务，提供对定时任务的动态添加、修改、删除等功能
 * 
 * @author wh
 * @since 0.0.1
 */
public class DistributedTaskSchedulerServer extends AbstractDistributedTaskSchedulerServer {
	
	private ScheduledTaskHolder scheduledTaskHolder;
	
	public DistributedTaskSchedulerServer(TaskSchedulerServerListener taskSchedulerServerListener) {
		super(taskSchedulerServerListener);
		setScheduledTaskHolder(new RunningScheduledTaskHolder());
	}
	
	public void setScheduledTaskHolder(ScheduledTaskHolder scheduledTaskHolder) {
		this.scheduledTaskHolder = scheduledTaskHolder;
	}

	@Override
	public void add(ScheduledTaskDefinition task) {
		if (task.isRunning()) {
			ScheduledFuture<?> scheduledFuture = getTaskScheduler().schedule(createTask(task), 
					new CronTrigger(task.getCronExpression()));
			scheduledTaskHolder.add(task.getId(), scheduledFuture);
		}
	}
	
	protected Runnable createTask(final ScheduledTaskDefinition task) {
		MethodInvoker methodInvoker = new MethodInvoker();
		Object bean = getBean(task.getBeanName());
		String method = task.getMethodName();
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
						logger.debug("定时任务 {}.{}() 将在主节点 - {} 上执行", bean.getClass().getName(), method, getName());
					}
					methodInvoker.invoke();
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("定时任务 {}.{}() 不会在从节点 - {} 上执行", bean.getClass().getName(), method, getName());
					}
				}
			} catch (InvocationTargetException e) {
				logger.error("运行定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), method, e.getMessage());
			} catch (IllegalAccessException e) {
				logger.error("运行定时任务 - {}.{}() 时, 发生异常 - {}", bean.getClass().getName(), method, e.getMessage());
			}
		}
		
	}
	
	@Override
	public void update(ScheduledTaskDefinition task) {
		delete(task);
		add(task);
	}

	@Override
	public void delete(ScheduledTaskDefinition task) {
		ScheduledFuture<?> scheduledFuture = scheduledTaskHolder.get(task.getId());
		if (scheduledFuture != null) {
			Object bean = getBean(task.getBeanName());
			String method = task.getMethodName();
			logger.info("停止定时任务 - {}.{}()", bean.getClass().getName(), method);
			scheduledFuture.cancel(true);
			scheduledTaskHolder.remove(task.getId());
		}
	}

}
