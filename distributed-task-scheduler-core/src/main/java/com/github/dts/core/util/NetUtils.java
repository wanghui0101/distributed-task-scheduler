package com.github.dts.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.util.ReflectionUtils;

/**
 * 网络工具类
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class NetUtils {
	
	private static String LOCAL_IP = null;
	
	/**
	 * 获取本地IP地址
	 * 
	 * <p>
	 * 如果返回127.0.0.1，请检查hosts配置文件
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			if (LOCAL_IP == null) {
				LOCAL_IP = InetAddress.getLocalHost().getHostAddress();
			}
			return LOCAL_IP;
		} catch (UnknownHostException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		return null;
	}
	
}
