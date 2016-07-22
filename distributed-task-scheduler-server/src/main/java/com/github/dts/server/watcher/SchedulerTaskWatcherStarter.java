package com.github.dts.server.watcher;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.github.dts.exception.DistributedTaskSchedulerException;

/**
 * 在Spring容器初始化完毕后，启动监听器
 * 
 * @author wh
 * @since 0.0.2
 */
public class SchedulerTaskWatcherStarter implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerTaskWatcherStarter.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ScheduledTaskWatcher watcher = event.getApplicationContext().getBean(ScheduledTaskWatcher.class);
		try {
			watcher.start();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new DistributedTaskSchedulerException(e);
		}
	}

}
