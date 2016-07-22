package com.github.dts.server;

import org.springframework.beans.factory.Aware;

/**
 * 表示需要有一个名字
 * 
 * @author wh
 * @since 0.0.2
 */
public interface ServerNameAware extends Aware {

	void setServerName(String serverName);
}
