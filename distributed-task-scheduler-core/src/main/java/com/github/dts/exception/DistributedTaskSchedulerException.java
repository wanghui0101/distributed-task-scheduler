package com.github.dts.exception;

/**
 * Curator运行时异常
 * 
 * @author wh
 * @since 0.0.1
 */
@SuppressWarnings("serial")
public class DistributedTaskSchedulerException extends RuntimeException {

	public DistributedTaskSchedulerException(String message) {
		super(message);
	}

	public DistributedTaskSchedulerException(Throwable cause) {
		super(cause);
	}

	public DistributedTaskSchedulerException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
