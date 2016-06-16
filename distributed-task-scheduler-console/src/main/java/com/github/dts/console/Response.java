package com.github.dts.console;

/**
 * 响应体的通用封装类
 * 
 * @author Wh
 * @lastModified 2015-7-1 12:01:09
 */
public class Response {
	
	private Boolean success; // 是否成功
	private Object data; // 返回的数据
	private String msg; // 消息
	
	private Response(Boolean success, Object data, String msg) {
		this.success = success;
		this.data = data;
		this.msg = msg;
	}
	
	/**
	 * 只返回成功的布尔值
	 * @return
	 */
	public static Response success() {
		return new Response(true, null, null);
	}

	/**
	 * 返回成功的布尔值和消息
	 * @param msg
	 * @return
	 */
	public static Response success(String msg) {
		return new Response(true, null, msg);
	}
	
	/**
	 * 返回成功的布尔值和数据
	 * @param data
	 * @return
	 */
	public static Response success(Object data) {
		return new Response(true, data, null);
	}
	
	/**
	 * 返回成功的布尔值、数据和消息
	 * @param data
	 * @param msg
	 * @return
	 */
	public static Response success(Object data, String msg) {
		return new Response(true, data, msg);
	}
	
	/**
	 * 只返回失败的布尔值
	 * @return
	 */
	public static Response failure() {
		return new Response(false, null, null);
	}
	
	/**
	 * 返回失败的布尔值和消息
	 * @param msg
	 * @return
	 */
	public static Response failure(String msg) {
		return new Response(false, null, msg);
	}
	
	/**
	 * 返回失败的布尔值和数据
	 * @param data
	 * @return
	 */
	public static Response failure(Object data) {
		return new Response(false, data, null);
	}
	
	/**
	 * 返回失败的布尔值、数据和消息
	 * @param data
	 * @param msg
	 * @return
	 */
	public static Response failure(Object data, String msg) {
		return new Response(false, data, msg);
	}

	public Boolean getSuccess() {
		return success;
	}

	public Object getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}
	
}
