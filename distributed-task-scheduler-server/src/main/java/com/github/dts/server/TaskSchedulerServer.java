package com.github.dts.server;

import com.github.dts.core.ScheduledTaskDefinition;

public interface TaskSchedulerServer {
	
	void add(ScheduledTaskDefinition task);
	
	void update(ScheduledTaskDefinition task);

	void delete(ScheduledTaskDefinition task);

}
