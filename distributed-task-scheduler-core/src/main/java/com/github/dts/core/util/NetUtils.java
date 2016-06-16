package com.github.dts.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.util.ReflectionUtils;

public abstract class NetUtils {
	
	public static String getLocalIpAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		return null;
	}
	
}
