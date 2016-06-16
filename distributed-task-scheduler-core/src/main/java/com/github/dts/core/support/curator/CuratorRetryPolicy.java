package com.github.dts.core.support.curator;

/**
 * 对Curator重试策略的简单封装
 * 
 * @author wh
 * @lastModified 2016-6-15 10:12:20
 */
public enum CuratorRetryPolicy {

	BOUNDED_EXPONENTIAL_BACKOFF("bounded-exponential-backoff"), 
	EXPONENTIAL_BACKOFF("exponential-backoff"), 
	RETRY_N_TIMES("retry-n-times"), 
	RETRY_ONE_TIME("retry-one-time"), 
	RETRY_UNTIL_ELAPSED("retry-until-elapsed");

	private final String type;

	private CuratorRetryPolicy(final String type) {
		this.type = type;
	}

	public static CuratorRetryPolicy findByType(final String type) {
		CuratorRetryPolicy retryPolicy = null;

		for (final CuratorRetryPolicy currentRetryPolicy : values()) {
			if (currentRetryPolicy.type.equals(type)) {
				retryPolicy = currentRetryPolicy;
				break;
			}
		}

		return retryPolicy;
	}

	@Override
	public String toString() {
		return type;
	}

}
