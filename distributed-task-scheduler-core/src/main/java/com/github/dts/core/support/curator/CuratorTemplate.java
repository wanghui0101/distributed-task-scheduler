package com.github.dts.core.support.curator;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * Curator模板，主要实现在CuratorOperations中定义的方法
 * 
 * @author wh
 * @since 0.0.1
 */
public class CuratorTemplate extends CuratorSupport implements CuratorOperations {
	
	private CuratorFramework curatorFramework;
	
	public CuratorTemplate(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}
	
	@Override
	public CuratorFramework getClient() {
		return curatorFramework;
	}
	
	@Override
	public <T> T execute(CuratorCallback<T> callback) {
		return doExecute(callback);
	}
	
	private <T> T doExecute(CuratorCallback<T> callback) throws CuratorException {
		try {
			return callback.doInCurator(curatorFramework);
		} catch (Exception e) {
			throw convertToCuratorException(e);
		}
	}
	
	@Override
	public List<String> getChildren(final String path) {
		return execute(new CuratorCallback<List<String>>() {

			@Override
			public List<String> doInCurator(CuratorFramework client) throws Exception {
				return client.getChildren().forPath(path);
			}
		});
	}

	@Override
	public void create(final String path, final byte[] data) {
		execute(new CuratorCallback<Void>() {

			@Override
			public Void doInCurator(CuratorFramework client) throws Exception {
				client.create().creatingParentsIfNeeded().forPath(path, data);
				return null;
			}
		});
	}

	@Override
	public byte[] getData(final String path) {
		return execute(new CuratorCallback<byte[]>() {

			@Override
			public byte[] doInCurator(CuratorFramework client) throws Exception {
				return client.getData().forPath(path);
			}
			
		});
	}

	@Override
	public void setData(final String path, final byte[] data) {
		execute(new CuratorCallback<Void>() {

			@Override
			public Void doInCurator(CuratorFramework client) throws Exception {
				client.setData().forPath(path, data);
				return null;
			}
		});
	}
	
	@Override
	public void delete(final String path) {
		execute(new CuratorCallback<Void>() {

			@Override
			public Void doInCurator(CuratorFramework client) throws Exception {
				client.delete().deletingChildrenIfNeeded().forPath(path);
				return null;
			}
		});
	}

	@Override
	public boolean checkPathExists(final String path) {
		return execute(new CuratorCallback<Boolean>() {

			@Override
			public Boolean doInCurator(CuratorFramework client) throws Exception {
				Stat stat = client.checkExists().forPath(path);
				return stat != null;
			}
		});
	}

	@Override
	public void createPathIfAbsent(String path) {
		if (!checkPathExists(path)) {
			create(path, new byte[0]);
		}
	}

}
