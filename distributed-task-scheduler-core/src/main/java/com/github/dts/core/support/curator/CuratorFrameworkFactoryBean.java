package com.github.dts.core.support.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 用于构造CuratorFramework对象的FactoryBean
 * 
 * @see org.springframework.beans.factory.FactoryBean<T>
 * @see com.github.dts.core.support.curator.CuratorRetryPolicy
 * @author wh
 * @since 0.0.1
 */
public class CuratorFrameworkFactoryBean implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(CuratorFrameworkFactoryBean.class);

	private String connectionString;
	private Integer connectionTimeout;
	private Integer sessionTimeout;

	private String retryPolicyType;
	private Integer retryPolicyBaseSleepTime;
	private Integer retryPolicyMaxElapsedTime;
	private Integer retryPolicyMaxRetries;
	private Integer retryPolicyMaxSleepTime;
	private Integer retryPolicySleepBetweenRetries;

	private CuratorFramework curatorFramework;

	@Override
	public void afterPropertiesSet() throws Exception {
		buildClient();
		validateClient();
	}

	@Override
	public CuratorFramework getObject() throws Exception {
		return curatorFramework;
	}

	@Override
	public Class<?> getObjectType() {
		return CuratorFramework.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	protected void buildClient() throws Exception {
		Assert.notNull(connectionString,
				"[Assertion failed] connectionString can not be null");

		final Builder builder = CuratorFrameworkFactory.builder();

		if (StringUtils.hasText(connectionString)) {
			builder.connectString(connectionString);
		}

		if (connectionTimeout != null) {
			builder.connectionTimeoutMs(connectionTimeout);
		}

		if (sessionTimeout != null) {
			builder.sessionTimeoutMs(sessionTimeout);
		}

		// TODO 如需更多配置项，后续可添加。目前先支持最基础的几个属性。

		builder.retryPolicy(createRetryPolicy());

		curatorFramework = builder.build();
	}

	protected void validateClient() throws Exception {
		Assert.notNull(curatorFramework,
				"Attempt to validate Curator client before creating the client.");

		curatorFramework.start();
		logger.trace("Waiting for curator to create a connection");
		curatorFramework.blockUntilConnected();
		curatorFramework.checkExists().forPath("/");
		logger.trace("Curator has successfully verified the client");
	}

	protected RetryPolicy createRetryPolicy() {
		RetryPolicy retryPolicy = null;

		final CuratorRetryPolicy curatorRetryPolicy = CuratorRetryPolicy
				.findByType(retryPolicyType);

		Assert.notNull(curatorRetryPolicy, "[Assertion failed] retry policy '"
				+ retryPolicyType + "' is invalid/unknown.");

		switch (curatorRetryPolicy) {
		case BOUNDED_EXPONENTIAL_BACKOFF:
			retryPolicy = new BoundedExponentialBackoffRetry(
					retryPolicyBaseSleepTime, retryPolicyMaxSleepTime,
					retryPolicyMaxRetries);
			break;
		case EXPONENTIAL_BACKOFF:
			retryPolicy = new ExponentialBackoffRetry(retryPolicyBaseSleepTime,
					retryPolicyMaxRetries, retryPolicyMaxSleepTime);
			break;
		case RETRY_N_TIMES:
			retryPolicy = new RetryNTimes(retryPolicyMaxRetries,
					retryPolicySleepBetweenRetries);
			break;
		case RETRY_ONE_TIME:
			retryPolicy = new RetryOneTime(retryPolicySleepBetweenRetries);
			break;
		case RETRY_UNTIL_ELAPSED:
			retryPolicy = new RetryUntilElapsed(retryPolicyMaxElapsedTime,
					retryPolicySleepBetweenRetries);
			break;
		}

		return retryPolicy;
	}

	@Override
	public void destroy() throws Exception {
		try {
			logger.info("Closing Curator client");
			if (curatorFramework != null) {
				curatorFramework.close();
			}
		} catch (final Exception e) {
			logger.error("Error closing Curator client: ", e);
		}
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(final Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(final Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getRetryPolicyType() {
		return retryPolicyType;
	}

	public void setRetryPolicyType(final String retryPolicyType) {
		this.retryPolicyType = retryPolicyType;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(final String connectionString) {
		this.connectionString = connectionString;
	}

	public Integer getRetryPolicyBaseSleepTime() {
		return retryPolicyBaseSleepTime;
	}

	public void setRetryPolicyBaseSleepTime(
			final Integer retryPolicyBaseSleepTime) {
		this.retryPolicyBaseSleepTime = retryPolicyBaseSleepTime;
	}

	public Integer getRetryPolicyMaxElapsedTime() {
		return retryPolicyMaxElapsedTime;
	}

	public void setRetryPolicyMaxElapsedTime(
			final Integer retryPolicyMaxElapsedTime) {
		this.retryPolicyMaxElapsedTime = retryPolicyMaxElapsedTime;
	}

	public Integer getRetryPolicyMaxRetries() {
		return retryPolicyMaxRetries;
	}

	public void setRetryPolicyMaxRetries(final Integer retryPolicyMaxRetries) {
		this.retryPolicyMaxRetries = retryPolicyMaxRetries;
	}

	public Integer getRetryPolicyMaxSleepTime() {
		return retryPolicyMaxSleepTime;
	}

	public void setRetryPolicyMaxSleepTime(final Integer retryPolicyMaxSleepTime) {
		this.retryPolicyMaxSleepTime = retryPolicyMaxSleepTime;
	}

	public Integer getRetryPolicySleepBetweenRetries() {
		return retryPolicySleepBetweenRetries;
	}

	public void setRetryPolicySleepBetweenRetries(
			final Integer retryPolicySleepBetweenRetries) {
		this.retryPolicySleepBetweenRetries = retryPolicySleepBetweenRetries;
	}
}
