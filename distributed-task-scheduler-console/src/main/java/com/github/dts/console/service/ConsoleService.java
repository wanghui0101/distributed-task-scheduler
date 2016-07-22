package com.github.dts.console.service;

import java.util.List;

import com.github.dts.core.ScheduledTaskDefinition;

/**
 * 控制台服务接口
 * 
 * @author wh
 * @since 0.0.1
 */
public interface ConsoleService {
	
	/**
	 * 查询所有定时任务定时
	 * @return
	 */
	List<ScheduledTaskDefinition> findAll();

	/**
	 * 根据id获取一个定时任务
	 * @param id
	 * @return
	 */
	ScheduledTaskDefinition findOne(String id);
	
	/**
	 * 添加定时任务
	 * @param task
	 */
	void add(ScheduledTaskDefinition task);
	
	/**
	 * 修改定时任务
	 * @param task
	 */
	void update(ScheduledTaskDefinition task);
	
	/**
	 * 根据id删除一个定时任务
	 * @param id
	 */
	void delete(String id);

	/**
	 * 启动定时任务
	 * @param id
	 */
	void run(String id);

	/**
	 * 停止定时任务
	 * @param id
	 */
	void stop(String id);

	/**
	 * 获取定时任务服务所有节点，主节点在第1个
	 * @return
	 */
	String[] nodes();
	
}
