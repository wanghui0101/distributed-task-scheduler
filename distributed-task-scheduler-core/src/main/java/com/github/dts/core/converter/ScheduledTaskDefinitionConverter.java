package com.github.dts.core.converter;

import com.github.dts.core.ScheduledTaskDefinition;
import com.github.dts.core.support.jackson.JsonMapper;

/**
 * 定时任务转换器, 由于定时任务存储在ZooKeeper的节点上, 因此需要转换为byte[]
 * 
 * @author wh
 * @since 0.0.1
 */
public class ScheduledTaskDefinitionConverter implements Converter<byte[], ScheduledTaskDefinition> {

	@Override
	public ScheduledTaskDefinition from(byte[] target) {
		return new JsonMapper().fromJson(new String(target), ScheduledTaskDefinition.class);
	}

	@Override
	public byte[] to(ScheduledTaskDefinition scheduledTaskDefinition) {
		return new JsonMapper().toJson(scheduledTaskDefinition).getBytes();
	}

}
