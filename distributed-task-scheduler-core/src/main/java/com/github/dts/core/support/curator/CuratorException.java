package com.github.dts.core.support.curator;

/**
 * Curator运行时异常
 * 
 * @author wh
 * @since 0.0.1
 */
@SuppressWarnings("serial")
public class CuratorException extends RuntimeException {

	public CuratorException(String message) {
		super(message);
	}

	public CuratorException(Throwable cause) {
		super(cause);
	}

	public CuratorException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
