package com.github.dts.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.github.dts.core.util.NetUtils;

/**
 * 向所有实现了ServerNameAware接口的类注入serverName
 * 
 * @author wh
 * @since 0.0.2
 */
public class ServerNameAwarePostProcessor implements BeanPostProcessor {
	
	private String serverName;
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	protected String getServerName() {
		return serverName;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof ServerNameAware) {
			((ServerNameAware) bean).setServerName(uniqueServerName());
		}
		return bean;
	}
	
	/**
	 * 每个服务都有唯一的名字
	 * 
	 * @param name
	 * @return
	 */
	protected String uniqueServerName() {
		return serverName + "@" + NetUtils.getLocalIpAddress(); // name@IP
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
