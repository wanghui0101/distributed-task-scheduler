package com.github.dts.core.support.curator;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;

public interface CuratorOperations {

	<T> T execute(CuratorCallback<T> callback);
	
	CuratorFramework getClient();
	
	boolean checkPathExists(String path);
	
	List<String> getChildren(String path);
	
	byte[] getData(String path);
	
	void setData(String path, byte[] data);
	
	void delete(String path);
	
	void create(String path, byte[] data);

	void createPathIfAbsent(String path);
	
}
