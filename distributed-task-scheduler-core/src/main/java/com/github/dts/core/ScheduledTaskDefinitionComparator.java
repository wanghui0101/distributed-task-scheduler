package com.github.dts.core;

import java.util.Comparator;

/**
 * ScheduledTaskDefinition的比较器
 * 
 * @author wh
 * @since 0.0.2
 */
public enum ScheduledTaskDefinitionComparator implements 
	Comparator<ScheduledTaskDefinition> {

	INSTANCE;
	
	@Override
	public int compare(ScheduledTaskDefinition std1, ScheduledTaskDefinition std2) {
		return std1.getName().compareTo(std2.getName());
	}

}
