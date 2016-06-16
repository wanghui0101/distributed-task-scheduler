package com.github.dts.server.listener;

/**
 * 实现此接口的类表明是一个可监听的服务
 * 
 * @author wh
 * @lastModified 2016-6-15 10:52:56
 */
public interface Listenable {

	boolean isLeader();
}
