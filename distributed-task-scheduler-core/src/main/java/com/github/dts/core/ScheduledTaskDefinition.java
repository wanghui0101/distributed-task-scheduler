package com.github.dts.core;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 定时任务定义. 实现Comparable接口用于排序
 * 
 * @author wh
 * @lastModified 2016-6-15 10:08:00
 */
public class ScheduledTaskDefinition implements Comparable<ScheduledTaskDefinition> {

	private String id;

	private String cronExpression;

	private String beanName;

	private String methodName;

	private Status status;

	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastModified;

	public enum Status {
		RUNNING, STOPPED;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@JsonIgnore
	public Boolean isRunning() {
		return Status.RUNNING.equals(this.getStatus());
	}
	
	@JsonIgnore
	public String getName() {
		return this.beanName + "." + this.methodName + "()";
	}

	@Override
	public int compareTo(ScheduledTaskDefinition that) {
		return this.getName().compareTo(that.getName());
	}

}
