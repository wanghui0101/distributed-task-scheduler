package com.github.dts.console.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.core.ScheduledTaskDefinition.Status;
import com.github.dts.core.converter.Converter;
import com.github.dts.core.support.curator.CuratorOperations;
import com.google.common.collect.Lists;

public class ConsoleServiceImpl implements ConsoleService, InitializingBean {
	
	private CuratorOperations curatorOperations;
	
	private String scheduledTaskDefinitionsParentPath;
	
	private Converter<ScheduledTaskDefinition, byte[]> converter;
	
	private String leaderListenerPath;

	public void setCuratorOperations(CuratorOperations curatorOperations) {
		this.curatorOperations = curatorOperations;
	}
	
	public void setScheduledTaskDefinitionsParentPath(String scheduledTaskDefinitionsParentPath) {
		this.scheduledTaskDefinitionsParentPath = scheduledTaskDefinitionsParentPath;
	}
	
	public void setConverter(Converter<ScheduledTaskDefinition, byte[]> converter) {
		this.converter = converter;
	}
	
	public void setLeaderListenerPath(String leaderListenerPath) {
		this.leaderListenerPath = leaderListenerPath;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		curatorOperations.createPathIfAbsent(scheduledTaskDefinitionsParentPath);
	}
	
	@Override
	public List<ScheduledTaskDefinition> findAll() {
		List<String> scheduledTaskDefinitionsPath = curatorOperations.getChildren(scheduledTaskDefinitionsParentPath);
		List<ScheduledTaskDefinition> scheduledTaskDefinitions = Lists.newArrayListWithExpectedSize(scheduledTaskDefinitionsPath.size());
		for (String scheduledTaskDefinitionPath : scheduledTaskDefinitionsPath) {
			String scheduledTaskDefinitionAbsolutePath = scheduledTaskDefinitionsParentPath + "/" + scheduledTaskDefinitionPath;
			byte[] scheduledTaskDefinitionData = curatorOperations.getData(scheduledTaskDefinitionAbsolutePath);
			ScheduledTaskDefinition scheduledTaskDefinition = converter.to(scheduledTaskDefinitionData);
			scheduledTaskDefinitions.add(scheduledTaskDefinition);
		}
		Collections.sort(scheduledTaskDefinitions);
		return scheduledTaskDefinitions;
	}

	@Override
	public ScheduledTaskDefinition findOne(String id) {
		String path = scheduledTaskDefinitionsParentPath + "/" + id;
		byte[] data = curatorOperations.getData(path);
		return converter.to(data);
	}

	@Override
	public void add(ScheduledTaskDefinition task) {
		task.setId(UUID.randomUUID().toString());
		task.setLastModified(new Date());
		task.setStatus(Status.STOPPED);
		byte[] data = converter.from(task);
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
		byte[] data = converter.from(task);
		String path = scheduledTaskDefinitionsParentPath + "/" + task.getId();
		curatorOperations.setData(path, data);
	}
	
	@Override
	public String leader() {
		List<String> children = curatorOperations.getChildren(leaderListenerPath);
		if (!CollectionUtils.isEmpty(children)) {
			for (String child : children) {
				if (child.contains("-lock-")) {
					String leaderLockPath = leaderListenerPath + "/" + child;
					return new String(curatorOperations.getData(leaderLockPath));
				}
			}
		}
		return "";
	}

}