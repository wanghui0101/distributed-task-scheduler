package com.github.dts.core.support.curator;

/**
 * Curator运行时异常
 * 
 * @author wh
 * @lastModified 2016-4-18 11:38:43
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
