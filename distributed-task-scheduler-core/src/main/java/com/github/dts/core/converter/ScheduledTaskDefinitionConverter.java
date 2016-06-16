package com.github.dts.core.converter;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.core.support.jackson.JsonMapper;

/**
 * 定时任务转换器, 由于定时任务存储在ZooKeeper的节点上, 因为需要转换为byte[]
 * 
 * @author wh
 * @lastModified 2016-6-15 10:09:01
 */
public class ScheduledTaskDefinitionConverter implements Converter<ScheduledTaskDefinition, byte[]> {

	@Override
	public byte[] from(ScheduledTaskDefinition source) {
		return new JsonMapper().toJson(source).getBytes();
	}

	@Override
	public ScheduledTaskDefinition to(byte[] target) {
		return new JsonMapper().fromJson(new String(target), ScheduledTaskDefinition.class);
	}

}
