package com.github.dts.console.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.github.dts.console.locknode.DefaultLockNodeResolver;
import com.github.dts.console.locknode.LockNode;
import com.github.dts.console.locknode.LockNodeComparator;
import com.github.dts.console.locknode.LockNodeResolver;
import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.core.ScheduledTaskDefinition.Status;
import com.github.dts.core.ScheduledTaskDefinitionComparator;
import com.github.dts.core.converter.Converter;
import com.github.dts.core.converter.ScheduledTaskDefinitionConverter;
import com.github.dts.core.support.curator.CuratorOperations;
import com.github.dts.core.util.ComparatorUtils;
import com.google.common.collect.Lists;

/**
 * 控制台服务实现类
 * 
 * @author wh
 * @since 0.0.1
 */
public class ConsoleServiceImpl implements ConsoleService, InitializingBean {
	
	private CuratorOperations curatorOperations;
	
	private String scheduledTaskDefinitionsParentPath;
	
	private Converter<byte[], ScheduledTaskDefinition> converter;
	
	private String listenerPath;
	
	private LockNodeResolver lockNodeResolver;

	public void setCuratorOperations(CuratorOperations curatorOperations) {
		this.curatorOperations = curatorOperations;
	}
	
	public void setScheduledTaskDefinitionsParentPath(String scheduledTaskDefinitionsParentPath) {
		this.scheduledTaskDefinitionsParentPath = scheduledTaskDefinitionsParentPath;
	}
	
	public void setConverter(Converter<byte[], ScheduledTaskDefinition> converter) {
		this.converter = converter;
	}
	
	public void setListenerPath(String listenerPath) {
		this.listenerPath = listenerPath;
	}
	
	public void setLockNodeResolver(LockNodeResolver lockNodeResolver) {
		this.lockNodeResolver = lockNodeResolver;
	}
	
	public ConsoleServiceImpl() {
		converter = new ScheduledTaskDefinitionConverter();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(curatorOperations);
		Assert.notNull(scheduledTaskDefinitionsParentPath);
		Assert.notNull(listenerPath);
		
		if (lockNodeResolver == null) {
			lockNodeResolver = new DefaultLockNodeResolver(curatorOperations, listenerPath);
		}
		
		curatorOperations.createPathIfAbsent(scheduledTaskDefinitionsParentPath);
	}
	
	@Override
	public List<ScheduledTaskDefinition> findAll() {
		List<String> scheduledTaskDefinitionsPath = curatorOperations.getChildren(scheduledTaskDefinitionsParentPath);
		List<ScheduledTaskDefinition> scheduledTaskDefinitions = Lists.newArrayListWithExpectedSize(scheduledTaskDefinitionsPath.size());
		for (String scheduledTaskDefinitionPath : scheduledTaskDefinitionsPath) {
			String scheduledTaskDefinitionAbsolutePath = scheduledTaskDefinitionsParentPath + "/" + scheduledTaskDefinitionPath;
			byte[] scheduledTaskDefinitionData = curatorOperations.getData(scheduledTaskDefinitionAbsolutePath);
			ScheduledTaskDefinition scheduledTaskDefinition = converter.from(scheduledTaskDefinitionData);
			scheduledTaskDefinitions.add(scheduledTaskDefinition);
		}
		ComparatorUtils.sort(scheduledTaskDefinitions, ScheduledTaskDefinitionComparator.INSTANCE);
		return scheduledTaskDefinitions;
	}

	@Override
	public ScheduledTaskDefinition findOne(String id) {
		String path = scheduledTaskDefinitionsParentPath + "/" + id;
		byte[] data = curatorOperations.getData(path);
		return converter.from(data);
	}

	@Override
	public void add(ScheduledTaskDefinition task) {
		task.setId(UUID.randomUUID().toString());
		task.setLastModified(new Date());
		task.setStatus(Status.STOPPED);
		byte[] data = converter.to(task);
		String path = scheduledTaskDefinitionsParentPath + "/" + task.getId();
		curatorOperations.create(path, data);
	}

	@Override
	public void update(ScheduledTaskDefinition task) {
		task.setLastModified(new Date());
		task.setStatus(Status.STOPPED);
		save(task);
	}
	
	@Override
	public void delete(String id) {
		String path = scheduledTaskDefinitionsParentPath + "/" + id;
		curatorOperations.delete(path);
	}
	
	@Override
	public void run(String id) {
		ScheduledTaskDefinition task = findOne(id);
		task.setStatus(Status.RUNNING);
		task.setLastModified(new Date());
		save(task);
	}

	@Override
	public void stop(String id) {
		ScheduledTaskDefinition task = findOne(id);
		task.setStatus(Status.STOPPED);
		task.setLastModified(new Date());
		save(task);
	}
	
	private void save(ScheduledTaskDefinition task) {
		byte[] data = converter.to(task);
		String path = scheduledTaskDefinitionsParentPath + "/" + task.getId();
		curatorOperations.setData(path, data);
	}
	
	@Override
	public String[] nodes() {
		List<LockNode> lockNodes = getOrdredLockNodes();
		if (!CollectionUtils.isEmpty(lockNodes)) {
			List<String> nodes = new ArrayList<String>(lockNodes.size());
			for (LockNode lockNode : lockNodes) {
				nodes.add(lockNode.getIp());
			}
			return nodes.toArray(new String[nodes.size()]);
		}
		return null;
	}
	
	private List<LockNode> getOrdredLockNodes() {
		List<String> locks = curatorOperations.getChildren(listenerPath);
		if (!CollectionUtils.isEmpty(locks)) {
			List<LockNode> lockNodes = new ArrayList<LockNode>();
			for (String lock : locks) {
				if (lockNodeResolver.supports(lock)) {
					LockNode lockNode = lockNodeResolver.resolve(lock);
					lockNodes.add(lockNode);
				}
			}
			ComparatorUtils.sort(lockNodes, LockNodeComparator.INSTANCE);
			return lockNodes;
		}
		return null;
	}

}